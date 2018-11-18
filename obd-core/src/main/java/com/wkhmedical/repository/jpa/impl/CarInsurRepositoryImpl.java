package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.SearchSqlParam;
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
		// 获取组装后的搜索条件SQL对象
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		return hibernateSupport.findByNativeSql(CarInsurDTO.class, sqlParam.getSql(), sqlParam.getParamList().toArray(), page * size, size);
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
	public Page<CarInsurDTO> findPgCarInsurDTO(CarInsurBody paramBody, Pageable pageable) {
		List<CarInsurDTO> lstRes = findCarInsurList(paramBody, pageable);
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		String countSql = sqlParam.getCountSql();
		long total = hibernateSupport.countByNativeSql(countSql, sqlParam.getParamList().toArray());
		PageImpl<CarInsurDTO> pageResult = new PageImpl<CarInsurDTO>(lstRes, pageable, total);
		//
		return pageResult;
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
		if (lstCarInsur != null && lstCarInsur.size() > 0) {
			return lstCarInsur.get(0);
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

	private static SearchSqlParam getDTOSql(CarInsurBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT cm.*,ci.eid,ci.areaId,ci.provId,ci.cityId");
		sqlBuf.append(" FROM car_insur cm");
		sqlBuf.append(" LEFT JOIN car_info ci ON ci.id=cm.cid");
		sqlBuf.append(" WHERE cm.delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND cm.id = ?");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND ci.eid = ?");
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND ci.areaId = ?");
		BizUtil.setSqlJoin(paramBody, "eidLike", sqlBuf, paramList, " AND ci.eid LIKE ?");
		BizUtil.setSqlJoin(paramBody, "insurNumLike", sqlBuf, paramList, " AND cm.insurNum LIKE ?");
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
		// 日期查询
		Integer timeSel = paramBody.getTimeSel();
		if (timeSel != null) {
			String dateName = "insTime";
			if (timeSel.intValue() == 1) {
				dateName = "insTime";
			}
			else if (timeSel.intValue() == 2) {
				dateName = "updTime";
			}
			else if (timeSel.intValue() == 3) {
				dateName = "effectDate";
			}
			else if (timeSel.intValue() == 4) {
				dateName = "expDate";
			}
			sqlBuf.append(" AND " + dateName + ">=? AND " + dateName + "<=?");
			paramList.add(paramBody.getTimeStart());
			paramList.add(paramBody.getTimeEnd() + " 23:59:59");
		}

		String countSql = sqlBuf.toString();
		countSql = "SELECT COUNT(1) " + countSql.substring(countSql.indexOf("FROM"));
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY cm.insTime DESC");
		sqlBuf.append(sqlOrder);
		// 返回组装后的查询SQL
		SearchSqlParam sqlParam = new SearchSqlParam();
		sqlParam.setSql(sqlBuf.toString());
		sqlParam.setParamList(paramList);
		sqlParam.setCountSql(countSql);
		return sqlParam;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Date findMaxExpDate(String cid) {
		List<Map> lstMaxExpDate = hibernateSupport.findByNativeSql(Map.class,
				"SELECT MAX(expDate) AS maxDate FROM car_insur WHERE delFlag = 0 AND cid=?", new String[] { cid }, 1);
		if (lstMaxExpDate != null) {
			Map<String, Object> rtnMap = lstMaxExpDate.get(0);
			return (Date) rtnMap.get("maxDate");
		}
		return null;
	}

	@Override
	public Page<CarInsurDTO> findByExpDay(Integer expDayFlag) {
		int expDayVal = expDayFlag.intValue();
		String sql = null;
		if (expDayFlag == 4) {
			System.out.println(sql + expDayVal);
		}
		return null;
	}

}
