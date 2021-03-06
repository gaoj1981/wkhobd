package com.wkhmedical.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.CheckItemTotal;
import com.wkhmedical.dto.CheckPeopleTotal;
import com.wkhmedical.dto.CheckTypeTotal;
import com.wkhmedical.dto.DeviceCheckDTO;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.DeviceTimeBody;
import com.wkhmedical.dto.DeviceTimeDTO;
import com.wkhmedical.dto.LicInfoDTO;
import com.wkhmedical.dto.MonthAvgCarDTO;
import com.wkhmedical.dto.MonthAvgDisDTO;
import com.wkhmedical.dto.MonthAvgExamDTO;
import com.wkhmedical.dto.MonthAvgTimeDTO;
import com.wkhmedical.dto.ObdLicDTO;
import com.wkhmedical.exception.ObdLicException;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.DeviceCheck;
import com.wkhmedical.po.DeviceCheckTime;
import com.wkhmedical.po.DeviceMonth;
import com.wkhmedical.po.DeviceTime;
import com.wkhmedical.po.DeviceTimeRate;
import com.wkhmedical.po.DeviceTimeTemp;
import com.wkhmedical.po.MgObdCar;
import com.wkhmedical.po.MgObdLic;
import com.wkhmedical.po.MgObdLicReq;
import com.wkhmedical.po.MgObdLicSum;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.DeviceCheckRepository;
import com.wkhmedical.repository.jpa.DeviceCheckTimeRepository;
import com.wkhmedical.repository.jpa.DeviceMonthRepository;
import com.wkhmedical.repository.jpa.DeviceTimeRateRepository;
import com.wkhmedical.repository.jpa.DeviceTimeRepository;
import com.wkhmedical.repository.jpa.DeviceTimeTempRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.repository.mongo.ObdLicRepository;
import com.wkhmedical.repository.mongo.ObdLicReqRepository;
import com.wkhmedical.repository.mongo.ObdLicSumRepository;
import com.wkhmedical.service.ObdLicService;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.RSAUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ObdLicServiceImpl implements ObdLicService {

	@Resource
	ConfigProperties configProps;

	@Resource
	ObdLicRepository licRepository;
	@Resource
	ObdLicSumRepository licSumRepository;
	@Resource
	ObdLicReqRepository licReqRepository;
	@Resource
	DeviceCheckRepository dcRepository;
	@Resource
	CarInfoRepository carInfoRepository;
	@Resource
	DeviceCheckTimeRepository deviceCheckTimeRepository;
	@Resource
	DeviceTimeRepository deviceTimeRepository;
	@Resource
	DeviceTimeTempRepository deviceTimeTempRepository;
	@Resource
	DeviceTimeRateRepository deviceTimeRateRepository;
	@Resource
	DeviceMonthRepository deviceMonthRepository;

	@Resource
	ObdCarRepository obdCarRepository;

	@Override
	public ObdLicDTO getObdLic(String urlEid, String rsaStr) {
		String decodedData = null;
		// 获取并验证加密数据
		try {
			decodedData = RSAUtil.privateDecrypt(rsaStr, RSAUtil.getPrivateKey(configProps.getRsaPrivate()));
			log.info("解密后数据" + decodedData);
		}
		catch (Exception e) {
			throw new ObdLicException("obdlic_rsa_error");
		}

		JSONObject requestJso = JSONObject.parseObject(decodedData);
		String eid = requestJso.getString("eid");
		// 加密数据中与接口地址中ID是否一致
		if (!urlEid.equals(eid)) {
			throw new ObdLicException("obdlic_rsa_id_nomatch", urlEid);
		}

		List<MgObdLic> lstActiveLic = licRepository.findByEidAndStatus(eid, LicStatus.Active);
		// 判断使用中的授权
		if (lstActiveLic != null && lstActiveLic.size() > 1) {
			// 有多个使用中授权，直接返回错误信息
			throw new ObdLicException("obdlic_active_already", eid);
		}

		JSONObject licJso = requestJso.getJSONObject("lic");
		MgObdLic rtnLicInfo = null;
		if (licJso.isEmpty()) {
			// lic为空，则为首次上传（丢狗上传调用另外接口初始化，故不考虑）
			if (lstActiveLic.size() == 1) {
				// 返回此使用中授权（正常情况不会有此状态，防止网络异常时的二次请求）
				rtnLicInfo = lstActiveLic.get(0);
			}
			else {
				// Step1:先取试用
				rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.None, 0);
				// Step2:若无试用，优先取Wait状态Lic
				if (rtnLicInfo == null) {
					rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.Wait, null);
				}
				// Step3:无Wait状态Lic，取正常授权
				if (rtnLicInfo == null) {
					rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.None, null);
				}
				// Step4:授权状态更新为Active
				if (rtnLicInfo != null) {
					rtnLicInfo.setStatus(LicStatus.Active);
					licRepository.update(rtnLicInfo);
				}
			}
		}
		// 正常使用中的请求
		else {
			String sn = licJso.getString("sn");
			JSONObject stats = licJso.getJSONObject("stats");
			if (stats == null) {
				// 客户端POST数量无用量Key，直接返回错误信息
				throw new ObdLicException("obdlic_active_already", eid);
			}

			MgObdLic curLicInfo = licRepository.findByEidAndSn(eid, sn);
			if (curLicInfo == null) {
				throw new ObdLicException("obdlic_bizdata_error");
			}
			// 判断请求是否过期时间戳
			Long curReqSt = requestJso.getLong("st");
			MgObdLicReq licReqInfo = licReqRepository.findByEid(eid);
			if (licReqInfo != null) {
				Long reqSt = licReqInfo.getSt();
				if (curReqSt == null || reqSt.compareTo(curReqSt) > 0) {
					throw new ObdLicException("obdlic_bizdata_error_expire");
				}
				// 更新请求时间戳
				licReqInfo.setSt(curReqSt);
				licReqRepository.update(licReqInfo);
			}
			else {
				// 插入请求时间戳
				licReqInfo = new MgObdLicReq();
				licReqInfo.setEid(eid);
				licReqInfo.setSt(curReqSt);
				licReqRepository.save(licReqInfo);
			}

			Map<String, Integer> mapCurStats = curLicInfo.getStats();
			// 更新当前Lic已使用次数
			for (Map.Entry<String, Object> entry : stats.entrySet()) {
				try {
					mapCurStats.put(entry.getKey(), (Integer) entry.getValue());
				}
				catch (Exception e) {
					// 客户端POST用量数据非数值型，直接返回错误信息
					throw new ObdLicException("obdlic_bizdata_error");
				}
			}
			curLicInfo.setStats(mapCurStats);
			licRepository.update(curLicInfo);

			// 获取当前设备总次数
			MgObdLicSum licSum = licSumRepository.findByEid(eid);
			// 更新此设备的Lic使用总次数
			Map<String, Integer> mapSumStats = new HashMap<String, Integer>();
			JSONObject statsSum = requestJso.getJSONObject("stats");
			for (Map.Entry<String, Object> entry : statsSum.entrySet()) {
				try {
					mapSumStats.put(entry.getKey(), (Integer) entry.getValue());
				}
				catch (Exception e) {
					// 客户端POST用量数据非数值型，直接返回错误信息
					throw new ObdLicException("obdlic_bizdata_error");
				}
			}
			if (licSum == null) {
				licSum = new MgObdLicSum();
				licSum.setEid(eid);
				licSum.setUt(DateUtil.getTimestamp());
				licSum.setStats(mapSumStats);
				licSumRepository.save(licSum);
			}
			else {
				licSum.setUt(DateUtil.getTimestamp());
				licSum.setStats(mapSumStats);
				licSumRepository.update(licSum);
			}

			// 判断版本号v与数据库是否相同
			Long v = licJso.getLong("v");
			Long licv = curLicInfo.getV();
			if (v.equals(licv)) {
				// 针对授权的各种状态判断
				LicStatus status = curLicInfo.getStatus();
				if (!status.equals(LicStatus.Active)) {
					// 非使用状态情况，返回当前使用状态的授权
					if (lstActiveLic != null && lstActiveLic.size() == 1) {
						rtnLicInfo = lstActiveLic.get(0);
					}
					else {
						// 无可使用状态时，则查找返回Wait状态Lic
						rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.Wait, null);
					}
				}
				else {
					// 正常的使用状态下
					Map<String, Integer> mapCurConf = curLicInfo.getConf();
					// 判断是否存在需要结转的授权项
					boolean isCover = false;
					String xxKey = "";
					String xxLimKey = "";
					String entryKey = "";
					int entryValue = 0;
					int limitValue = 0;
					for (Map.Entry<String, Object> entry : stats.entrySet()) {
						entryKey = entry.getKey();
						xxKey = entryKey.replaceAll("_used", "");
						xxLimKey = xxKey + "_lim";
						limitValue = mapCurConf.get(xxLimKey) == null ? 0 : mapCurConf.get(xxLimKey);
						entryValue = (Integer) entry.getValue();
						if (entryValue >= limitValue) {
							// 如果授权中有任意一项数值超标，则进行结转操作
							isCover = true;
							break;
						}
					}
					// 结转操作
					if (isCover) {
						// 优先返回Wait状态Lic
						rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.Wait, null);
						if (rtnLicInfo == null) {
							// 无Wait状态Lic情况下，返回未使用
							rtnLicInfo = licRepository.getLicInfoGtNow(eid, LicStatus.None, null);
						}
						if (rtnLicInfo != null) {
							/**
							 * 客户要求只切Lic，无需结转，暂注释以下代码
							 */
							// // 对待授权进行结转
							// Map<String, Integer> mapConf = rtnLicInfo.getConf();
							// Integer licConfValue;
							// Integer curLimitValue;
							// Integer coverValue;
							// String xxLimCoverKey = "";
							// for (Map.Entry<String, Object> entry : stats.entrySet()) {
							// entryKey = entry.getKey();
							// xxKey = entryKey.replaceAll("_used", "");
							// xxLimKey = xxKey + "_lim";
							// xxLimCoverKey = xxLimKey + "_b";
							// // 当前授权XX项的总量
							// curLimitValue = mapCurConf.get(xxLimKey);
							// // 当前授权XX项的用量
							// entryValue = (Integer) entry.getValue();
							// // 当前授权XX项的余量
							// coverValue = curLimitValue - entryValue;
							// // 待授权的XX项总量
							// licConfValue = mapConf.get(xxLimKey);
							// // 更新待授权结转数据
							// if (licConfValue == null) {
							// // 结转授权中无此XX项，则添加此XX项
							// mapConf.put(xxLimKey, coverValue);
							// mapConf.put(xxLimCoverKey, coverValue);
							// }
							// else {
							// // 结转授权中有此XX项，则xx项次数+结转次数
							// mapConf.put(xxLimKey, licConfValue + coverValue);
							// // 待授权的XX项现有的结转量
							// Integer licConfCoverValue = mapConf.get(xxLimCoverKey);
							// if (licConfCoverValue == null) licConfCoverValue = 0;
							// // xx项结转次数 +本次结转次数
							// mapConf.put(xxLimCoverKey, licConfCoverValue + coverValue);
							// }
							// }
							// 结束当前授权
							curLicInfo.setStatus(LicStatus.Discard);
							licRepository.update(curLicInfo);
							// 更新待授权
							// rtnLicInfo.setConf(mapConf);//Lic需结转时，需打开
							rtnLicInfo.setStatus(LicStatus.Active);
							licRepository.update(rtnLicInfo);
						}
					}
				}
			}
			else {
				// 当前Lic为非Active时，属异常请求
				LicStatus status = curLicInfo.getStatus();
				if (!status.equals(LicStatus.Active)) {
					throw new ObdLicException("obdlic_bizdata_error");
				}
				rtnLicInfo = curLicInfo;
			}
		}

		log.info("后端算法后的授权信息" + JSON.toJSONString(rtnLicInfo));
		// 组装返回数据结构
		ObdLicDTO rtnDTO = new ObdLicDTO();
		LicInfoDTO licInfoDTO = new LicInfoDTO();
		rtnDTO.setEid(eid);
		rtnDTO.setSt(DateUtil.getTimestamp());
		if (rtnLicInfo != null) {
			licInfoDTO.setSn(rtnLicInfo.getSn());
			licInfoDTO.setExp(rtnLicInfo.getExp());
			licInfoDTO.setV(rtnLicInfo.getV());
			licInfoDTO.setStatus(rtnLicInfo.getStatus());
			// 删除XX项的结转Key
			Map<String, Integer> mapNewConf = new HashMap<String, Integer>();
			Map<String, Integer> mapConf = rtnLicInfo.getConf();
			for (Map.Entry<String, Integer> entry : mapConf.entrySet()) {
				if (entry.getKey().indexOf("_b") > 0) {
					continue;
				}
				mapNewConf.put(entry.getKey(), entry.getValue());
			}
			licInfoDTO.setConf(mapNewConf);
		}
		rtnDTO.setLic(licInfoDTO);

		return rtnDTO;
	}

	@Override
	public Long[] getLicCountArr(LicStatus status) {
		// 获取已使用/总授权量
		if (status == null) {
			Long[] rtnArr = new Long[2];
			// 获取正常使用完成的授权
			Long discardCount = licRepository.getCountByLicInfo(null, LicStatus.Discard, null);
			// 获取总授权数
			Long totalCount = licRepository.getCountByLicInfo(null, null, null);
			rtnArr[0] = discardCount;
			rtnArr[1] = totalCount;
			return rtnArr;
		}
		else {
			// 后续根据具体业务扩展
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#updateEquipCheck(java.lang.String)
	 */
	@Override
	@Transactional
	public void updateEquipCheck(String sendStr) {
		JSONObject requestJso = JSONObject.parseObject(sendStr);
		log.info("体检JSON：" + requestJso);
		//
		String eid = requestJso.getString("eid");
		CarInfo carInfo = carInfoRepository.findByEidAndDelFlag(eid, 0);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		//
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		//
		DeviceCheckDTO dcDTO = BizUtil.getDCheck4Json(requestJso);
		Long time = DateUtil.getTimestamp();
		String t0Str = dcDTO.getT0();
		String t1Str = dcDTO.getT1();
		Date t0;
		Date t1 = null;
		// 校验开始日期格式t0
		t0 = DateUtil.parseToDate(t0Str, "yyyyMMddHHmmss");
		if (t0 == null) {
			throw new BizRuntimeException("devicecheck_sdt_error");
		}
		String t0Str4Day = t0Str.substring(0, 8);
		// 校验结束日期格式t0
		if (StringUtils.isNotBlank(t1Str)) {
			String t1Str4Day = t1Str.substring(0, 8);
			if (!t1Str4Day.equals(t0Str4Day)) {
				throw new BizRuntimeException("devicecheck_edt_error");
			}
			t1 = DateUtil.parseToDate(t1Str, "yyyyMMddHHmmss");

			if (t1 == null) {
				throw new BizRuntimeException("devicecheck_edt_error");
			}
		}
		Date dtSend = DateUtil.parseToDate(t0Str4Day, "yyyyMMdd");

		//
		DeviceCheck dcObj;
		DeviceCheckTime dcTimeObj;
		String typeName;
		for (Map.Entry<String, String> entry : BizConstant.MAP_CHECK_ITEMS.entrySet()) {
			try {
				Long number = (Long) PropertyUtils.getProperty(dcDTO, entry.getKey());
				typeName = entry.getValue();
				dcObj = dcRepository.findByEidAndType(eid, typeName);
				// 处理体检汇总
				if (dcObj == null) {
					dcObj = new DeviceCheck();
					dcObj.setId(BizUtil.genDbIdStr(idWorker));
					dcObj.setEid(eid);
					dcObj.setType(typeName);
					dcObj.setStatus(BizUtil.getCheckStatus(typeName));
					dcObj.setNumber(number);
					dcObj.setTime(time);
					dcObj.setProvId(carInfo.getProvId());
					dcObj.setCityId(carInfo.getCityId());
					dcObj.setAreaId(carInfo.getAreaId());
					dcObj.setTownId(carInfo.getTownId());
					dcObj.setVillId(carInfo.getVillId());
					dcRepository.save(dcObj);
				}
				else {
					Long objNum = dcObj.getNumber();
					dcObj.setNumber(objNum + number);
					dcObj.setTime(time);
					dcRepository.update(dcObj);
				}
				// 处理体检日期汇总
				dcTimeObj = deviceCheckTimeRepository.findByEidAndTypeAndDt(eid, typeName, dtSend);
				if (dcTimeObj == null) {
					dcTimeObj = new DeviceCheckTime();
					dcTimeObj.setId(BizUtil.genDbIdStr(idWorker));
					dcTimeObj.setEid(eid);
					dcTimeObj.setType(typeName);
					dcTimeObj.setStatus(dcObj.getStatus());
					dcTimeObj.setNumber(number);
					dcTimeObj.setDt(dtSend);
					dcTimeObj.setProvId(carInfo.getProvId());
					dcTimeObj.setCityId(carInfo.getCityId());
					dcTimeObj.setAreaId(carInfo.getAreaId());
					dcTimeObj.setTownId(carInfo.getTownId());
					dcTimeObj.setVillId(carInfo.getVillId());
					deviceCheckTimeRepository.save(dcTimeObj);
				}
				else {
					Long objNum = dcTimeObj.getNumber();
					dcTimeObj.setNumber(objNum + number);
					deviceCheckTimeRepository.update(dcTimeObj);
				}
			}
			catch (Exception e) {
			}
		}
		// 处理开关机时间
		DeviceTimeTemp deviceTimeTemp = deviceTimeTempRepository.findByEidAndDt(eid, dtSend);
		if (deviceTimeTemp == null) {
			deviceTimeTemp = new DeviceTimeTemp();
			deviceTimeTemp.setId(BizUtil.genDbIdStr(idWorker));
			deviceTimeTemp.setEid(eid);
			deviceTimeTemp.setDt(dtSend);
			deviceTimeTemp.setSdt(t0);
			deviceTimeTemp.setEdt(t1);
			deviceTimeTemp.setFlag(0);
			deviceTimeTempRepository.save(deviceTimeTemp);
		}
		else {
			Date sdt = deviceTimeTemp.getSdt();
			if (sdt != null && sdt.compareTo(t0) > 0) {
				deviceTimeTemp.setSdt(t0);
			}
			Date edt = deviceTimeTemp.getEdt();
			if (t1 != null) {
				if (edt == null) {
					deviceTimeTemp.setEdt(t1);
				}
				else {
					if (edt.compareTo(t1) < 0) {
						deviceTimeTemp.setEdt(t1);
					}
				}
			}
			deviceTimeTempRepository.update(deviceTimeTemp);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#updateEquipStuff(java.lang.String)
	 */
	@Override
	public void updateEquipStuff(String sendStr) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getCheckSum(com.wkhmedical.dto.DeviceCheckSumBody)
	 */
	@Override
	public Long getCheckSum(DeviceCheckSumBody paramBody) {
		StringBuilder inStrs = new StringBuilder("");

		// 反射机制获取需要汇总的体检项
		for (Map.Entry<String, String> entry : BizConstant.MAP_CHECK_ITEMS.entrySet()) {
			try {
				Boolean isTrue = (Boolean) PropertyUtils.getProperty(paramBody, entry.getKey());
				if (isTrue) {
					inStrs.append("'" + entry.getValue() + "',");
				}
			}
			catch (Exception e) {
			}
		}

		String inTypeStr = inStrs.toString();
		if (inTypeStr.endsWith(",")) {
			inTypeStr = inTypeStr.substring(0, inTypeStr.length() - 1);
		}
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		return dcRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, inTypeStr);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.wkhmedical.service.ObdLicService#getCheckItemSum(com.wkhmedical.dto.DeviceCheckSumBody)
	 */
	@Override
	public CheckItemTotal getCheckItemSum(DeviceCheckSumBody paramBody) {
		StringBuilder inStrs = new StringBuilder("");

		// 反射机制获取需要汇总的体检项
		for (Map.Entry<String, String> entry : BizConstant.MAP_CHECK_ITEMS.entrySet()) {
			try {
				Boolean isTrue = (Boolean) PropertyUtils.getProperty(paramBody, entry.getKey());
				if (isTrue) {
					inStrs.append("'" + entry.getValue() + "',");
				}
			}
			catch (Exception e) {
			}
		}

		String inTypeStr = inStrs.toString();
		if (inTypeStr.endsWith(",")) {
			inTypeStr = inTypeStr.substring(0, inTypeStr.length() - 1);
		}
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();
		//
		CheckItemTotal rtnObj = new CheckItemTotal();
		List<DeviceCheck> lstObj = dcRepository.getCheckItemSum(eid, provId, cityId, areaId, townId, villId, inTypeStr);
		for (DeviceCheck dcTmp : lstObj) {
			try {
				PropertyUtils.setProperty(rtnObj, dcTmp.getType().toLowerCase(), dcTmp.getNumber());
			}
			catch (Exception e) {
			}
		}
		return rtnObj;
	}

	@Override
	public BigDecimal getCheckExpRate() {
		// 检测异常数
		BigDecimal expNum = dcRepository.getCheckSumByStatus(0);
		// 检测总数
		BigDecimal ttNum = dcRepository.getCheckSumByStatus(null);
		if (ttNum.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
		return expNum.divide(ttNum, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getCheckExpRate(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public BigDecimal getCheckExpRate(AreaCarBody paramBody) {
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		// 检测异常数
		BigDecimal expNum = deviceTimeRepository.getCkSum(eid, provId, cityId, areaId, townId, villId, 0);
		// 检测总数
		BigDecimal ttNum = deviceTimeRepository.getCkSum(eid, provId, cityId, areaId, townId, villId, 1);

		// // 检测异常数
		// BigDecimal expNum = dcRepository.getCheckSumByStatus(0, eid, provId, cityId, areaId,
		// townId, villId);
		// // 检测总数
		// BigDecimal ttNum = dcRepository.getCheckSumByStatus(1, eid, provId, cityId, areaId,
		// townId, villId);
		if (ttNum.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
		return expNum.divide(ttNum, 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100));
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#qzCheckTime(java.lang.String)
	 */
	@Override
	public void qzCheckTime() {
		Date dtNow = DateUtil.getCurDateByFormat("yyyy-MM-dd");
		List<DeviceTimeTemp> lstDtt = deviceTimeTempRepository.findTop2000ByFlagAndDtLessThan(0, dtNow);
		DeviceTime devTime;
		Date dt, sdt, edt;
		String eid, deviceNumber;
		CarInfo carInfo;
		MgObdCar obdCar;
		BigDecimal sdis, edis;
		String id;
		for (DeviceTimeTemp dtt : lstDtt) {
			try {
				// 判断eid是否存在
				eid = dtt.getEid();
				dt = dtt.getDt();
				devTime = deviceTimeRepository.findByEidAndDt(eid, dt);
				//
				if (devTime == null) {
					id = null;
					devTime = new DeviceTime();
				}
				else {
					id = devTime.getId();
				}
				BeanUtils.merageProperty(devTime, dtt);
				// 更新ID
				if (id != null) {
					devTime.setId(id);
				}
				sdt = dtt.getSdt();
				edt = dtt.getEdt();
				if (sdt == null) {
					sdt = DateUtil.parseToDate(DateUtil.getDateBegin(dt), "yyyy-MM-dd HH:mm:ss");
				}
				if (edt == null) {
					edt = DateUtil.parseToDate(DateUtil.getDateEnd(dt), "yyyy-MM-dd HH:mm:ss");
				}
				devTime.setTs(DateUtil.getDiffSeconds(sdt, edt));
				// 运营天数
				devTime.setWds(1);
				// 完善区域属性
				carInfo = carInfoRepository.findByEidAndDelFlag(eid, 0);
				if (carInfo != null) {
					devTime.setProvId(carInfo.getProvId());
					devTime.setCityId(carInfo.getCityId());
					devTime.setAreaId(carInfo.getAreaId());
					devTime.setTownId(carInfo.getTownId());
					devTime.setVillId(carInfo.getVillId());
				}
				// 更新行驶距离
				deviceNumber = carInfo.getDeviceNumber();
				if (StringUtils.isNotBlank(deviceNumber)) {
					obdCar = obdCarRepository.findTopByDeviceNumberAndInsTimeGreaterThanOrderByInsTimeAsc(deviceNumber, sdt);
					if (obdCar != null) {
						sdis = new BigDecimal(obdCar.getTotalMileage());
						sdis = sdis.setScale(4, BigDecimal.ROUND_HALF_UP);
					}
					else {
						sdis = BigDecimal.ZERO;
					}
					obdCar = obdCarRepository.findTopByDeviceNumberAndInsTimeLessThanOrderByInsTimeDesc(deviceNumber, edt);
					if (obdCar != null) {
						edis = new BigDecimal(obdCar.getTotalMileage());
						edis = edis.setScale(4, BigDecimal.ROUND_HALF_UP);
					}
					else {
						edis = BigDecimal.ZERO;
					}
					devTime.setDis(edis.subtract(sdis));
				}
				else {
					devTime.setDis(BigDecimal.ZERO);
				}
				// 检测人次统计
				DeviceCheckTime dctObj = deviceCheckTimeRepository.findByEidAndTypeAndDt(eid, BizConstant.MAP_CHECK_ITEMS.get("persontime"), dt);
				if (dctObj == null) {
					devTime.setPts(0L);
				}
				else {
					devTime.setPts(dctObj.getNumber());
				}
				// 检测正常数即总数
				BigDecimal ttNum = deviceCheckTimeRepository.getCheckSumByStatus(1, eid, dt);
				// 检测异常数
				BigDecimal expNum = deviceCheckTimeRepository.getCheckSumByStatus(0, eid, dt);
				// 检测项统计
				devTime.setCks(ttNum.longValue());
				// 检测异常百分比
				if (ttNum.compareTo(BigDecimal.ZERO) == 0) {
					devTime.setExprt(0L);
				}
				else {
					// devTime.setExprt(expNum.divide(ttNum, 2,
					// BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue());
					devTime.setExprt(expNum.longValue());
				}
				// 累计打印
				dctObj = deviceCheckTimeRepository.findByEidAndTypeAndDt(eid, BizConstant.MAP_CHECK_ITEMS.get("report"), dt);
				if (dctObj == null) {
					devTime.setRps(0L);
				}
				else {
					devTime.setRps(dctObj.getNumber());
				}
				// 移至正式开关机统计表中
				deviceTimeRepository.save(devTime);
				// 更改临时表的状态为已统计
				dtt.setFlag(1);
				deviceTimeTempRepository.update(dtt);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void qzDeviceTimeRate() {
		Date dtNow = DateUtil.getCurDateByFormat("yyyy-MM-dd");
		Date dtBegin = DateUtil.getDateAddDay(dtNow, -35);
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		BigDecimal rate, bdDay, bdSum = BigDecimal.ZERO;
		BigDecimal bd100 = new BigDecimal("100");
		//
		for (Date dtTmp = dtBegin; dtTmp.compareTo(dtNow) < 0; dtTmp = DateUtil.getDateAddDay(dtTmp, 1)) {
			try {
				// 判断此时点日出车率是否统计
				DeviceTimeRate dtRate = deviceTimeRateRepository.findByDt(dtTmp);
				if (dtRate != null) {
					// 出车率已统计
					continue;
				}
				// 判断此时点出车数是否统计
				Long timeTjNum = deviceTimeTempRepository.countByFlagAndDt(0, dtTmp);
				if (timeTjNum > 0) {
					// 此时点的车辆开关机尚未统计
					continue;
				}
				// 获取此时点的当天出车数
				Long carDaySum = deviceTimeRepository.countByDt(dtTmp);
				// 此时点的车辆总数
				Long carSum = carInfoRepository.findCarCountEndTime(dtTmp);
				if (carSum > 0) {
					// 计算日出车率 = (当日出车数/总车数)*100
					bdDay = new BigDecimal(carDaySum);
					bdSum = new BigDecimal(carSum);
					rate = bdDay.divide(bdSum, 2, BigDecimal.ROUND_HALF_UP).multiply(bd100);
				}
				else {
					rate = BigDecimal.ZERO;
				}
				// 入库
				dtRate = new DeviceTimeRate();
				dtRate.setId(BizUtil.genDbIdStr(idWorker));
				dtRate.setDt(dtTmp);
				dtRate.setRate(rate);
				deviceTimeRateRepository.save(dtRate);
			}
			catch (Exception e) {
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#qzMonthSum()
	 */
	@Override
	public void qzMonthSum() {
		Date dtNow = new Date();
		// 处理上个月数据
		String ymMonth = DateUtil.formatDate(DateUtil.getDateAddMonth(dtNow, -1), "yyyyMM");
		DeviceMonth deviceMonth = deviceMonthRepository.findByYmMonth(ymMonth);
		if (deviceMonth != null) {
			Date updTime = deviceMonth.getUpdTime();
			String updTimeStr = DateUtil.formatDate(updTime, "yyyyMM");
			if (updTimeStr.compareTo(ymMonth) <= 0) {
				// 需要重新更新
			}
		}

		// 处理本月数据
		ymMonth = DateUtil.getNowDateByFormat("yyyyMM");
		deviceMonth = deviceMonthRepository.findByYmMonth(ymMonth);
		if (deviceMonth != null) {
			//
		}
		else {

		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getDisMonthAvg(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public List<MonthAvgDisDTO> getDisMonthAvg(AreaCarBody paramBody) {
		List<MonthAvgDisDTO> rtnList = new ArrayList<MonthAvgDisDTO>();
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		//
		MonthAvgDisDTO monthAvgObj;
		BigDecimal disNum = BigDecimal.ZERO;
		Long carNum = 0L;
		BigDecimal avgNum = BigDecimal.ZERO;
		BigDecimal bcarNum;
		Date[] dtRangeArr;
		// 半年的月份列表
		String[] monthArr = DateUtil.getMonthHalfYear();
		for (String monthTmp : monthArr) {
			dtRangeArr = DateUtil.getMonthSTArr(monthTmp);
			// 运营距离
			disNum = deviceTimeRepository.getDisSum(eid, provId, cityId, areaId, townId, villId, dtRangeArr[0], dtRangeArr[1]);
			// 车辆数
			carNum = carInfoRepository.countByInsTimeLessThan(DateUtil.getDateAddDay(dtRangeArr[1], 1));
			// 平均运营时长
			if (carNum > 0) {
				bcarNum = new BigDecimal(carNum);
				avgNum = disNum.divide(bcarNum, 3, BigDecimal.ROUND_HALF_UP);
			}
			else {
				avgNum = BigDecimal.ZERO;
			}
			//
			monthAvgObj = new MonthAvgDisDTO();
			monthAvgObj.setMonth(Integer.valueOf(monthTmp.substring(4)));
			monthAvgObj.setAverageDistance(avgNum);
			rtnList.add(monthAvgObj);
		}
		return rtnList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getCheckMonthAvg(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public List<MonthAvgExamDTO> getCheckMonthAvg(AreaCarBody paramBody) {
		List<MonthAvgExamDTO> rtnList = new ArrayList<MonthAvgExamDTO>();
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();
		String type = BizConstant.MAP_CHECK_ITEMS.get("persontime");

		//
		MonthAvgExamDTO monthAvgObj;
		Long ckNum = 0L;
		Long carNum = 0L;
		Long avgNum = 0L;
		BigDecimal bckNum;
		BigDecimal bcarNum;
		Date[] dtRangeArr;
		// 一年的月份列表
		String[] monthArr = DateUtil.getMonthYear();
		for (String monthTmp : monthArr) {
			dtRangeArr = DateUtil.getMonthSTArr(monthTmp);
			// 体检人数
			ckNum = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, type, dtRangeArr[0], dtRangeArr[1]);
			// 车辆数
			carNum = carInfoRepository.countByDelFlagAndInsTimeLessThan(0, DateUtil.getDateAddDay(dtRangeArr[1], 1));
			// 平均体检人数
			if (carNum > 0) {
				bckNum = new BigDecimal(ckNum);
				bcarNum = new BigDecimal(carNum);
				avgNum = bckNum.divide(bcarNum, 0, BigDecimal.ROUND_HALF_UP).longValue();
			}
			else {
				avgNum = 0L;
			}
			//
			monthAvgObj = new MonthAvgExamDTO();
			monthAvgObj.setMonth(Integer.valueOf(monthTmp.substring(4)));
			monthAvgObj.setAvgExamNum(avgNum);
			rtnList.add(monthAvgObj);
		}
		return rtnList;
	}

	@Override
	public List<MonthAvgTimeDTO> getTimeMonthAvg(AreaCarBody paramBody) {
		List<MonthAvgTimeDTO> rtnList = new ArrayList<MonthAvgTimeDTO>();
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		//
		MonthAvgTimeDTO monthAvgObj;
		Long tsNum = 0L;
		Long carNum = 0L;
		Long avgNum = 0L;
		BigDecimal btsNum;
		BigDecimal bcarNum;
		Date[] dtRangeArr;
		// 一年的月份列表
		String[] monthArr = DateUtil.getMonthYear();
		for (String monthTmp : monthArr) {
			dtRangeArr = DateUtil.getMonthSTArr(monthTmp);
			// 运营时长
			tsNum = deviceTimeRepository.getTimeSum(eid, provId, cityId, areaId, townId, villId, dtRangeArr[0], dtRangeArr[1]);
			// 车辆数
			carNum = carInfoRepository.countByInsTimeLessThan(DateUtil.getDateAddDay(dtRangeArr[1], 1));
			// 平均运营时长
			if (carNum > 0) {
				btsNum = new BigDecimal(tsNum);
				bcarNum = new BigDecimal(carNum);
				avgNum = btsNum.divide(bcarNum, 0, BigDecimal.ROUND_HALF_UP).longValue();
			}
			else {
				avgNum = 0L;
			}
			//
			monthAvgObj = new MonthAvgTimeDTO();
			monthAvgObj.setMonth(Integer.valueOf(monthTmp.substring(4)));
			monthAvgObj.setOperatDuration(avgNum);
			rtnList.add(monthAvgObj);
		}
		return rtnList;
	}

	@Override
	public List<MonthAvgCarDTO> getCarMonthAvg(AreaCarBody paramBody) {
		List<MonthAvgCarDTO> rtnList = new ArrayList<MonthAvgCarDTO>();
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		//
		MonthAvgCarDTO monthAvgObj;
		Long runNum = 0L;
		Long norunNum = 0L;
		Long carNum = 0L;
		Date[] dtRangeArr;
		// 一年的月份列表
		String[] monthArr = DateUtil.getMonthYear();
		for (String monthTmp : monthArr) {
			dtRangeArr = DateUtil.getMonthSTArr(monthTmp);
			// 车辆数
			carNum = carInfoRepository.countByInsTimeLessThan(DateUtil.getDateAddDay(dtRangeArr[1], 1));
			// 出车数
			runNum = deviceTimeRepository.getCarSum(eid, provId, cityId, areaId, townId, villId, dtRangeArr[0], dtRangeArr[1]);
			// 未出车数
			norunNum = carNum - runNum;
			//
			monthAvgObj = new MonthAvgCarDTO();
			monthAvgObj.setMonth(Integer.valueOf(monthTmp.substring(4)));
			monthAvgObj.setOutwardRunNum(runNum);
			monthAvgObj.setNoDrivingOut(norunNum);
			rtnList.add(monthAvgObj);
		}
		return rtnList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getTimeTotal(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public Long getTimeTotal(AreaCarBody paramBody) {
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();
		// 运营时长
		return deviceTimeRepository.getTimeSum(eid, provId, cityId, areaId, townId, villId, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getCheckPeopleTotal(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public CheckPeopleTotal getCheckPeopleTotal(AreaCarBody paramBody) {
		// curTime
		Date dtNow = DateUtil.getCurDateByFormat("yyyy-MM-dd");
		Date dtEnd = dtNow;
		Date dtStart;
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();
		String type = BizConstant.MAP_CHECK_ITEMS.get("healthexam");
		// yesterdayData
		Date dtYesterday = DateUtil.getDateAddDay(dtNow, -1);
		Long yesterdayData = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, type, dtYesterday, dtYesterday);
		// weekData
		dtStart = DateUtil.parseToDate(DateUtil.getCurWeekBegin(), "yyyy-MM-dd");
		Long weekData = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, type, dtStart, dtEnd);
		// monthData
		dtStart = DateUtil.getCurMonthBegin();
		Long monthData = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, type, dtStart, dtEnd);
		// yearData
		dtStart = DateUtil.getCurYearBegin();
		Long yearData = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, type, dtStart, dtEnd);
		//
		CheckPeopleTotal rtnTotal = new CheckPeopleTotal();
		rtnTotal.setYesterdayData(yesterdayData);
		rtnTotal.setWeekData(weekData);
		rtnTotal.setMonthData(monthData);
		rtnTotal.setYearData(yearData);
		return rtnTotal;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getCheckTypeTotal(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public CheckTypeTotal getCheckTypeTotal(AreaCarBody paramBody) {
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();

		// 检测项总数目
		Long itemCount = deviceCheckTimeRepository.getCheckItemCount(eid, provId, cityId, areaId, townId, villId);
		// 中医体质人数
		Long tcmNum = deviceCheckTimeRepository.getCheckSum(eid, provId, cityId, areaId, townId, villId, BizConstant.MAP_CHECK_ITEMS.get("tcmexam"),
				null, null);
		//
		CheckTypeTotal rtnObj = new CheckTypeTotal();
		rtnObj.setInspectTotal(itemCount);
		rtnObj.setTcmConstitutionTotal(tcmNum);
		return rtnObj;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getDisTotal(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public BigDecimal getDisTotal(AreaCarBody paramBody) {
		// 业务数据准备
		String eid = paramBody.getEid();
		Long provId = paramBody.getProvId();
		Long cityId = paramBody.getCityId();
		Long areaId = paramBody.getAreaId();
		Long townId = paramBody.getTownId();
		Long villId = paramBody.getVillId();
		// 运营里程
		return deviceTimeRepository.getDisSum(eid, provId, cityId, areaId, townId, villId, null, null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdLicService#getDeviceTimePage(com.taoxeo.repository.Paging)
	 */
	@Override
	public Page<DeviceTimeDTO> getDeviceTimePage(Paging<DeviceTimeBody> paramBody) {
		DeviceTimeBody queryObj = paramBody.getQuery();
		Page<DeviceTimeDTO> pgInfo = deviceTimeRepository.findPgDeviceTimeDTO(queryObj, paramBody.toPageable());
		return pgInfo;
	}

}
