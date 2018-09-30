package com.wkhmedical.repository.mongo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgObdCar;

@Repository
public interface ObdCarRepository extends MongoRepository<MgObdCar, String>, IObdCarRepository {

	MgObdCar findByDeviceNumber(String deviceNumber);

	List<MgObdCar> findTopByDeviceNumberOrderByInsTimeDesc(String deviceNumber);
	
}
