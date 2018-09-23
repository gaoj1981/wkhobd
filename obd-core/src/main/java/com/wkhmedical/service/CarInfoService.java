package com.wkhmedical.service;

import java.util.List;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoListBody;

public interface CarInfoService {
	List<CarInfoDTO> getCarInfoList(CarInfoListBody paramBody);
}
