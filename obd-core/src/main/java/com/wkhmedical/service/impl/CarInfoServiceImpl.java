package com.wkhmedical.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarInfoServiceImpl implements CarInfoService {

	@Resource
	CarInfoRepository carInfoRepository;
	@Resource
	ObdCarRepository obdCarRepository;

	@Override
	public Page<CarInfo> getCarInfoPage(CarInfoPage paramBody) {
		Page<CarInfo> pgCarInfo = carInfoRepository.findPgCarInfo(paramBody);
		return pgCarInfo;
	}

	@Override
	public List<CarInfoDTO> getCarInfoList(CarInfoPage paramBody) {
		return carInfoRepository.findCarInfoList(paramBody);
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

		// 组装CarInfo
		CarInfo carInfo = AssistUtil.coverBean(carInfoBody, CarInfo.class);
		Integer areaId = carInfo.getAreaId();
		Long id = BizUtil.genDbId();
		carInfo.setId(id);
		carInfo.setGroupId(areaId + "");// 目前需求暂将区间ID作为分组标准
		carInfo.setProvId(BizUtil.getProvId(areaId));
		carInfo.setCityId(BizUtil.getCityId(areaId));
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

		// 更新
		BeanUtils.merageProperty(carInfoUpd, carInfoBody);
		carInfoRepository.update(carInfoUpd);

	}

	@Override
	public void deleteCarInfo(String eid) {
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo != null) {
			carInfo.setEid(BizUtil.getDelBackupVal(eid));
			carInfo.setDelFlag(1);
			carInfoRepository.update(carInfo);
		}
	}
}
