package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.ICarInfoRepository;

public class CarInfoRepositoryImpl implements ICarInfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public List<CarInfoDTO> findCarInfoList(CarInfoPageParam paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("SELECT ci.* FROM car_info ci WHERE ci.provId = ? ");
		paramList.add(paramBody.getProvId());

		//
		String orderByStr = " ORDER BY h.id ASC";
		sqlBuf.append(orderByStr);
		//
		int curPg = paramBody.getPaging();
		int skip = (curPg - 1) * BizConstant.FIND_PAGE_NUM;
		return hibernateSupport.findByNativeSql(CarInfoDTO.class, sqlBuf.toString(), paramList.toArray(), skip, BizConstant.FIND_PAGE_NUM);

	}

	public Page<CarInfo> findPgCarInfo(CarInfoPageParam paramBody) {
		int page = paramBody.getPaging();
		page = page - 1;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, BizConstant.FIND_PAGE_NUM);
		String[] objArr = new String[0];

		return carInfoRepository.findPageByNativeSql("SELECT ci.* FROM car_info ci", "SELECT COUNT(1) FROM car_info ci", objArr, pageable);
	}

}
