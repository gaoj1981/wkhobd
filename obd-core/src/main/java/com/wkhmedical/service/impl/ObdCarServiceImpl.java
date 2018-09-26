package com.wkhmedical.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.MgObdCar;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.service.ObdCarService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ObdCarServiceImpl implements ObdCarService {

	@Resource
	ObdCarRepository obdCarRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public void saveObdCar(CarSendDTO carSend) {
		String deviceNumber = carSend.getDeviceNumber();
		if (deviceNumber == null) {
			throw new BizRuntimeException("obdcar_not_devicenumber");
		}
		MgObdCar obdCar = obdCarRepository.findByDeviceNumber(deviceNumber);
		if (obdCar == null) {
			obdCar = BizUtil.convertCarInfo(carSend);
			// 获取对应的车辆基本信息
			CarInfo carInfo = carInfoRepository.findByDeviceNumber(deviceNumber);
			if (carInfo != null) {
				String eid = carInfo.getEid();
				// 判断eid是否存在于OBD中
				MgObdCar obdEidCar = obdCarRepository.findByEid(eid);
				if (obdEidCar == null) {
					// 建立eid与deviceNumber关系
					obdCar.setEid(eid);
				} else {
					log.error(eid + "与" + deviceNumber + "重复匹配");
					throw new BizRuntimeException("obdcar_eid_devicenumber_error", eid, deviceNumber);
				}
			}
			obdCarRepository.save(obdCar);
		} else {
			MgObdCar obdCarUpd = BizUtil.convertCarInfo(carSend);
			obdCarUpd.setId(obdCar.getId());
			// 防止实时数据覆盖eid与devicenumber匹配关系
			obdCarUpd.setEid(obdCar.getEid());
			obdCarRepository.update(obdCarUpd);
		}
	}

	@Override
	public ObdCarDTO getObdCar(String eid) {
		MgObdCar obdCar = obdCarRepository.findByEid(eid);
		if (obdCar == null) {
			throw new BizRuntimeException("obdcar_not_related", eid);
		}
		return AssistUtil.coverBean(obdCar, ObdCarDTO.class);
	}

}
