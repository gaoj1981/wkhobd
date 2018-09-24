package com.wkhmedical.service;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;

public interface CarInfoService {
	Page<CarInfo> getCarInfoList(CarInfoPageParam paramBody);
	
	CarInfoDTO getCarInfo(CarInfoParam paramBody);
}
