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
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.dto.SearchSqlParam;
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
	public List<CarMotDTO> findCarMotList(CarMotBody paramBody, Pageable pageable) {
		// 获取组装后的搜索条件SQL对象
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		return hibernateSupport.findByNativeSql(CarMotDTO.class, sqlParam.getSql(), sqlParam.getParamList().toArray(), page * size, size);
	}

	@Override
	public Page<CarMot> findPgCarMot(CarMotBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM car_mot";
		String sqlCount = "SELECT COUNT(1) FROM car_mot";
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
		return carMotRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public Page<CarMotDTO> findPgCarMotDTO(CarMotBody paramBody, Pageable pageable) {
		List<CarMotDTO> lstRes = findCarMotList(paramBody, pageable);
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		String countSql = sqlParam.getCountSql();
		long total = hibernateSupport.countByNativeSql(countSql, sqlParam.getParamList().toArray());
		PageImpl<CarMotDTO> pageResult = new PageImpl<CarMotDTO>(lstRes, pageable, total);
		//
		return pageResult;
	}

	@Override
	public CarMot findByKey(String id) {
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
	public Integer findCount(String id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

	private static SearchSqlParam getDTOSql(CarMotBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT cm.*,ci.eid,ci.areaId,ci.provId,ci.cityId");
		sqlBuf.append(" FROM car_mot cm");
		sqlBuf.append(" LEFT JOIN car_info ci ON ci.id=cm.cid");
		sqlBuf.append(" WHERE cm.delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND cm.id = ?");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND ci.eid = ?");
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND ci.areaId = ?");
		BizUtil.setSqlJoin(paramBody, "eidLike", sqlBuf, paramList, " AND ci.eid LIKE ?");
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
				dateName = "expDate";
			}
			else if (timeSel.intValue() == 4) {
				dateName = "motDate";
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
		List<Map> lstMaxExpDate = hibernateSupport.findByNativeSql(Map.class, "SELECT MAX(expDate) AS maxDate FROM car_mot WHERE cid=?",
				new String[] { cid }, 1);
		if (lstMaxExpDate != null) {
			Map<String, Object> rtnMap = lstMaxExpDate.get(0);
			return (Date) rtnMap.get("maxDate");
		}
		return null;
	}

	@Override
	public Page<CarMotDTO> findByExpDay(Integer expDayFlag) {
		int expDayVal = expDayFlag.intValue();
		String sql = null;
		if (expDayFlag == 4){
			System.out.println(sql+expDayVal);
		}
		return null;
	}

}
