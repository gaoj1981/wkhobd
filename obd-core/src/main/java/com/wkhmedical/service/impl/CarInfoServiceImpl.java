package com.wkhmedical.service.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.util.BizUtil;

@Service
public class CarInfoServiceImpl implements CarInfoService {

	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public Page<CarInfo> getCarInfoList(CarInfoPageParam paramBody) {
		Page<CarInfo> pgCarInfo = carInfoRepository.findPgCarInfo(paramBody);
		return pgCarInfo;
	}

	@Override
	public CarInfoDTO getCarInfo(CarInfoParam paramBody) {
		String eid = paramBody.getEid();
		CarInfo carInfo = carInfoRepository.findByEid(eid);
//		return BizUtil.coverBean(carInfo, CarInfoDTO.class);
		return null;
	}

}
