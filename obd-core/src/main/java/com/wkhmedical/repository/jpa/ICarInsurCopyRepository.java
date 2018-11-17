package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarInsurCopyBody;
import com.wkhmedical.dto.CarInsurCopyDTO;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.po.CarInsurCopy;

public interface ICarInsurCopyRepository {
	CarInsurCopy findByKey(String id);

	List<CarInsurCopyDTO> findCarInsurCopyList(CarInsurCopyBody paramBody, Pageable pageable);

	Page<CarInsurCopy> findPgCarInsurCopy(CarInsurCopyBody paramBody, Pageable pageable);

	Integer findCount(String id);

	void deleteByCid(String cid);

	Page<CarInsurDTO> findByExpDay(String expDateMin, String expDateMax, Pageable pageable);
}
