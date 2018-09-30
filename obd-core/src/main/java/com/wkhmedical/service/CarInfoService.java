package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.po.CarInfo;

public interface CarInfoService {
	Page<CarInfo> getCarInfoPage(CarInfoPage paramBody);

	List<CarInfoDTO> getCarInfoList(CarInfoPage paramBody);

	CarInfoDTO getCarInfo(String eid);

	void addCarInfo(CarInfoAddBody carInfoBody);

	void updateCarInfo(CarInfoEditBody carInfoBody);

	void deleteCarInfo(String eid);
}
