package com.wkhmedical.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoListBody;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.service.CarInfoService;

@Service
public class CarInfoServiceImpl implements CarInfoService {

	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public List<CarInfoDTO> getCarInfoList(CarInfoListBody paramBody) {
		return null;
	}

}
