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
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurPage;
import com.wkhmedical.po.CarInsur;
import com.wkhmedical.repository.jpa.CarInsurRepository;
import com.wkhmedical.repository.jpa.ICarInsurRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarInsurRepositoryImpl implements ICarInsurRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarInsurRepository carInsurRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<CarInsurDTO> findCarInsurList(CarInsurPage paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT ci.*");
		sqlBuf.append(" FROM car_insur ci");
		sqlBuf.append(" LEFT JOIN car_info car ON car.id=ci.cid");
		sqlBuf.append(" WHERE ci.delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND ci.id = ?");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND car.eid = ?");
		BizUtil.setSqlJoin(paramBody, "insurType", sqlBuf, paramList, " AND ci.insurType = ?");
		BizUtil.setSqlJoin(paramBody, "insurNum", sqlBuf, paramList, " AND ci.insurNum = ?");
		Integer valiType = (Integer) BizUtil.getFieldValueByName("valiType", paramBody);
		if (valiType != null) {
			if (valiType.intValue() == 1) {

			}
		}
		//
		String orderByStr = " ORDER BY insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int curPg = paramBody.getPaging();
		int skip = (curPg - 1) * BizConstant.FIND_PAGE_NUM;
		return hibernateSupport.findByNativeSql(CarInsurDTO.class, sqlBuf.toString(), paramList.toArray(), skip,
				BizConstant.FIND_PAGE_NUM);
	}

	@Override
	public CarInsur findByKey(Long id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_insur");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<CarInsur> lstCarInsur = hibernateSupport.findByNativeSql(CarInsur.class, sqlBuf.toString(),
				paramList.toArray());
		if (lstCarInsur != null) {
			return lstCarInsur.get(0);
		}
		return null;
	}

	@Override
	public Page<CarInsur> findPgCarInsur(CarInsurPage paramBody) {
		int page = paramBody.getPaging();
		page = page - 1;
		if (page < 0)
			page = 0;
		Pageable pageable = PageRequest.of(page, BizConstant.FIND_PAGE_NUM);
		String[] objArr = new String[0];

		return carInsurRepository.findPageByNativeSql("SELECT * FROM car_insur", "SELECT COUNT(1) FROM car_insur",
				objArr, pageable);
	}

	@Override
	public Integer findCount(Long id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

}
