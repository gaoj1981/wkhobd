package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.UserExinfoDTO;
import com.wkhmedical.dto.UserExinfoBody;
import com.wkhmedical.po.UserExinfo;

public interface IUserExinfoRepository {
	UserExinfo findByKey(String id);
	
	List<UserExinfoDTO> findUserExinfoList(UserExinfoBody paramBody, Pageable pageable);
	
	Page<UserExinfoDTO> findPgUserExinfoDTO(UserExinfoBody paramBody, Pageable pageable);
	
	Page<UserExinfo> findPgUserExinfo(UserExinfoBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
