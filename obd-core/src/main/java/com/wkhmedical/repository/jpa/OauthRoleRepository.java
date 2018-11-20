package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.OauthRole;

@Repository
public interface OauthRoleRepository extends JpaRepository<OauthRole, String>, IOauthRoleRepository {
	
}
