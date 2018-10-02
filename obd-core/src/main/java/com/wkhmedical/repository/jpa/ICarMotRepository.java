package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.po.CarMot;

public interface ICarMotRepository {
	CarMot findByKey(Long id);
	
	List<CarMotDTO> findCarMotList(CarMotBody paramBody);
	
	Page<CarMot> findPgCarMot(CarMotBody paramBody);

	Integer findCount(Long id);
}
