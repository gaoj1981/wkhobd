package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.DeviceTimeBody;
import com.wkhmedical.dto.DeviceTimeDTO;
import com.wkhmedical.repository.jpa.DeviceTimeRepository;
import com.wkhmedical.repository.jpa.IDeviceTimeRepository;
import com.wkhmedical.util.BizUtil;

public class DeviceTimeRepositoryImpl implements IDeviceTimeRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	DeviceTimeRepository deviceTimeRepository;

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.repository.jpa.IDeviceTimeRepository#findCarDayCount(java.util.Date,
	 * java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public Long findCarDayCount(Date dt, String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("SELECT COUNT(1) FROM device_time WHERE dt=?");
		paramList.add(dt);
		if (StringUtils.isNotBlank(eid)) {
			sqlBuf.append(" AND eid=?");
			paramList.add(eid);
		}
		if (provId != null) {
			sqlBuf.append(" AND provId=?");
			paramList.add(provId);
		}
		if (cityId != null) {
			sqlBuf.append(" AND cityId=?");
			paramList.add(cityId);
		}
		if (areaId != null) {
			sqlBuf.append(" AND areaId=?");
			paramList.add(areaId);
		}
		if (townId != null) {
			sqlBuf.append(" AND townId=?");
			paramList.add(townId);
		}
		if (villId != null) {
			sqlBuf.append(" AND villId=?");
			paramList.add(villId);
		}
		return hibernateSupport.countByNativeSql(sqlBuf.toString(), paramList.toArray());
	}

	@Override
	public Long getTimeSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT SUM(dct.ts) AS sumNum");
		sqlBuf.append(" FROM device_time dct");
		sqlBuf.append(" WHERE 1 = 1");
		if (StringUtils.isNotBlank(eid)) {
			sqlBuf.append(" AND dct.eid = ?");
			paramList.add(eid);
		}
		if (provId != null) {
			sqlBuf.append(" AND dct.provId = ?");
			paramList.add(provId);
		}
		if (cityId != null) {
			sqlBuf.append(" AND dct.cityId = ?");
			paramList.add(cityId);
		}
		if (areaId != null) {
			sqlBuf.append(" AND dct.areaId = ?");
			paramList.add(areaId);
		}
		if (townId != null) {
			sqlBuf.append(" AND dct.townId = ?");
			paramList.add(townId);
		}
		if (villId != null) {
			sqlBuf.append(" AND dct.villId = ?");
			paramList.add(villId);
		}
		// 处理日期查询
		if (dtStart != null) {
			sqlBuf.append(" AND dct.dt >= ?");
			paramList.add(dtStart);
		}
		if (dtEnd != null) {
			sqlBuf.append(" AND dct.dt <= ?");
			paramList.add(dtEnd);
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, sqlBuf.toString(), paramList.toArray());
		BigDecimal resNum = (BigDecimal) lstCount.get(0).get("sumNum");
		if (resNum == null) return 0L;
		return resNum.longValue();
	}

	@Override
	public BigDecimal getDisSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT SUM(dct.dis) AS sumNum");
		sqlBuf.append(" FROM device_time dct");
		sqlBuf.append(" WHERE 1 = 1");
		if (StringUtils.isNotBlank(eid)) {
			sqlBuf.append(" AND dct.eid = ?");
			paramList.add(eid);
		}
		if (provId != null) {
			sqlBuf.append(" AND dct.provId = ?");
			paramList.add(provId);
		}
		if (cityId != null) {
			sqlBuf.append(" AND dct.cityId = ?");
			paramList.add(cityId);
		}
		if (areaId != null) {
			sqlBuf.append(" AND dct.areaId = ?");
			paramList.add(areaId);
		}
		if (townId != null) {
			sqlBuf.append(" AND dct.townId = ?");
			paramList.add(townId);
		}
		if (villId != null) {
			sqlBuf.append(" AND dct.villId = ?");
			paramList.add(villId);
		}
		// 处理日期查询
		if (dtStart != null) {
			sqlBuf.append(" AND dct.dt >= ?");
			paramList.add(dtStart);
		}
		if (dtEnd != null) {
			sqlBuf.append(" AND dct.dt <= ?");
			paramList.add(dtEnd);
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, sqlBuf.toString(), paramList.toArray());
		BigDecimal resNum = (BigDecimal) lstCount.get(0).get("sumNum");
		if (resNum == null) return BigDecimal.ZERO;
		return resNum;
	}

	@Override
	public Long getCarSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, Date dtStart, Date dtEnd) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT COUNT(DISTINCT(eid)) AS sumNum");
		sqlBuf.append(" FROM device_time dct");
		sqlBuf.append(" WHERE 1 = 1");
		if (StringUtils.isNotBlank(eid)) {
			sqlBuf.append(" AND dct.eid = ?");
			paramList.add(eid);
		}
		if (provId != null) {
			sqlBuf.append(" AND dct.provId = ?");
			paramList.add(provId);
		}
		if (cityId != null) {
			sqlBuf.append(" AND dct.cityId = ?");
			paramList.add(cityId);
		}
		if (areaId != null) {
			sqlBuf.append(" AND dct.areaId = ?");
			paramList.add(areaId);
		}
		if (townId != null) {
			sqlBuf.append(" AND dct.townId = ?");
			paramList.add(townId);
		}
		if (villId != null) {
			sqlBuf.append(" AND dct.villId = ?");
			paramList.add(villId);
		}
		// 处理日期查询
		if (dtStart != null) {
			sqlBuf.append(" AND dct.dt >= ?");
			paramList.add(dtStart);
		}
		if (dtEnd != null) {
			sqlBuf.append(" AND dct.dt <= ?");
			paramList.add(dtEnd);
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, sqlBuf.toString(), paramList.toArray());
		BigInteger resNum = (BigInteger) lstCount.get(0).get("sumNum");
		if (resNum == null) return 0L;
		return resNum.longValue();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.wkhmedical.repository.jpa.IDeviceTimeRepository#findPgDeviceTimeDTO(com.wkhmedical.dto.
	 * DeviceTimeBody, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<DeviceTimeDTO> findPgDeviceTimeDTO(DeviceTimeBody paramBody, Pageable pageable) {
		//
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT d.eid,");
		sqlBuf.append(" MIN(c.plateNum) AS plateNum,");
		sqlBuf.append(" MIN(d.provId) AS provId,");
		sqlBuf.append(" MIN(d.cityId) AS cityId,");
		sqlBuf.append(" MIN(d.areaId) AS areaId,");
		sqlBuf.append(" MIN(d.townId) AS townId,");
		sqlBuf.append(" MIN(d.villId) AS villId,");
		sqlBuf.append(" SUM(d.dis) AS dis,");
		sqlBuf.append(" SUM(d.pts) AS pts,");
		sqlBuf.append(" SUM(d.cks) AS cks,");
		sqlBuf.append(" ceil(SUM(d.exprt) / COUNT(1)) AS exprt,");
		sqlBuf.append(" SUM(d.rps) AS rps,");
		sqlBuf.append(" COUNT(1) AS wds,");
		sqlBuf.append(" SUM(d.ts) AS ts");
		sqlBuf.append(" FROM device_time d");
		sqlBuf.append(" LEFT JOIN car_info c ON d.eid = c.eid");
		sqlBuf.append(" WHERE 1=1 AND c.delFlag=0");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND d.eid = ?");
		BizUtil.setSqlJoin(paramBody, "plateNum", sqlBuf, paramList, " AND c.plateNum = ?");
		BizUtil.setSqlJoin(paramBody, "provId", sqlBuf, paramList, " AND d.provId = ?");
		BizUtil.setSqlJoin(paramBody, "cityId", sqlBuf, paramList, " AND d.cityId = ?");
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND d.areaId = ?");
		BizUtil.setSqlJoin(paramBody, "townId", sqlBuf, paramList, " AND d.townId = ?");
		BizUtil.setSqlJoin(paramBody, "villId", sqlBuf, paramList, " AND d.villId = ?");
		BizUtil.setSqlJoin(paramBody, "sdt", sqlBuf, paramList, " AND d.dt >= ?");
		BizUtil.setSqlJoin(paramBody, "edt", sqlBuf, paramList, " AND d.dt <= ?");
		sqlBuf.append(" GROUP BY d.eid");
		//
		String orderByStr = " ORDER BY ";
		Sort sort = pageable.getSort();
		Iterator<Order> iter = sort.iterator();
		Order order;
		while (iter.hasNext()) {
			order = iter.next();
			orderByStr = orderByStr + order.getProperty() + " " + order.getDirection().name() + ",";
		}
		orderByStr += " d.eid DESC";
		sqlBuf.append(orderByStr);
		//
		String sql = sqlBuf.toString();
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		List<DeviceTimeDTO> lstRes = hibernateSupport.findByNativeSql(DeviceTimeDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
		//
		String countSql = sql.substring(sql.indexOf("FROM"));
		countSql = countSql.substring(0, countSql.indexOf("ORDER BY"));
		countSql = "SELECT COUNT(1)" + countSql;
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, countSql, paramList.toArray());
		long total = 0L;
		if (lstCount == null) {
			total = 0L;
		}
		else {
			total = lstCount.size();
		}
		PageImpl<DeviceTimeDTO> pageResult = new PageImpl<DeviceTimeDTO>(lstRes, pageable, total);
		//
		return pageResult;
	}

}
