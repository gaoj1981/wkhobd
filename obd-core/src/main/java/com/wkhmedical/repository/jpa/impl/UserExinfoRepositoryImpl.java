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
import com.wkhmedical.dto.UserExinfoDTO;
import com.wkhmedical.dto.UserExinfoBody;
import com.wkhmedical.po.UserExinfo;
import com.wkhmedical.repository.jpa.UserExinfoRepository;
import com.wkhmedical.repository.jpa.IUserExinfoRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserExinfoRepositoryImpl implements IUserExinfoRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	UserExinfoRepository userExinfoRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<UserExinfoDTO> findUserExinfoList(UserExinfoBody paramBody, Pageable pageable) {
		// 获取组装后的搜索条件SQL对象
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		//
		return hibernateSupport.findByNativeSql(UserExinfoDTO.class, sqlParam.getSql(), sqlParam.getParamList().toArray(), page * size, size);
	}

	@Override
	public Page<UserExinfoDTO> findPgUserExinfoDTO(UserExinfoBody paramBody, Pageable pageable) {
		List<UserExinfoDTO> lstRes = findUserExinfoList(paramBody, pageable);
		SearchSqlParam sqlParam = getDTOSql(paramBody, pageable);
		String countSql = sqlParam.getCountSql();
		long total = hibernateSupport.countByNativeSql(countSql, sqlParam.getParamList().toArray());
		PageImpl<UserExinfoDTO> pageResult = new PageImpl<UserExinfoDTO>(lstRes, pageable, total);
		//
		return pageResult;
	}

	@Override
	public Page<UserExinfo> findPgUserExinfo(UserExinfoBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM user_exinfo";
		String sqlCount = "SELECT COUNT(1) FROM user_exinfo";
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
		return userExinfoRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere,
				objList.toArray(), pageable);
	}

	@Override
	public UserExinfo findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM user_exinfo");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<UserExinfo> lstUserExinfo = hibernateSupport.findByNativeSql(UserExinfo.class, sqlBuf.toString(),
				paramList.toArray());
		if (lstUserExinfo != null && lstUserExinfo.size() > 0) {
			return lstUserExinfo.get(0);
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

	private static SearchSqlParam getDTOSql(UserExinfoBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM user_exinfo");
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
