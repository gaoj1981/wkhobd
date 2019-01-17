package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;

public interface ObdCarService {

	void saveObdCar(CarSendDTO carInfo);

	ObdCarDTO getObdCar(String eid);

	Page<ObdCarDTO> getObdCarPage(String eid);

	List<ObdCarDTO> getObdCarList(String eid);
}
