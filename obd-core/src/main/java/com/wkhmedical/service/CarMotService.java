package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarMot;

public interface CarMotService {

	CarMot getInfo(CarMotBody paramBody);

	List<CarMotDTO> getList(CarMotBody paramBody);
	
	Page<CarMot> getPgList(CarMotBody paramBody);

	void addInfo(CarMotBody infoBody);

	void updateInfo(CarMotBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);
}
