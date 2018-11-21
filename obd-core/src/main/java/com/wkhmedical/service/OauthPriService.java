package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.OauthPriBody;
import com.wkhmedical.dto.OauthPriDTO;
import com.wkhmedical.po.OauthPri;

public interface OauthPriService {

	OauthPri getInfo(OauthPriBody paramBody);

	List<OauthPriDTO> getList(Paging<OauthPriBody> paramBody);
	
	Page<OauthPri> getPgList(Paging<OauthPriBody> paramBody);

	void addInfo(OauthPriBody infoBody);

	void updateInfo(OauthPriBody infoBody);

	void deleteInfo(String id);

	void delInfo(String id);
	
	Long getCountSum();
}
