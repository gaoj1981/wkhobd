package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.Date;
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
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.po.CarMot;
import com.wkhmedical.repository.jpa.CarMotRepository;
import com.wkhmedical.repository.jpa.ICarMotRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarMotRepositoryImpl implements ICarMotRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarMotRepository carMotRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<CarMotDTO> findCarMotList(CarMotBody paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT cm.*,ci.eid");
		sqlBuf.append(" FROM car_mot cm");
		sqlBuf.append(" LEFT JOIN car_info ci ON ci.id=cm.cid");
		sqlBuf.append(" WHERE cm.delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND cm.id = ?");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND ci.eid = ?");
		Integer valiType = (Integer) BizUtil.getFieldValueByName("valiType", paramBody);
		if (valiType != null) {
			Date dtNow = new Date();
			if (valiType.intValue() == 1) {
				sqlBuf.append(" AND cm.expDate > ?");
				paramList.add(dtNow);
			}
			else if (valiType.intValue() == 2) {
				sqlBuf.append(" AND cm.expDate < ?");
				paramList.add(dtNow);
			}
		}
		//
		String orderByStr = " ORDER BY insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int curPg = paramBody.getPaging();
		int skip = (curPg - 1) * BizConstant.FIND_PAGE_NUM;
		return hibernateSupport.findByNativeSql(CarMotDTO.class, sqlBuf.toString(), paramList.toArray(), skip, BizConstant.FIND_PAGE_NUM);
	}

	@Override
	public Page<CarMot> findPgCarMot(CarMotBody paramBody) {
		int page = paramBody.getPaging();
		page = page - 1;
		if (page < 0) page = 0;
		Pageable pageable = PageRequest.of(page, BizConstant.FIND_PAGE_NUM);
		String[] objArr = new String[0];

		return carMotRepository.findPageByNativeSql("SELECT * FROM car_mot", "SELECT COUNT(1) FROM car_mot", objArr, pageable);
	}

	@Override
	public CarMot findByKey(Long id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_mot");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<CarMot> lstCarMot = hibernateSupport.findByNativeSql(CarMot.class, sqlBuf.toString(), paramList.toArray());
		if (lstCarMot != null && lstCarMot.size() > 0) {
			return lstCarMot.get(0);
		}
		return null;
	}

	@Override
	public Integer findCount(Long id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

}
