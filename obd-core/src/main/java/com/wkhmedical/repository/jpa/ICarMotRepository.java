package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.po.CarMot;

public interface ICarMotRepository {
	CarMot findByKey(String id);

	List<CarMotDTO> findCarMotList(CarMotBody paramBody, Pageable pageable);

	Page<CarMot> findPgCarMot(CarMotBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
