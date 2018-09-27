package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserPage;
import com.wkhmedical.po.BindUser;

public interface IBindUserRepository {
	
	List<BindUserDTO> findBindUserList(BindUserPage paramBody);
	
	Page<BindUser> findPgBindUser(BindUserPage paramBody);

	Integer findCount(Long id);
}
