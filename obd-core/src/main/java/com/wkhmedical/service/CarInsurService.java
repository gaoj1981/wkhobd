package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurBodyAdd;
import com.wkhmedical.dto.CarInsurBodyEdit;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurParam;
import com.wkhmedical.po.CarInsur;

public interface CarInsurService {

	CarInsur getInfo(CarInsurParam paramBody);

	List<CarInsurDTO> getList(Paging<CarInsurBody> paramBody);
	
	Page<CarInsur> getPgList(Paging<CarInsurBody> paramBody);

	void addInfo(CarInsurBodyAdd infoBody);

	void updateInfo(CarInsurBodyEdit infoBody);

	void deleteInfo(String id);

	void delInfo(String id);
}
