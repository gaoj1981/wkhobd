package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.repository.jpa.BindUserRepository;
import com.wkhmedical.repository.jpa.IBindUserRepository;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BindUserRepositoryImpl implements IBindUserRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	BindUserRepository bindUserRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<BindUserDTO> findBindUserList(BindUserBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM bind_user");
		sqlBuf.append(" WHERE delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND id = ?");
		BizUtil.setSqlJoin(paramBody, "areaId", sqlBuf, paramList, " AND areaId = ?");
		BizUtil.setSqlJoin(paramBody, "utype", sqlBuf, paramList, " AND utype = ?");
		BizUtil.setSqlJoin(paramBody, "unameLike", sqlBuf, paramList, " AND ci.uname LIKE ?");
		BizUtil.setSqlJoin(paramBody, "telLike", sqlBuf, paramList, " AND ci.tel LIKE ?");
		// 同时支持姓名或电话模糊查询
		String orUnameTel = paramBody.getOrUnameTel();
		if (StringUtils.isNoneBlank(orUnameTel)) {
			sqlBuf.append(" AND (tel LIKE ? OR uname LIKE ?)");
			paramList.add("%" + orUnameTel + "%");
			paramList.add("%" + orUnameTel + "%");
		}
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		sqlBuf.append(sqlOrder);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		return hibernateSupport.findByNativeSql(BindUserDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
	}

	@Override
	public Page<BindUser> findPgBindUser(BindUserBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM bind_user";
		String sqlCount = "SELECT COUNT(1) FROM bind_user";
		// 组装where语句
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlWhere = new StringBuffer(" WHERE delFlag = 0");
		if (paramBody != null) {
			List<String> sqlStrList = new ArrayList<String>();
			sqlStrList.add(" AND id = ?");
			sqlStrList.add(" AND areaId = ?");
			sqlStrList.add(" AND utype = ?");
			sqlStrList.add(" AND isDefault = ?");
			sqlStrList.add(" AND uname LIKE ?");
			sqlStrList.add(" AND tel LIKE ?");
			BizUtil.setSqlWhere(paramBody, "id,areaId,utype,isDefault,unameLike,telLike", sqlWhere, paramList, sqlStrList);
		}
		// 同时支持姓名或电话模糊查询
		String orUnameTel = paramBody.getOrUnameTel();
		if (StringUtils.isNoneBlank(orUnameTel)) {
			sqlWhere.append(" AND (tel LIKE ? OR uname LIKE ?)");
			paramList.add("%" + orUnameTel + "%");
			paramList.add("%" + orUnameTel + "%");
		}
		// utype多IN查询
		Integer[] utypeArr = paramBody.getUtypeSel();
		if (utypeArr != null && utypeArr.length > 0) {
			sqlWhere.append(" AND utype IN (");
			int i = 0;
			for (int utypeTmp : utypeArr) {
				if (i == 0) {
					sqlWhere.append(utypeTmp);
				}
				else {
					sqlWhere.append("," + utypeTmp);
				}
				i++;
			}
			sqlWhere.append(")");
		}
		// sex多IN查询
		String[] sexArr = paramBody.getSexSel();
		if (sexArr != null && sexArr.length > 0) {
			if (!AssistUtil.isArrContains(sexArr, "All")) {
				sqlWhere.append(" AND sex IN (");
				int i = 0;
				for (String sexTmp : sexArr) {
					if (i == 0) {
						sqlWhere.append("?");
					}
					else {
						sqlWhere.append(",?");
					}
					paramList.add(sexTmp);
					i++;
				}
				sqlWhere.append(")");
			}
		}
		// 日期查询
		Integer timeSel = paramBody.getTimeSel();
		if (timeSel != null) {
			if (timeSel.intValue() == 1) {
				sqlWhere.append(" AND insTime >=? AND insTime<=?");
				paramList.add(paramBody.getTimeStart());
				paramList.add(paramBody.getTimeEnd() + " 23:59:59");
			}
			else if (timeSel.intValue() == 2) {
				sqlWhere.append(" AND updTime >=? AND updTime<=?");
				paramList.add(paramBody.getTimeStart());
				paramList.add(paramBody.getTimeEnd() + " 23:59:59");
			}
		}

		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY insTime DESC");
		return bindUserRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, paramList.toArray(), pageable);
	}

	@Override
	public BindUser findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM bind_user");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<BindUser> lstBindUser = hibernateSupport.findByNativeSql(BindUser.class, sqlBuf.toString(), paramList.toArray());
		if (lstBindUser != null && lstBindUser.size() > 0) {
			return lstBindUser.get(0);
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

	@Override
	public void updateBindUserDefault(Long areaId, Integer utype, Integer isDefault) {
		if (utype == null || areaId == null || isDefault == null) return;
		int defaultWhere = isDefault == 0 ? 1 : 0;
		String sql = "UPDATE bind_user SET isDefault =" + isDefault + " WHERE areaId=" + areaId + " AND utype=" + utype + " AND isDefault="
				+ defaultWhere;
		jdbcQuery.getJdbcTemplate().execute(sql);
	}

}
