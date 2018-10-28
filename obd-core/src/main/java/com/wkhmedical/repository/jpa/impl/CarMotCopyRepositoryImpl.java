package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.CarMotCopyBody;
import com.wkhmedical.dto.CarMotCopyDTO;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarMotCopy;
import com.wkhmedical.repository.jpa.CarMotCopyRepository;
import com.wkhmedical.repository.jpa.ICarMotCopyRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarMotCopyRepositoryImpl implements ICarMotCopyRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarMotCopyRepository carMotCopyRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<CarMotCopyDTO> findCarMotCopyList(CarMotCopyBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_mot_copy");
		sqlBuf.append(" WHERE delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND id = ?");
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		sqlBuf.append(sqlOrder);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		//
		return hibernateSupport.findByNativeSql(CarMotCopyDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
	}

	@Override
	public Page<CarMotCopy> findPgCarMotCopy(CarMotCopyBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM car_mot_copy";
		String sqlCount = "SELECT COUNT(1) FROM car_mot_copy";
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
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		//
		return carMotCopyRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public CarMotCopy findByKey(Long id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_mot_copy");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<CarMotCopy> lstCarMotCopy = hibernateSupport.findByNativeSql(CarMotCopy.class, sqlBuf.toString(), paramList.toArray());
		if (lstCarMotCopy != null && lstCarMotCopy.size() > 0) {
			return lstCarMotCopy.get(0);
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

	@Override
	public void deleteByCid(String cid) {
		jdbcQuery.getJdbcTemplate().execute("DELETE FROM car_mot_copy WHERE cid = '" + cid + "'");
	}

	@Override
	public Page<CarMotDTO> findByExpDay(String expDateMin, String expDateMax, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append("SELECT cmc.*,ci.eid,ci.areaId,ci.provId,ci.cityId");
		sqlBuf.append(" FROM car_mot_copy cmc");
		sqlBuf.append(" LEFT JOIN car_info ci ON ci.id=cmc.cid");
		sqlBuf.append(" WHERE cmc.delFlag = 0");
		if (StringUtils.isNotBlank(expDateMax)) {
			sqlBuf.append(" AND cmc.expDate < ?");
			paramList.add(expDateMax);
		}
		if (StringUtils.isNotBlank(expDateMin)) {
			sqlBuf.append(" AND cmc.expDate > ?");
			paramList.add(expDateMin);
		}
		String countSql = sqlBuf.toString();
		countSql = "SELECT COUNT(1) " + countSql.substring(countSql.indexOf("FROM"));
		//
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		List<CarMotDTO> lstRes = hibernateSupport.findByNativeSql(CarMotDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
		long total = hibernateSupport.countByNativeSql(countSql, paramList.toArray());
		PageImpl<CarMotDTO> pageResult = new PageImpl<CarMotDTO>(lstRes, pageable, total);

		return pageResult;
	}

}
