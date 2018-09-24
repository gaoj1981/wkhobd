package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.po.CarInfo;

public interface ICarInfoRepository {
	List<CarInfoDTO> findCarInfoList(CarInfoPageParam paramBody);

	Page<CarInfo> findPgCarInfo(CarInfoPageParam paramBody);
}
