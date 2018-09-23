package com.wkhmedical.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.po.MgObdCar;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.service.ObdCarService;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ObdCarServiceImpl implements ObdCarService {

	@Resource
	ObdCarRepository carRepository;

	@Override
	public void saveObdCar(CarSendDTO carInfo) {
		String deviceNumber = carInfo.getDeviceNumber();
		MgObdCar obdCar = carRepository.findByDeviceNumber(deviceNumber);
		if (obdCar == null) {
			obdCar = BizUtil.convertCarInfo(carInfo);
			carRepository.save(obdCar);
		}
		else {
			MgObdCar obdCarUpd = BizUtil.convertCarInfo(carInfo);
			obdCarUpd.setId(obdCar.getId());
			carRepository.update(obdCarUpd);
		}
	}

}
