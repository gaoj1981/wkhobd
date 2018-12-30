package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.po.BindUser;

public interface IBindUserRepository {
	BindUser findByKey(String id);

	List<BindUserDTO> findBindUserList(BindUserBody paramBody, Pageable pageable);

	Page<BindUser> findPgBindUser(BindUserBody paramBody, Pageable pageable);

	Integer findCount(String id);

	void updateBindUserDefault(Long areaId, Integer utype, Integer isDefault);
}
