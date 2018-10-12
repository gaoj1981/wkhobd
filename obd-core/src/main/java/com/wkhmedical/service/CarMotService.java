package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarMot;

public interface CarMotService {

	CarMot getInfo(CarMotBody paramBody);

	List<CarMotDTO> getList(Paging<CarMotBody> paramBody);
	
	Page<CarMot> getPgList(Paging<CarMotBody> paramBody);

	void addInfo(CarMotBody infoBody);

	void updateInfo(CarMotBody infoBody);

	void deleteInfo(String id);

	void delInfo(String id);
}
