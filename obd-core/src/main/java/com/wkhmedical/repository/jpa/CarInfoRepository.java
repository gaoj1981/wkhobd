package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarInfo;

@Repository
public interface CarInfoRepository extends JpaRepository<CarInfo, Long>, ICarInfoRepository {

	CarInfo findByEid(String eid);

	CarInfo findByDeviceNumber(String deviceNumber);

	CarInfo findByEidAndDelFlag(String eid, int delFlag);

}