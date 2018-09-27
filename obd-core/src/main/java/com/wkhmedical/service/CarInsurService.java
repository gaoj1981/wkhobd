package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurPage;
import com.wkhmedical.dto.CarInsurParam;
import com.wkhmedical.po.CarInsur;

public interface CarInsurService {

	CarInsur getInfo(CarInsurParam paramBody);

	List<CarInsurDTO> getList(CarInsurPage paramBody);
	
	Page<CarInsur> getPgList(CarInsurPage paramBody);

	void addInfo(CarInsurBody infoBody);

	void updateInfo(CarInsurBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);
}
