package com.wkhmedical.repository.jpa.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.repository.jpa.IDeviceCheckTimeRepository;

public class DeviceCheckTimeRepositoryImpl implements IDeviceCheckTimeRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Override
	public Long getCheckSum(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId, String type, Date dtStart, Date dtEnd) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT SUM(dct.number) AS sumNum");
		sqlBuf.append(" FROM device_check_time dct");
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
		// 处理体检项查询
		if (StringUtils.isNotBlank(type)) {
			sqlBuf.append(" AND dct.type = ?");
			paramList.add(type);
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

	/*
	 * (non-Javadoc)
	 * @see
	 * com.wkhmedical.repository.jpa.IDeviceCheckTimeRepository#getCheckItemCount(java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long getCheckItemCount(String eid, Long provId, Long cityId, Long areaId, Long townId, Long villId) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuilder sqlBuf = new StringBuilder("");
		sqlBuf.append(" SELECT SUM(dct.number) AS sumNum");
		sqlBuf.append(" FROM device_check_time dct");
		sqlBuf.append(" WHERE 1 = 1 AND status=1");
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
		@SuppressWarnings("rawtypes")
		List<Map> lstCount = hibernateSupport.findByNativeSql(Map.class, sqlBuf.toString(), paramList.toArray());
		BigDecimal resNum = (BigDecimal) lstCount.get(0).get("sumNum");
		if (resNum == null) return 0L;
		return resNum.longValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.repository.jpa.IDeviceCheckTimeRepository#getCheckSumByStatus(java.lang.
	 * Integer, java.lang.String, java.util.Date)
	 */
	@Override
	public BigDecimal getCheckSumByStatus(Integer status, String eid, Date dt) {
		List<Object> paramList = new ArrayList<Object>();
		String sql = "SELECT SUM(number) AS sumNum FROM device_check_time WHERE 1=1";
		if (status != null) {
			sql = sql + " AND status=" + status;
		}
		if (StringUtils.isNotBlank(eid)) {
			sql = sql + " AND eid='" + eid + "'";
		}
		if (dt != null) {
			sql = sql + " AND dt=?";
			paramList.add(dt);
		}
		@SuppressWarnings("rawtypes")
		List<Map> lstObj = hibernateSupport.findByNativeSql(Map.class, sql, paramList.toArray(), 1);
		if (lstObj == null || lstObj.get(0).get("sumNum") == null) {
			return BigDecimal.ZERO;
		}
		return (BigDecimal) lstObj.get(0).get("sumNum");
	}

}
