package com.wkhmedical.repository.jpa;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarInfo;

@Repository
public interface CarInfoRepository extends JpaRepository<CarInfo, String>, ICarInfoRepository {

	CarInfo findByEid(String eid);

	CarInfo findByDeviceNumber(String deviceNumber);

	CarInfo findByEidAndDelFlag(String eid, int delFlag);

	CarInfo findByPlateNum(String plateNum);

	Long countByInsTimeLessThan(Date dt);

	Long countByDelFlagAndInsTimeLessThan(int delFlag, Date dt);
}
