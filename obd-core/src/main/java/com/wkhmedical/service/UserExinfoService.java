package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.UserExinfoBody;
import com.wkhmedical.dto.UserExinfoDTO;
import com.wkhmedical.po.UserExinfo;

public interface UserExinfoService {

	UserExinfo getInfo(UserExinfoBody paramBody);

	UserExinfo getUserExinfo(String id);

	List<UserExinfoDTO> getList(Paging<UserExinfoBody> paramBody);

	Page<UserExinfo> getPgList(Paging<UserExinfoBody> paramBody);

	void addInfo(UserExinfoBody infoBody);

	void updateInfo(UserExinfoBody infoBody);

	void deleteInfo(String id);

	void delInfo(String id);

	Long getCountSum();
}
