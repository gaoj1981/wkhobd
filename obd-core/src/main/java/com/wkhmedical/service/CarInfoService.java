package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPageSearch;
import com.wkhmedical.po.CarInfo;

public interface CarInfoService {
	Page<CarInfo> getCarInfoPage(CarInfoPageSearch paramBody);

	List<CarInfoDTO> getCarInfoList(CarInfoPageSearch paramBody);

	CarInfoDTO getCarInfo(String eid);

	void addCarInfo(CarInfoAddBody carInfoBody);

	void updateCarInfo(CarInfoEditBody carInfoBody);

	void deleteCarInfo(String eid);

	Long getCountSum();
}
