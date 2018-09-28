package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarInsur;

@Repository
public interface CarInsurRepository extends JpaRepository<CarInsur, Long>, ICarInsurRepository {

	CarInsur findByCidAndInsurType(Long cid, Integer insurType);
	
	CarInsur findByInsurNum(String insurNum);
}
