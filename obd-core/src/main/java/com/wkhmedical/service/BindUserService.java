package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserParam;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.BindUserAddBody;
import com.wkhmedical.dto.BindUserEditBody;
import com.wkhmedical.po.BindUser;

public interface BindUserService {

	BindUser getInfo(BindUserParam paramBody);

	List<BindUserDTO> getList(Paging<BindUserBody> paramBody);
	
	Page<BindUser> getPgList(Paging<BindUserBody> paramBody);

	void addInfo(BindUserAddBody infoBody);

	void updateInfo(BindUserEditBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);
}
