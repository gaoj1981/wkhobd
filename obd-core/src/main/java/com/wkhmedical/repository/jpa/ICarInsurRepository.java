package com.wkhmedical.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.po.CarInsur;

public interface ICarInsurRepository {
	CarInsur findByKey(String id);

	List<CarInsurDTO> findCarInsurList(CarInsurBody paramBody, Pageable pageable);

	Page<CarInsur> findPgCarInsur(CarInsurBody paramBody, Pageable pageable);

	Integer findCount(String id);

	Page<CarInsurDTO> findPgCarInsurDTO(CarInsurBody paramBody, Pageable pageable);

	Page<CarInsurDTO> findByExpDay(Integer expDayFlag);

	Date findMaxExpDate(String cid, Integer insurType);
}
