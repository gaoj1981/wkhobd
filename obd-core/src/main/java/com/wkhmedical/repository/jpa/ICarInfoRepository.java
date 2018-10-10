package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;

public interface ICarInfoRepository {

	List<CarInfoDTO> findCarInfoList(CarInfoPageParam paramBody, Integer page, Integer size);

	CarInfoDTO findCarInfo(CarInfoParam paramBody);

	Page<CarInfo> findPgCarInfo(CarInfoPageParam paramBody, Integer page, Integer size);

	Integer findCarCount(Integer areaId);

	Long findCountSum();

	void updateCarInfoBindUser(Long bindUserId, Integer utype, Integer areaId);

	void updateCarInfoBindUserNull(Long bindUserId, Integer utype);
}
