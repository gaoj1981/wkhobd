package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;

public interface CarInfoService {
	Page<CarInfo> getCarInfoPage(CarInfoPage paramBody);

	List<CarInfoDTO> getCarInfoList(CarInfoPage paramBody);

	CarInfoDTO getCarInfo(CarInfoParam paramBody);

	void addCarInfo(CarInfoBody carInfoBody);

	void updateCarInfo(CarInfoBody carInfoBody);
	
	void deleteCarInfo(Long id);
}
