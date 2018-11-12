package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.CarInfoDTO;
import com.wkhmedical.dto.CarInfoPageParam;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.ICarInfoRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CarInfoRepositoryImpl implements ICarInfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;
	@Resource
	CarInfoRepository carInfoRepository;

	@Value("#{query.findCarCount}")
	private String findCarCount;

	private StringBuffer getSelectSql() {
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT ci.*,");
		sqlBuf.append(" bu1.uname AS prinName,bu1.job AS prinJob,bu1.tel AS prinTel,");
		sqlBuf.append(" bu1.urName AS prinUrName,bu1.urTel AS prinUrTel,");
		sqlBuf.append(" bu2.uname AS maintName,bu2.tel AS maintTel,bu2.urName AS maintUrName,bu2.urTel AS maintUrTel");
		sqlBuf.append(" FROM car_info ci");
		sqlBuf.append(" LEFT JOIN bind_user bu1 ON ci.prinId = bu1.id AND bu1.utype = 1");
		sqlBuf.append(" LEFT JOIN bind_user bu2 ON ci.maintId = bu2.id AND bu2.utype = 2");
		sqlBuf.append(" WHERE ci.delFlag = 0");
		return sqlBuf;
	}

	@Override
	public List<CarInfoDTO> findCarInfoList(CarInfoPageParam paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = getSelectSql();
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND ci.areaId = ?");
		BizUtil.setSqlJoin(paramBody, "provId", sqlBuf, paramList, " AND ci.provId = ?");
		BizUtil.setSqlJoin(paramBody, "cityId", sqlBuf, paramList, " AND ci.cityId = ?");
		BizUtil.setSqlJoin(paramBody, "eidLike", sqlBuf, paramList, " AND ci.eid LIKE ?");
		BizUtil.setSqlJoin(paramBody, "plateNumLike", sqlBuf, paramList, " AND ci.plateNum LIKE ?");
		//
		String orderByStr = " ORDER BY ci.insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		//
		return hibernateSupport.findByNativeSql(CarInfoDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
	}

	@Override
	// 自定义Page<bean>备用
	public Page<CarInfoDTO> findPageCarInfoDTO(CarInfoPageParam paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = getSelectSql();
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND ci.areaId = ?");
		BizUtil.setSqlJoin(paramBody, "eidLike", sqlBuf, paramList, " AND ci.eid LIKE ?");
		//
		String orderByStr = " ORDER BY ci.insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		List<CarInfoDTO> lstRes = hibernateSupport.findByNativeSql(CarInfoDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
		PageImpl<CarInfoDTO> pageResult = new PageImpl<CarInfoDTO>(lstRes, pageable, lstRes.size());
		//
		return pageResult;
	}

	@Override
	public CarInfoDTO findCarInfo(CarInfoParam paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = getSelectSql();
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND ci.eid = ?");
		List<CarInfoDTO> lstCarInfo = hibernateSupport.findByNativeSql(CarInfoDTO.class, sqlBuf.toString(), paramList.toArray(), 1);
		if (lstCarInfo != null && lstCarInfo.size() > 0) {
			return lstCarInfo.get(0);
		}
		return null;
	}

	public Page<CarInfo> findPgCarInfo(CarInfoPageParam paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM car_info";
		String sqlCount = "SELECT COUNT(1) FROM car_info";
		// 组装where语句
		List<Object> objList = new ArrayList<Object>();
		StringBuffer sqlWhere = new StringBuffer(" WHERE delFlag = 0");
		if (paramBody != null) {
			List<String> sqlStrList = new ArrayList<String>();
			sqlStrList.add(" AND areaId = ?");
			sqlStrList.add(" AND eid LIKE ?");
			BizUtil.setSqlWhere(paramBody, "areaId,eidLike", sqlWhere, objList, sqlStrList);
			// 车管人员查询处理
			String buserId = paramBody.getBuserId();
			if (StringUtils.isNotBlank(buserId)) {
				sqlWhere.append(" AND (prinId = ? OR maintId = ?)");
				objList.add(buserId);
				objList.add(buserId);
			}
		}
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		// 执行分页查询
		return carInfoRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public void updateCarInfoBindUser(String bindUserId, Integer utype, Integer areaId) {
		String utypeStr = null;
		if (utype == 1) {
			utypeStr = "prinId";
		}
		else if (utype == 2) {
			utypeStr = "maintId";
		}
		if (utypeStr != null && areaId != null) {
			String sql = "UPDATE car_info SET " + utypeStr + "=" + bindUserId + " WHERE areaId=" + areaId;
			jdbcQuery.getJdbcTemplate().execute(sql);
		}
	}

	@Override
	public void updateCarInfoBindUserNull(String bindUserId, Integer utype) {
		String utypeStr = null;
		if (utype == 1) {
			utypeStr = "prinId";
		}
		else if (utype == 2) {
			utypeStr = "maintId";
		}
		if (utypeStr != null && bindUserId != null) {
			String sql = "UPDATE car_info SET " + utypeStr + "=NULL" + " WHERE " + utypeStr + "=" + bindUserId;
			jdbcQuery.getJdbcTemplate().execute(sql);
		}
	}

	@Override
	public Integer findCarCount(Integer areaId) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCarCount, Map.class, areaId);
		log.info("jdbcQuery测试：车辆数为" + count.get(0));
		return 0;
	}

	@Override
	public Long findCountSum() {
		return hibernateSupport.countByNativeSql("SELECT COUNT(1) FROM car_info WHERE delFlag=0", null);
	}

	@Override
	public List<ChartCarDTO> findCarCountGroupBy(CarInfoParam paramBody) {
		boolean groupByProv = paramBody.getGroupByProv();
		if (groupByProv) {
			// 按省份汇总
			return hibernateSupport.findByNativeSql(ChartCarDTO.class, "SELECT provId,COUNT(1) AS countNum FROM car_info GROUP BY provId", null,
					1000);
		}
		return new ArrayList<ChartCarDTO>();
	}
}
