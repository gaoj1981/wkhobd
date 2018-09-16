package com.wkhmedical.repository.jpa.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.UserDTO;
import com.wkhmedical.repository.jpa.IYunUserRepository;

public class YunUserRepositoryImpl implements IYunUserRepository {
	@Value("#{query.findUserInfo}")
	private String findUserInfo;

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Override
	public UserDTO findUserInfo(String credId) {
		List<UserDTO> lstDTO = jdbcQuery.find(findUserInfo, UserDTO.class, credId, credId);
		if (lstDTO.size() == 0) {
			return null;
		}
		return lstDTO.get(0);
	}

}
