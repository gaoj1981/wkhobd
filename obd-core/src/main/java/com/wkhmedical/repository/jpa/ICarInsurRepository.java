package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.po.CarInsur;

public interface ICarInsurRepository {
	CarInsur findByKey(Long id);

	List<CarInsurDTO> findCarInsurList(CarInsurBody paramBody, Pageable pageable);

	Page<CarInsur> findPgCarInsur(CarInsurBody paramBody, Pageable pageable);

	Integer findCount(Long id);
}
