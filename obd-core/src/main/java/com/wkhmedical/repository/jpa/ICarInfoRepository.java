package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.po.CarInfo;

public interface ICarInfoRepository {

	List<CarInfoDTO> findCarInfoList(CarInfoPageParam paramBody, Pageable pageable);

	/**
	 * 自定义返回Page<bean>参考示例
	 * 
	 * @param paramBody
	 * @param pageable
	 * @return
	 */
	Page<CarInfoDTO> findPageCarInfoDTO(CarInfoPageParam paramBody, Pageable pageable);

	CarInfoDTO findCarInfo(CarInfoParam paramBody);

	Page<CarInfo> findPgCarInfo(CarInfoPageParam paramBody, Pageable pageable);

	Integer findCarCount(Integer areaId);

	Long findCountSum();

	void updateCarInfoBindUser(String bindUserId, Integer utype, Integer areaId);

	void updateCarInfoBindUserNull(String bindUserId, Integer utype);
}
