package com.wkhmedical.repository.jpa;

import org.springframework.stereotype.Repository;

import com.taoxeo.repository.JpaRepository;
import com.wkhmedical.po.OauthPri;

@Repository
public interface OauthPriRepository extends JpaRepository<OauthPri, String>, IOauthPriRepository {
	
}
