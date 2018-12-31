package com.wkhmedical.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.AreaCarDTO;
import com.wkhmedical.dto.CarAreaNum;
import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.po.BaseArea;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.BaseAreaRepository;
import com.wkhmedical.repository.jpa.BindUserRepository;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.DeviceTimeRateRepository;
import com.wkhmedical.repository.jpa.EquipInfoRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarInfoServiceImpl implements CarInfoService {

	@Resource
	CarInfoRepository carInfoRepository;
	@Resource
	BindUserRepository bindUserRepository;
	@Resource
	ObdCarRepository obdCarRepository;
	@Resource
	EquipInfoRepository equipInfoRepository;
	@Resource
	BaseAreaRepository baseAreaRepository;
	@Resource
	DeviceTimeRateRepository deviceTimeRateRepository;

	@Override
	public Page<CarInfo> getCarInfoPage(Paging<CarInfoPageParam> paramBody) {
		CarInfoPageParam queryObj = paramBody.getQuery();
		Page<CarInfo> pgCarInfo = carInfoRepository.findPgCarInfo(queryObj, paramBody.toPageable());
		return pgCarInfo;
	}

	@Override
	public List<CarInfoDTO> getCarInfoList(Paging<CarInfoPageParam> paramBody) {
		CarInfoPageParam queryObj = paramBody.getQuery();
		return carInfoRepository.findCarInfoList(queryObj, paramBody.toPageable());
	}

	@Override
	public CarInfo getInfo(CarInfoParam paramBody) {
		String id = paramBody.getId();
		Optional<CarInfo> optObj = carInfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public CarInfo getCarInfo(CarInfoParam paramBody) {
		String id = paramBody.getId();
		String eid = paramBody.getEid();
		String deviceNumber = paramBody.getDeviceNumber();
		if (StringUtils.isNotBlank(id)) {
			Optional<CarInfo> optObj = carInfoRepository.findById(id);
			if (optObj.isPresent()) {
				return optObj.get();
			}
		}
		else if (StringUtils.isNotBlank(eid)) {
			return carInfoRepository.findByEid(eid);
		}
		else if (StringUtils.isNotBlank(deviceNumber)) {
			return carInfoRepository.findByDeviceNumber(deviceNumber);
		}
		return null;
	}

	@Override
	public CarInfoDTO getCarInfo(String eid) {
		CarInfoParam paramBody = new CarInfoParam();
		paramBody.setEid(eid);
		CarInfoDTO carInfoDTO = carInfoRepository.findCarInfo(paramBody);
		if (carInfoDTO == null) {
			log.error("查询不存在的车辆ID");
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		return carInfoDTO;
	}

	@Override
	@Transactional
	public void addCarInfo(CarInfoAddBody carInfoBody) {
		String eid = carInfoBody.getEid();
		// 判断是否存在已有的eid车辆
		CarInfo carInfoExist = carInfoRepository.findByEid(eid);
		if (carInfoExist != null) {
			throw new BizRuntimeException("carinfo_already_exists", eid);
		}
		// 判断车辆信息中是否存在已有的deviceNumber车辆
		String deviceNumber = carInfoBody.getDeviceNumber();
		if (!StringUtils.isEmpty(deviceNumber)) {
			carInfoExist = carInfoRepository.findByDeviceNumber(deviceNumber);
			if (carInfoExist != null) {
				throw new BizRuntimeException("carinfo_devicenumber_already_exists", deviceNumber);
			}
		}
		// 判断车牌号唯一性
		String plateNum = carInfoBody.getPlateNum();
		if (StringUtils.isNoneBlank(plateNum)) {
			CarInfo plateInfo = carInfoRepository.findByPlateNum(plateNum);
			if (plateInfo != null) {
				throw new BizRuntimeException("carinfo_platenum_already_exists", plateNum);
			}
		}
		//
		CarInfo carInfo = AssistUtil.coverBean(carInfoBody, CarInfo.class);
		Long areaId = carInfo.getAreaId();
		// 获取车辆所在区县运营维护负责人
		BindUser bu1 = bindUserRepository.findByUtypeAndAreaIdAndIsDefault(1, areaId, 1);
		BindUser bu2 = bindUserRepository.findByUtypeAndAreaIdAndIsDefault(2, areaId, 1);
		// 组装CarInfo
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		String id = BizUtil.genDbIdStr(idWorker);
		carInfo.setId(id);
		carInfo.setGroupId(areaId + "");// 目前需求暂将区间ID作为分组标准
		carInfo.setProvId(BizUtil.getProvId4Long(areaId));
		carInfo.setCityId(BizUtil.getCityId4Long(areaId));
		carInfo.setPrinId(bu1 != null ? bu1.getId() : null);
		carInfo.setMaintId(bu2 != null ? bu2.getId() : null);
		carInfo.setDelFlag(0);
		// 车辆信息入库
		carInfoRepository.save(carInfo);

	}

	@Override
	@Transactional
	public void updateCarInfo(CarInfoEditBody carInfoBody) {
		// 校验车辆是否存在
		String eid = carInfoBody.getEid();
		CarInfo carInfoUpd = carInfoRepository.findByEid(eid);
		if (carInfoUpd == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		// 判断deviceNumber的唯一性
		String deviceNumber = carInfoBody.getDeviceNumber();
		String deviceNumberDB = carInfoUpd.getDeviceNumber();
		if (!StringUtils.isEmpty(deviceNumber)) {
			if (!deviceNumber.equals(deviceNumberDB)) {
				CarInfo carInfo = carInfoRepository.findByDeviceNumber(deviceNumber);
				if (carInfo != null) {
					throw new BizRuntimeException("carinfo_devicenumber_already_exists", deviceNumber);
				}
			}
		}
		// 判断车牌号唯一性
		String plateNum = carInfoBody.getPlateNum();
		if (StringUtils.isNoneBlank(plateNum)) {
			CarInfo plateInfo = carInfoRepository.findByPlateNum(plateNum);
			if (plateInfo != null && !eid.equals(plateInfo.getEid())) {
				throw new BizRuntimeException("carinfo_platenum_already_exists", plateNum);
			}
		}
		// 更新
		BeanUtils.merageProperty(carInfoUpd, carInfoBody);
		carInfoRepository.update(carInfoUpd);

	}

	@Override
	@Transactional
	public void deleteCarInfo(String eid) {
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo != null) {
			carInfo.setEid(BizUtil.getDelBackupVal(eid));
			carInfo.setDelFlag(1);
			carInfoRepository.update(carInfo);
			// 同步删除其下所有设备
			EquipInfoBody paramBody = new EquipInfoBody();
			paramBody.setEid(eid);
			equipInfoRepository.deleteByParam(paramBody);
		}
	}

	@Override
	@Transactional
	public void delInfo(String id) {
		Optional<CarInfo> optObj = carInfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		CarInfo carInfo = optObj.get();
		String eid = carInfo.getEid();
		carInfo.setEid(BizUtil.getDelBackupVal(eid));
		carInfo.setDelFlag(1);
		carInfoRepository.update(carInfo);
		// 同步删除其下所有设备
		EquipInfoBody paramBody = new EquipInfoBody();
		paramBody.setEid(eid);
		equipInfoRepository.deleteByParam(paramBody);
	}

	@Override
	public Long getCountSum() {
		return carInfoRepository.findCountSum();
	}

	@Override
	public List<ChartCarDTO> getChartCarList(Integer groupType) {
		CarInfoParam paramBody = new CarInfoParam();
		paramBody.setGroupByProv(false);
		if (groupType == 1) {
			// 按省份统计
			paramBody.setGroupByProv(true);
		}
		List<ChartCarDTO> lstChart = carInfoRepository.findCarCountGroupBy(paramBody);
		// 涵盖所有省
		Map<Integer, String> mapProv = new HashMap<Integer, String>();
		mapProv.putAll(BizConstant.MAP_PROV_ELLIPSIS);
		// 统计省份不存在，则置0
		for (ChartCarDTO chartCar : lstChart) {
			chartCar.setY(chartCar.getCountNum());
			chartCar.setX(mapProv.get(chartCar.getProvId()));
			mapProv.remove(chartCar.getProvId());
		}
		ChartCarDTO chartCar;
		for (String value : mapProv.values()) {
			chartCar = new ChartCarDTO();
			chartCar.setX(value);
			chartCar.setY(0);
			lstChart.add(chartCar);
		}
		return lstChart;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.CarInfoService#getAreaCar(com.wkhmedical.dto.AreaCarBody)
	 */
	@Override
	public AreaCarDTO getAreaCar(AreaCarBody paramBody) {
		AreaCarDTO areaCarDTO = new AreaCarDTO();
		Map<String, Object> mapArea = new HashMap<String, Object>();
		// 地区ID
		Long pid = null;
		String areaKey = null;
		// 判断eid是否传递
		String eid = paramBody.getEid();
		if (StringUtils.isNotBlank(eid)) {
			mapArea.put("eid", eid);
		}
		else {
			// 未传递eid时，则需获取调用方传递的区域分类ID
			try {
				for (String keyStr : BizConstant.AREA_KEY_ARR) {
					Long aid = (Long) PropertyUtils.getProperty(paramBody, keyStr);
					if (aid != null) {
						mapArea.put(keyStr, aid);
						pid = aid;
						areaKey = keyStr;
						break;
					}
				}
			}
			catch (Exception e) {
			}
		}
		// 判断参数是否有误
		if (mapArea.isEmpty()) {
			throw new BizRuntimeException("obdlic_bizdata_error");
		}
		// 获取符合条件的车总数
		Long vehicleTotal = 1L;
		// 获取符合条件的覆盖乡镇数
		Long townshipsTotal = 1L;
		// 是否eid查询情况
		if (StringUtils.isBlank(eid) && pid != null) {
			// 车总数
			vehicleTotal = carInfoRepository.findCarCountByMapArea(mapArea);
			// 覆盖乡镇数
			BaseArea baseArea = new BaseArea();
			baseArea.setPid(pid);
			Example<BaseArea> example = Example.of(baseArea);
			townshipsTotal = baseAreaRepository.count(example);
		}
		else {
			CarInfo carInfo = carInfoRepository.findByEid(eid);
			pid = carInfo.getAreaId();
			areaKey = BizConstant.AREA_KEY_ARR[2];
		}
		// 其下区域key
		String areaKeyNext = BizUtil.getNextArea(areaKey);
		// 其下区域车辆数
		List<CarAreaNum> lstCarAreaNum = carInfoRepository.findCarCountAreaGroupBy(pid, areaKey, areaKeyNext);
		//
		areaCarDTO.setQueryId(pid);
		areaCarDTO.setVehicleTotal(vehicleTotal);
		areaCarDTO.setTownshipsTotal(townshipsTotal);
		areaCarDTO.setData(lstCarAreaNum);
		return areaCarDTO;
	}

	@Override
	public BigDecimal getCarMonthRate() {
		int month = DateUtil.getNowMonth();
		BigDecimal monthRateSum = BigDecimal.ZERO;
		int monthNum = 0;
		for (int i = 1; i < month; i++) {
			BigDecimal days = new BigDecimal(DateUtil.getDaysOfMonth(i));
			Date[] dtMonthArr = DateUtil.getMonthSTArr(month);
			BigDecimal bdRateSum = deviceTimeRateRepository.getRateSumByDate(dtMonthArr[0], dtMonthArr[1]);
			if (bdRateSum.compareTo(BigDecimal.ZERO) > 0) {
				monthNum++;
				monthRateSum.add(bdRateSum.divide(days));
			}
		}
		BigDecimal avgMonthRateSum = monthRateSum.divide(new BigDecimal(monthNum)).setScale(2, BigDecimal.ROUND_HALF_UP);
		return avgMonthRateSum;
	}
}
