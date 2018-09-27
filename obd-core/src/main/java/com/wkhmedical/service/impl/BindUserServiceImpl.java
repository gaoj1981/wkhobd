package com.wkhmedical.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserPage;
import com.wkhmedical.dto.BindUserParam;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.repository.jpa.BindUserRepository;
import com.wkhmedical.service.BindUserService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BindUserServiceImpl implements BindUserService {

	@Resource
	BindUserRepository bindUserRepository;

	@Override
	public BindUser getInfo(BindUserParam paramBody) {
		Long id = paramBody.getId();
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public List<BindUserDTO> getList(BindUserPage paramBody) {
		return bindUserRepository.findBindUserList(paramBody);
	}

	@Override
	public Page<BindUser> getPgList(BindUserPage paramBody) {
		return bindUserRepository.findPgBindUser(paramBody);
	}

	@Override
	public void addInfo(BindUserBody infoBody) {
		// 组装Bean
		BindUser bindUser = AssistUtil.coverBean(infoBody, BindUser.class);
		bindUser.setId(BizUtil.genDbId());
		bindUser.setDelFlag(0);
		// 入库
		bindUserRepository.save(bindUser);
	}

	@Override
	@Transactional
	public void updateInfo(BindUserBody infoBody) {
		// id检查
		Long id = infoBody.getId();
		if (id == null) {
			throw new BizRuntimeException("info_edit_id_must", id);
		}
		// 判断待修改记录唯一性
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		BindUser bindUserUpd = optObj.get();
		// merge修改body与原记录对象
		BeanUtils.merageProperty(bindUserUpd, infoBody);
		// 更新库记录
		bindUserRepository.update(bindUserUpd);
	}

	@Override
	public void deleteInfo(Long id) {
		bindUserRepository.deleteById(id);
	}

	@Override
	public void delInfo(Long id) {
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		log.info("逻辑删除");
		BindUser bindUserUpd = optObj.get();
		bindUserUpd.setDelFlag(1);
		bindUserRepository.update(bindUserUpd);
	}

}
