package com.wkhmedical.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wkhmedical.config.ConfigProperties;
import com.wkhmedical.constant.LicStatus;
import com.wkhmedical.dto.LicInfoDTO;
import com.wkhmedical.dto.ObdLicDTO;
import com.wkhmedical.exception.ObdLicException;
import com.wkhmedical.po.MgObdLic;
import com.wkhmedical.po.MgObdLicReq;
import com.wkhmedical.po.MgObdLicSum;
import com.wkhmedical.repository.mongo.ObdLicRepository;
import com.wkhmedical.repository.mongo.ObdLicReqRepository;
import com.wkhmedical.repository.mongo.ObdLicSumRepository;
import com.wkhmedical.service.ObdLicService;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.RSAUtil;

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

	@Override
	public ObdLicDTO getObdLic(String id, String rsaStr) {
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
		String did = requestJso.getString("id");
		// 加密数据中与接口地址中ID是否一致
		if (!id.equals(did)) {
			throw new ObdLicException("obdlic_rsa_id_nomatch", id);
		}

		List<MgObdLic> lstActiveLic = licRepository.findByDidAndStatus(did, LicStatus.Active);
		// 判断使用中的授权
		if (lstActiveLic != null && lstActiveLic.size() > 1) {
			// 有多个使用中授权，直接返回错误信息
			throw new ObdLicException("obdlic_active_already", did);
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
				rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.None, 0);
				// Step2:若无试用，优先取Wait状态Lic
				if (rtnLicInfo == null) {
					rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.Wait, null);
				}
				// Step3:无Wait状态Lic，取正常授权
				if (rtnLicInfo == null) {
					rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.None, null);
				}
				// Step4:授权状态更新为Active
				rtnLicInfo.setStatus(LicStatus.Active);
				licRepository.update(rtnLicInfo);
			}
		}
		// 正常使用中的请求
		else {
			String sn = licJso.getString("sn");
			JSONObject stats = licJso.getJSONObject("stats");
			if (stats == null) {
				// 客户端POST数量无用量Key，直接返回错误信息
				throw new ObdLicException("obdlic_active_already", did);
			}

			MgObdLic curLicInfo = licRepository.findByDidAndSn(id, sn);
			if (curLicInfo == null) {
				throw new ObdLicException("obdlic_bizdata_error");
			}
			// 判断请求是否过期时间戳
			Long curReqSt = requestJso.getLong("st");
			MgObdLicReq licReqInfo = licReqRepository.findByDid(did);
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
				licReqInfo.setDid(did);
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
			MgObdLicSum licSum = licSumRepository.findByDid(did);
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
				licSum.setDid(did);
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
						rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.Wait, null);
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
						limitValue = mapCurConf.get(xxLimKey);
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
						rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.Wait, null);
						if (rtnLicInfo == null) {
							// 无Wait状态Lic情况下，返回未使用
							rtnLicInfo = licRepository.getLicInfoGtNow(did, LicStatus.None, null);
						}
						String xxLimCoverKey = "";
						if (rtnLicInfo != null) {
							// 对待授权进行结转
							Map<String, Integer> mapConf = rtnLicInfo.getConf();
							Integer licConfValue;
							Integer curLimitValue;
							Integer coverValue;
							for (Map.Entry<String, Object> entry : stats.entrySet()) {
								entryKey = entry.getKey();
								xxKey = entryKey.replaceAll("_used", "");
								xxLimKey = xxKey + "_lim";
								xxLimCoverKey = xxLimKey + "_b";
								// 当前授权XX项的总量
								curLimitValue = mapCurConf.get(xxLimKey);
								// 当前授权XX项的用量
								entryValue = (Integer) entry.getValue();
								// 当前授权XX项的余量
								coverValue = curLimitValue - entryValue;
								// 待授权的XX项总量
								licConfValue = mapConf.get(xxLimKey);
								// 更新待授权结转数据
								if (licConfValue == null) {
									// 结转授权中无此XX项，则添加此XX项
									mapConf.put(xxLimKey, coverValue);
									mapConf.put(xxLimCoverKey, coverValue);
								}
								else {
									// 结转授权中有此XX项，则xx项次数+结转次数
									mapConf.put(xxLimKey, licConfValue + coverValue);
									// 待授权的XX项现有的结转量
									Integer licConfCoverValue = mapConf.get(xxLimCoverKey);
									if (licConfCoverValue == null) licConfCoverValue = 0;
									// xx项结转次数 +本次结转次数
									mapConf.put(xxLimCoverKey, licConfCoverValue + coverValue);
								}
							}
							// 结束当前授权
							curLicInfo.setStatus(LicStatus.Discard);
							licRepository.update(curLicInfo);
							// 更新待授权及
							rtnLicInfo.setConf(mapConf);
							rtnLicInfo.setStatus(LicStatus.Active);
							licRepository.update(rtnLicInfo);
						}
					}
				}
			}
			else {
				rtnLicInfo = curLicInfo;
			}
		}

		log.info("后端算法后的授权信息" + JSON.toJSONString(rtnLicInfo));
		// 组装返回数据结构
		ObdLicDTO rtnDTO = new ObdLicDTO();
		LicInfoDTO licInfoDTO = new LicInfoDTO();
		rtnDTO.setId(did);
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

}
