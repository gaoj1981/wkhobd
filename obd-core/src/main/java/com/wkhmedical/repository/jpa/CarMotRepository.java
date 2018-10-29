package com.wkhmedical.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.CarMot;

@Repository
public interface CarMotRepository extends JpaRepository<CarMot, String>, ICarMotRepository {
	List<CarMot> findByCidAndDelFlagAndExpDateGreaterThanAndExpDateLessThanOrderByExpDateDesc(String cid, Integer delFlag, Date dt1, Date dt2);

	List<CarMot> findByCidAndDelFlagOrderByExpDateDesc(String cid, Integer delFlag);
}
