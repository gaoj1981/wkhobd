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
import com.wkhmedical.dto.OauthPriBody;
import com.wkhmedical.dto.OauthPriDTO;
import com.wkhmedical.po.OauthPri;
import com.wkhmedical.repository.jpa.OauthPriRepository;
import com.wkhmedical.service.OauthPriService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OauthPriServiceImpl implements OauthPriService {

	@Resource
	OauthPriRepository oauthPriRepository;

	@Override
	public OauthPri getInfo(OauthPriBody paramBody) {
		String id = paramBody.getId();
		Optional<OauthPri> optObj = oauthPriRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public List<OauthPriDTO> getList(Paging<OauthPriBody> paramBody) {
		OauthPriBody queryObj = paramBody.getQuery();
		return oauthPriRepository.findOauthPriList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<OauthPri> getPgList(Paging<OauthPriBody> paramBody) {
		OauthPriBody queryObj = paramBody.getQuery();
		return oauthPriRepository.findPgOauthPri(queryObj, paramBody.toPageable());
	}

	@Override
	public void addInfo(OauthPriBody infoBody) {
		// 组装Bean
		OauthPri oauthPri = AssistUtil.coverBean(infoBody, OauthPri.class);
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		oauthPri.setId(BizUtil.genDbIdStr(idWorker));
		// 入库
		oauthPriRepository.save(oauthPri);
	}

	@Override
	@Transactional
	public void updateInfo(OauthPriBody infoBody) {
		// 判断待修改记录唯一性
		String id = infoBody.getId();
		OauthPri oauthPriUpd = oauthPriRepository.findByKey(id);
		if (oauthPriUpd == null) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(oauthPriUpd, infoBody);
		// 更新库记录
		oauthPriRepository.update(oauthPriUpd);
	}

	@Override
	public void deleteInfo(String id) {
		try {
			oauthPriRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(String id) {
		Optional<OauthPri> optObj = oauthPriRepository.findById(id);
		if (optObj.isPresent()) {
			OauthPri oauthPriUpd = optObj.get();
			oauthPriUpd.setDelFlag(1);
			oauthPriRepository.update(oauthPriUpd);
		}
	}

	@Override
	public Long getCountSum() {
		return oauthPriRepository.count();
	}
}
