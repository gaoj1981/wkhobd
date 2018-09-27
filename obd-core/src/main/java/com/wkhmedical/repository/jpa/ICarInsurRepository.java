package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurPage;
import com.wkhmedical.po.CarInsur;

public interface ICarInsurRepository {
	
	List<CarInsurDTO> findCarInsurList(CarInsurPage paramBody);
	
	Page<CarInsur> findPgCarInsur(CarInsurPage paramBody);

	Integer findCount(Long id);
}
