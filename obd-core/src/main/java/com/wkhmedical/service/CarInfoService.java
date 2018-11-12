package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarInfoAddBody;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoEditBody;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.po.CarInfo;

public interface CarInfoService {
	Page<CarInfo> getCarInfoPage(Paging<CarInfoPageParam> paramBody);

	List<CarInfoDTO> getCarInfoList(Paging<CarInfoPageParam> paramBody);

	CarInfo getInfo(CarInfoParam paramBody);

	CarInfo getCarInfo(CarInfoParam paramBody);

	CarInfoDTO getCarInfo(String eid);

	void addCarInfo(CarInfoAddBody carInfoBody);

	void updateCarInfo(CarInfoEditBody carInfoBody);

	void deleteCarInfo(String eid);

	void delInfo(String id);

	Long getCountSum();

	List<ChartCarDTO> getChartCarList(Integer groupType);

}
