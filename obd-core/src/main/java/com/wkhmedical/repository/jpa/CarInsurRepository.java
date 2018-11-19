package com.wkhmedical.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarInsur;

@Repository
public interface CarInsurRepository extends JpaRepository<CarInsur, String>, ICarInsurRepository {
	List<CarInsur> findByCidAndInsurTypeAndDelFlagAndExpDateGreaterThanAndExpDateLessThanOrderByExpDateDesc(String cid, Integer insurType,
			Integer delFlag, Date dt1, Date dt2);

	List<CarInsur> findByCidAndInsurTypeAndDelFlagOrderByExpDateDesc(String cid, Integer insurType, Integer delFlag);

	CarInsur findByCidAndInsurType(String cid, Integer insurType);

	CarInsur findByInsurNum(String insurNum);
}
