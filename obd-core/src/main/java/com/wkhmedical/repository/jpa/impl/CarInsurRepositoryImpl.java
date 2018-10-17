package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurDTO;
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
	public List<CarInsurDTO> findCarInsurList(CarInsurBody paramBody, Pageable pageable) {
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
			Date dtNow = new Date();
			if (valiType.intValue() == 0) {
				sqlBuf.append(" AND ci.effectDate > ?");
				paramList.add(dtNow);
			}
			else if (valiType.intValue() == 1) {
				sqlBuf.append(" AND ci.effectDate < ? AND ci.expDate > ?");
				paramList.add(dtNow);
				paramList.add(dtNow);
			}
			else if (valiType.intValue() == 2) {
				sqlBuf.append(" AND ci.expDate < ?");
				paramList.add(dtNow);
			}
		}
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY ci.insTime DESC");
		sqlBuf.append(sqlOrder);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		return hibernateSupport.findByNativeSql(CarInsurDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
	}

	@Override
	public CarInsur findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_insur");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<CarInsur> lstCarInsur = hibernateSupport.findByNativeSql(CarInsur.class, sqlBuf.toString(), paramList.toArray());
		if (lstCarInsur != null) {
			return lstCarInsur.get(0);
		}
		return null;
	}

	@Override
	public Page<CarInsur> findPgCarInsur(CarInsurBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM car_insur";
		String sqlCount = "SELECT COUNT(1) FROM car_insur";
		// 组装where语句
		List<Object> objList = new ArrayList<Object>();
		StringBuffer sqlWhere = new StringBuffer(" WHERE delFlag = 0");
		if (paramBody != null) {
			List<String> sqlStrList = new ArrayList<String>();
			sqlStrList.add(" AND id = ?");
			BizUtil.setSqlWhere(paramBody, "id", sqlWhere, objList, sqlStrList);
		}
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY id DESC");
		return carInsurRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public Integer findCount(String id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

}
