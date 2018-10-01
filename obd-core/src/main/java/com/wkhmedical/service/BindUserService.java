package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserPage;
import com.wkhmedical.dto.BindUserParam;
import com.wkhmedical.dto.BindUserAddBody;
import com.wkhmedical.dto.BindUserEditBody;
import com.wkhmedical.po.BindUser;

public interface BindUserService {

	BindUser getInfo(BindUserParam paramBody);

	List<BindUserDTO> getList(BindUserPage paramBody);
	
	Page<BindUser> getPgList(BindUserPage paramBody);

	void addInfo(BindUserAddBody infoBody);

	void updateInfo(BindUserEditBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);
}
