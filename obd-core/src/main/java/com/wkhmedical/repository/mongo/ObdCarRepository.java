package com.wkhmedical.repository.mongo;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgObdCar;

@Repository
public interface ObdCarRepository extends MongoRepository<MgObdCar, String>, IObdCarRepository {

	MgObdCar findByDeviceNumber(String deviceNumber);

}
