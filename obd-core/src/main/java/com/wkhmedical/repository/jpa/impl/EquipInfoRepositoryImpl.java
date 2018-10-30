package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
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
import com.wkhmedical.dto.SearchSqlParam;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.po.EquipInfo;
import com.wkhmedical.repository.jpa.EquipInfoRepository;
import com.wkhmedical.repository.jpa.IEquipInfoRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EquipInfoRepositoryImpl implements IEquipInfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	EquipInfoRepository equipInfoRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<EquipInfoDTO> findEquipInfoList(EquipInfoBody paramBody, Pageable pageable) {
		// 获取组装后的搜索条件SQL对象
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		//
		return hibernateSupport.findByNativeSql(EquipInfoDTO.class, sqlParam.getSql(), sqlParam.getParamList().toArray(), page * size, size);
	}

	@Override
	public Page<EquipInfoDTO> findPgEquipInfoDTO(EquipInfoBody paramBody, Pageable pageable) {
		List<EquipInfoDTO> lstRes = findEquipInfoList(paramBody, pageable);
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		String countSql = sqlParam.getCountSql();
		long total = hibernateSupport.countByNativeSql(countSql, sqlParam.getParamList().toArray());
		PageImpl<EquipInfoDTO> pageResult = new PageImpl<EquipInfoDTO>(lstRes, pageable, total);
		//
		return pageResult;
	}

	@Override
	public Page<EquipInfo> findPgEquipInfo(EquipInfoBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM equip_info";
		String sqlCount = "SELECT COUNT(1) FROM equip_info";
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
		return equipInfoRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere,
				objList.toArray(), pageable);
	}

	@Override
	public EquipInfo findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM equip_info");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<EquipInfo> lstEquipInfo = hibernateSupport.findByNativeSql(EquipInfo.class, sqlBuf.toString(),
				paramList.toArray());
		if (lstEquipInfo != null && lstEquipInfo.size() > 0) {
			return lstEquipInfo.get(0);
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

	private static SearchSqlParam getDTOSql(EquipInfoBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM equip_info");
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
	
}
