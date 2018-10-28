package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarMotCopyBody;
import com.wkhmedical.dto.CarMotCopyDTO;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarMotCopy;

public interface ICarMotCopyRepository {
	CarMotCopy findByKey(Long id);

	List<CarMotCopyDTO> findCarMotCopyList(CarMotCopyBody paramBody, Pageable pageable);

	Page<CarMotCopy> findPgCarMotCopy(CarMotCopyBody paramBody, Pageable pageable);

	Integer findCount(Long id);

	void deleteByCid(String cid);

	Page<CarMotDTO> findByExpDay(String expDateMin, String expDateMax, Pageable pageable);
}
