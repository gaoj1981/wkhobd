package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.constant.BizConstant;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserPage;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.repository.jpa.BindUserRepository;
import com.wkhmedical.repository.jpa.IBindUserRepository;

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
	public List<BindUserDTO> findBindUserList(BindUserPage paramBody) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM bind_user");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(paramBody.getId());
		//
		String orderByStr = " ORDER BY insTime DESC";
		sqlBuf.append(orderByStr);
		//
		int curPg = paramBody.getPaging();
		int skip = (curPg - 1) * BizConstant.FIND_PAGE_NUM;
		return hibernateSupport.findByNativeSql(BindUserDTO.class, sqlBuf.toString(), paramList.toArray(), skip,
				BizConstant.FIND_PAGE_NUM);
	}

	@Override
	public Page<BindUser> findPgBindUser(BindUserPage paramBody) {
		int page = paramBody.getPaging();
		page = page - 1;
		if (page < 0)
			page = 0;
		Pageable pageable = PageRequest.of(page, BizConstant.FIND_PAGE_NUM);
		String[] objArr = new String[0];

		return bindUserRepository.findPageByNativeSql("SELECT * FROM bind_user", "SELECT COUNT(1) FROM bind_user",
				objArr, pageable);
	}

	@Override
	public Integer findCount(Long id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

}
