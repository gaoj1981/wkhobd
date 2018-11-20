package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.OauthRoleDTO;
import com.wkhmedical.dto.OauthRoleBody;
import com.wkhmedical.po.OauthRole;

public interface IOauthRoleRepository {
	OauthRole findByKey(String id);
	
	List<OauthRoleDTO> findOauthRoleList(OauthRoleBody paramBody, Pageable pageable);
	
	Page<OauthRoleDTO> findPgOauthRoleDTO(OauthRoleBody paramBody, Pageable pageable);
	
	Page<OauthRole> findPgOauthRole(OauthRoleBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
