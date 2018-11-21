package com.wkhmedical.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wkhmedical.dto.OauthPriDTO;
import com.wkhmedical.dto.OauthPriBody;
import com.wkhmedical.po.OauthPri;

public interface IOauthPriRepository {
	OauthPri findByKey(String id);
	
	List<OauthPriDTO> findOauthPriList(OauthPriBody paramBody, Pageable pageable);
	
	Page<OauthPriDTO> findPgOauthPriDTO(OauthPriBody paramBody, Pageable pageable);
	
	Page<OauthPri> findPgOauthPri(OauthPriBody paramBody, Pageable pageable);

	Integer findCount(String id);
}
