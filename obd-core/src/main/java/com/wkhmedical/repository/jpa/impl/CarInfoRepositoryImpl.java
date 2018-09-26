package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPage;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.ICarInfoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarInfoRepositoryImpl implements ICarInfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;
	@Resource
	CarInfoRepository carInfoRepository;

	@Value("#{query.findCarCount}")
	private String findCarCount;

	@Override
	public List<CarInfoDTO> findCarInfoList(CarInfoPage paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT ci.*,");
		sqlBuf.append(" bu1.uname AS prinName,bu1.job AS prinJob,bu1.tel AS prinTel,");
		sqlBuf.append(" bu1.urName AS prinUrName,bu1.urTel AS prinUrTel,");
		sqlBuf.append(" bu2.uname AS maintName,bu2.tel AS maintTel,bu2.urName AS maintUrName,bu2.urTel AS maintUrTel");
		sqlBuf.append(" FROM car_info ci");
		sqlBuf.append(" LEFT JOIN bind_user bu1 ON ci.prinId = bu1.id");
		sqlBuf.append(" LEFT JOIN bind_user bu2 ON ci.maintId = bu2.id");
		sqlBuf.append(" WHERE ci.areaId = ?");
		paramList.add(paramBody.getAreaId());
		//
		String orderByStr = " ORDER BY ci.insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int curPg = paramBody.getPaging();
		int skip = (curPg - 1) * BizConstant.FIND_PAGE_NUM;
		return hibernateSupport.findByNativeSql(CarInfoDTO.class, sqlBuf.toString(), paramList.toArray(), skip,
				BizConstant.FIND_PAGE_NUM);

	}

	public Page<CarInfo> findPgCarInfo(CarInfoPage paramBody) {
		int page = paramBody.getPaging();
		page = page - 1;
		if (page < 0)
			page = 0;
		Pageable pageable = PageRequest.of(page, BizConstant.FIND_PAGE_NUM);
		String[] objArr = new String[0];

		return carInfoRepository.findPageByNativeSql("SELECT ci.* FROM car_info ci", "SELECT COUNT(1) FROM car_info ci",
				objArr, pageable);
	}

	@Override
	public Integer findCarCount(Integer areaId) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCarCount, Map.class, areaId);
		log.info("jdbcQuery测试：车辆数为" + count.get(0));
		return 0;
	}

}
