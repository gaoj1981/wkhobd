package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.po.BindUser;

public interface IBindUserRepository {
	
	List<BindUserDTO> findBindUserList(BindUserBody paramBody, Pageable pageable);
	
	Page<BindUser> findPgBindUser(BindUserBody paramBody, Pageable pageable);

	Integer findCount(Long id);
}
