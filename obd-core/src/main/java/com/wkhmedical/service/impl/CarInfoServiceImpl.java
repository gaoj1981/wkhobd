package com.wkhmedical.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.CarInfoBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.MgObdCar;
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
	public CarInfoDTO getCarInfo(CarInfoParam paramBody) {
		String eid = paramBody.getEid();
		CarInfo carInfo = carInfoRepository.findByEidAndDelFlag(eid, 0);
		if (carInfo == null) {
			log.error("查询不存在的车辆ID");
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		return AssistUtil.coverBean(carInfo, CarInfoDTO.class);
	}

	@Override
	@Transactional
	public void addCarInfo(CarInfoBody carInfoBody) {
		String eid = carInfoBody.getEid();
		// 判断是否存在已有的eid车辆
		CarInfo carInfoExist = carInfoRepository.findByEid(eid);
		if (carInfoExist != null) {
			throw new BizRuntimeException("carinfo_already_exists", eid);
		}
		// 判断是否存在已有的deviceNumber车辆
		String deviceNumber = carInfoBody.getDeviceNumber();
		if (!StringUtils.isEmpty(deviceNumber)) {
			carInfoExist = carInfoRepository.findByDeviceNumber(deviceNumber);
			if (carInfoExist != null) {
				throw new BizRuntimeException("carinfo_devicenumber_already_exists", deviceNumber);
			}
		}

		// 组装CarInfo
		CarInfo carInfo = AssistUtil.coverBean(carInfoBody, CarInfo.class);
		carInfo.setId(BizUtil.genDbId());
		carInfo.setGroupId(carInfo.getAreaId() + "");// 目前需求暂将区间ID作为分组标准
		carInfo.setDelFlag(0);

		// OBD关联处理
		List<MgObdCar> obdCarList = obdCarRepository.findByEidOrDeviceNumber(eid, deviceNumber);
		if (obdCarList == null) {
			// 新增时，是否必须存在OBD信息

		} else if (obdCarList.size() > 1) {
			// obd与eid重复匹配
			throw new BizRuntimeException("obdcar_eid_devicenumber_double", eid, deviceNumber);
		} else if (obdCarList.size() == 1) {
			// obd中正常存在设备情况
			MgObdCar obdCar = obdCarList.get(0);
			String eidObd = obdCar.getEid();
			if (!eid.equals(eidObd)) {
				if (StringUtils.isEmpty(eidObd)) {
					// 关联OBD中的eid与deviceNumber
					obdCar.setEid(carInfo.getEid());
					obdCarRepository.update(obdCar);
				} else {
					throw new BizRuntimeException("obdcar_devicenumber_already_exists", deviceNumber);
				}
			}
			if (StringUtils.isEmpty(deviceNumber)) {
				// 关联carInfo中的deviceNumber
				carInfo.setDeviceNumber(obdCar.getDeviceNumber());
			}

		}
		//
		carInfoRepository.save(carInfo);
	}

	@Override
	@Transactional
	public void updateCarInfo(CarInfoBody carInfoBody) {
		// id检查
		Long id = carInfoBody.getId();
		if (id == null) {
			throw new BizRuntimeException("info_edit_id_must", id);
		}
		// 判断待修改记录唯一性
		Optional<CarInfo> optObj = carInfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("carinfo_not_exists", id);
		}
		CarInfo carInfoUpd = optObj.get();

		// 判断eid的唯一性
		CarInfo carInfo;
		String eid = carInfoBody.getEid();
		String eidDB = carInfoUpd.getEid();
		if (!eid.equals(eidDB)) {
			carInfo = carInfoRepository.findByEid(eid);
			if (carInfo != null) {
				throw new BizRuntimeException("carinfo_already_exists", eid);
			}
		}
		// 判断deviceNumber的唯一性
		String deviceNumber = carInfoBody.getDeviceNumber();
		String deviceNumberDB = carInfoUpd.getDeviceNumber();
		if (!StringUtils.isEmpty(deviceNumber)) {
			if (!deviceNumber.equals(deviceNumberDB)) {
				carInfo = carInfoRepository.findByDeviceNumber(deviceNumber);
				if (carInfo != null) {
					throw new BizRuntimeException("carinfo_devicenumber_already_exists", deviceNumber);
				}
			}
		}
		// OBD关联处理
		MgObdCar obdCar = obdCarRepository.findByEid(eid);
		if (obdCar != null) {
			String deviceNumberObd = obdCar.getDeviceNumber();
			if (StringUtils.isEmpty(deviceNumberObd)) {
				// OBD的deviceNumber为空，说明存在脏数据
				throw new BizRuntimeException("obdcar_data_dirty");
			}
			if (!deviceNumberObd.equals(deviceNumber)) {
				if (deviceNumber == null) {
					if (deviceNumberDB == null) {
						// 关联OBD的deviceNumber
						carInfoUpd.setDeviceNumber(deviceNumberObd);
					} else if (!deviceNumberObd.equals(deviceNumberDB)) {
						throw new BizRuntimeException("obdcar_carinfo_nomatch", eid, deviceNumberObd);
					}
				} else {
					throw new BizRuntimeException("obdcar_eid_already_exists", eid);
				}
			}
		}
		if (!StringUtils.isEmpty(deviceNumber)) {
			obdCar = obdCarRepository.findByDeviceNumber(deviceNumber);
			if (obdCar != null) {
				String eidObd = obdCar.getEid();
				if (!StringUtils.isEmpty(eidObd) && !eidObd.equals(eid)) {
					throw new BizRuntimeException("obdcar_devicenumber_already_exists", deviceNumber);
				}
			}
		} else if (!StringUtils.isEmpty(deviceNumberDB)) {
			obdCar = obdCarRepository.findByDeviceNumber(deviceNumberDB);
			if (obdCar != null) {
				String eidObd = obdCar.getEid();
				if (!StringUtils.isEmpty(eidObd) && !eidObd.equals(eid)) {
					throw new BizRuntimeException("obdcar_carinfo_nomatch", eid, deviceNumberDB);
				}
			}
		}

		// 更新
		BeanUtils.merageProperty(carInfoUpd, carInfoBody);
		carInfoRepository.update(carInfoUpd);
	}

	@Override
	public void deleteCarInfo(Long id) {
		Optional<CarInfo> optObj = carInfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("carinfo_not_exists", id);
		}
		CarInfo carInfoUpd = optObj.get();
		carInfoUpd.setDelFlag(1);
		carInfoRepository.update(carInfoUpd);
	}
}
