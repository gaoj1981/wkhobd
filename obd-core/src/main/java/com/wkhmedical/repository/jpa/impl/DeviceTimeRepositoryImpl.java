package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.IDeviceTimeRepository;

public class DeviceTimeRepositoryImpl implements IDeviceTimeRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

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

}
