package com.wkhmedical.repository.mongo.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Example;

import com.taoxeo.repository.MongoSupport;
import com.wkhmedical.po.MgObdCar;
import com.wkhmedical.repository.mongo.IObdCarRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;

public class ObdCarRepositoryImpl implements IObdCarRepository {
	@Resource
	MongoSupport mongoSupport;
	@Resource
	ObdCarRepository obdCarRepository;

	@Override
	public MgObdCar findObdExample(String deviceNumber) {
		MgObdCar obdCarQuery = new MgObdCar();
		obdCarQuery.setDeviceNumber(deviceNumber);
		Example<MgObdCar> example = Example.of(obdCarQuery);
		Optional<MgObdCar> optObj = obdCarRepository.findOne(example);
		if (optObj.isPresent()) {
			return optObj.get();
		}
		return null;
	}

}
