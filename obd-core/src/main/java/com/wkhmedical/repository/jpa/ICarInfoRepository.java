package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;

public interface ICarInfoRepository {

	List<CarInfoDTO> findCarInfoList(CarInfoPage paramBody);

	CarInfoDTO findCarInfo(CarInfoParam paramBody);

	Page<CarInfo> findPgCarInfo(CarInfoPage paramBody);

	Integer findCarCount(Integer areaId);

	void updateCarInfoBindUser(Long bindUserId, Integer utype, Integer areaId);

	void updateCarInfoBindUserNull(Long bindUserId, Integer utype);
}
