package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.po.CarInfo;

public interface ICarInfoRepository {
	List<CarInfoDTO> findCarInfoList(CarInfoPage paramBody);

	Page<CarInfo> findPgCarInfo(CarInfoPage paramBody);

	Integer findCarCount(Integer areaId);
}
