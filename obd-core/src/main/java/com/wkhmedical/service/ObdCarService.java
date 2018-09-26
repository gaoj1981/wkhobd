package com.wkhmedical.service;

import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;

public interface ObdCarService {

	void saveObdCar(CarSendDTO carInfo);

	ObdCarDTO getObdCar(String eid);
}
