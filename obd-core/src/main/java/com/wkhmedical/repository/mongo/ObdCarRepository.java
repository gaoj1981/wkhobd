package com.wkhmedical.repository.mongo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.MongoRepository;
import com.wkhmedical.po.MgObdCar;

@Repository
public interface ObdCarRepository extends MongoRepository<MgObdCar, String>, IObdCarRepository {

	MgObdCar findByDeviceNumber(String deviceNumber);

	MgObdCar findByEid(String eid);

	List<MgObdCar> findByEidOrDeviceNumber(String deviceNumber, String eid);
}
