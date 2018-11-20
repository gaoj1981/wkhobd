package com.wkhmedical.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.UserExinfoBody;
import com.wkhmedical.dto.UserExinfoDTO;
import com.wkhmedical.po.UserExinfo;
import com.wkhmedical.repository.jpa.UserExinfoRepository;
import com.wkhmedical.service.UserExinfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserExinfoServiceImpl implements UserExinfoService {

	@Resource
	UserExinfoRepository userExinfoRepository;

	@Override
	public UserExinfo getInfo(UserExinfoBody paramBody) {
		String id = paramBody.getId();
		Optional<UserExinfo> optObj = userExinfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public UserExinfo getUserExinfo(String id) {
		Optional<UserExinfo> optObj = userExinfoRepository.findById(id);
		if (!optObj.isPresent()) {
			return null;
		}
		return optObj.get();
	}

	@Override
	public List<UserExinfoDTO> getList(Paging<UserExinfoBody> paramBody) {
		UserExinfoBody queryObj = paramBody.getQuery();
		return userExinfoRepository.findUserExinfoList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<UserExinfo> getPgList(Paging<UserExinfoBody> paramBody) {
		UserExinfoBody queryObj = paramBody.getQuery();
		return userExinfoRepository.findPgUserExinfo(queryObj, paramBody.toPageable());
	}

	@Override
	public void addInfo(UserExinfoBody infoBody) {
		// 组装Bean
		UserExinfo userExinfo = AssistUtil.coverBean(infoBody, UserExinfo.class);
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		userExinfo.setId(BizUtil.genDbIdStr(idWorker));
		// 入库
		userExinfoRepository.save(userExinfo);
	}

	@Override
	@Transactional
	public void updateInfo(UserExinfoBody infoBody) {
		// 判断待修改记录唯一性
		String id = infoBody.getId();
		UserExinfo userExinfoUpd = userExinfoRepository.findByKey(id);
		if (userExinfoUpd == null) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(userExinfoUpd, infoBody);
		// 更新库记录
		userExinfoRepository.update(userExinfoUpd);
	}

	@Override
	public void deleteInfo(String id) {
		try {
			userExinfoRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(String id) {
		Optional<UserExinfo> optObj = userExinfoRepository.findById(id);
		if (optObj.isPresent()) {
			UserExinfo userExinfoUpd = optObj.get();
			userExinfoUpd.setDelFlag(1);
			userExinfoRepository.update(userExinfoUpd);
		}
	}

	@Override
	public Long getCountSum() {
		return userExinfoRepository.count();
	}
}
