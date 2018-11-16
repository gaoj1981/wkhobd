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
import com.wkhmedical.dto.CarInsurCopyBody;
import com.wkhmedical.dto.CarInsurCopyDTO;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.SearchSqlParam;
import com.wkhmedical.po.CarInsurCopy;
import com.wkhmedical.repository.jpa.CarInsurCopyRepository;
import com.wkhmedical.repository.jpa.ICarInsurCopyRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarInsurCopyRepositoryImpl implements ICarInsurCopyRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	CarInsurCopyRepository carInsurCopyRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<CarInsurCopyDTO> findCarInsurCopyList(CarInsurCopyBody paramBody, Pageable pageable) {
		// 获取组装后的搜索条件SQL对象
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		//
		return hibernateSupport.findByNativeSql(CarInsurCopyDTO.class, sqlParam.getSql(), sqlParam.getParamList().toArray(), page * size, size);
	}

	@Override
	public Page<CarInsurCopyDTO> findPgCarInsurCopyDTO(CarInsurCopyBody paramBody, Pageable pageable) {
		List<CarInsurCopyDTO> lstRes = findCarInsurCopyList(paramBody, pageable);
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		String countSql = sqlParam.getCountSql();
		long total = hibernateSupport.countByNativeSql(countSql, sqlParam.getParamList().toArray());
		PageImpl<CarInsurCopyDTO> pageResult = new PageImpl<CarInsurCopyDTO>(lstRes, pageable, total);
		//
		return pageResult;
	}

	@Override
	public Page<CarInsurCopy> findPgCarInsurCopy(CarInsurCopyBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM car_insur_copy";
		String sqlCount = "SELECT COUNT(1) FROM car_insur_copy";
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
		return carInsurCopyRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public CarInsurCopy findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_insur_copy");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<CarInsurCopy> lstCarInsurCopy = hibernateSupport.findByNativeSql(CarInsurCopy.class, sqlBuf.toString(), paramList.toArray());
		if (lstCarInsurCopy != null && lstCarInsurCopy.size() > 0) {
			return lstCarInsurCopy.get(0);
		}
		return null;
	}

	@Override
	public Integer findCount(String id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

	private static SearchSqlParam getDTOSql(CarInsurCopyBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM car_insur_copy");
		sqlBuf.append(" WHERE delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND id = ?");

		//
		String countSql = sqlBuf.toString();
		countSql = "SELECT COUNT(1) " + countSql.substring(countSql.indexOf("FROM"));
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		sqlBuf.append(sqlOrder);
		// 返回组装后的查询SQL
		SearchSqlParam sqlParam = new SearchSqlParam();
		sqlParam.setSql(sqlBuf.toString());
		sqlParam.setParamList(paramList);
		sqlParam.setCountSql(countSql);
		return sqlParam;
	}

	@Override
	public Page<CarInsurDTO> findByExpDay(String expDateMin, String expDateMax, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append("SELECT cmc.*,ci.eid,ci.areaId,ci.provId,ci.cityId");
		sqlBuf.append(" FROM car_insur_copy cmc");
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
		List<CarInsurDTO> lstRes = hibernateSupport.findByNativeSql(CarInsurDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
		long total = hibernateSupport.countByNativeSql(countSql, paramList.toArray());
		PageImpl<CarInsurDTO> pageResult = new PageImpl<CarInsurDTO>(lstRes, pageable, total);

		return pageResult;
	}

}
