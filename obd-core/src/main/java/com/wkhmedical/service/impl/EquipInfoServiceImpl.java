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
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.po.EquipInfo;
import com.wkhmedical.repository.jpa.EquipInfoRepository;
import com.wkhmedical.service.EquipInfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EquipInfoServiceImpl implements EquipInfoService {

	@Resource
	EquipInfoRepository equipInfoRepository;

	@Override
	public EquipInfo getInfo(EquipInfoBody paramBody) {
		String id = paramBody.getId();
		Optional<EquipInfo> optObj = equipInfoRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		return optObj.get();
	}

	@Override
	public List<EquipInfoDTO> getList(Paging<EquipInfoBody> paramBody) {
		EquipInfoBody queryObj = paramBody.getQuery();
		return equipInfoRepository.findEquipInfoList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<EquipInfo> getPgList(Paging<EquipInfoBody> paramBody) {
		EquipInfoBody queryObj = paramBody.getQuery();
		return equipInfoRepository.findPgEquipInfo(queryObj, paramBody.toPageable());
	}

	@Override
	public void addInfo(EquipInfoBody infoBody) {
		// 组装Bean
		EquipInfo equipInfo = AssistUtil.coverBean(infoBody, EquipInfo.class);
		equipInfo.setId(BizUtil.genDbId());
		// 入库
		equipInfoRepository.save(equipInfo);
	}

	@Override
	@Transactional
	public void updateInfo(EquipInfoBody infoBody) {
		// 判断待修改记录唯一性
		String id = infoBody.getId();
		EquipInfo equipInfoUpd = equipInfoRepository.findByKey(id);
		if (equipInfoUpd == null) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(equipInfoUpd, infoBody);
		// 更新库记录
		equipInfoRepository.update(equipInfoUpd);
	}

	@Override
	public void deleteInfo(String id) {
		try {
			equipInfoRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(String id) {
		Optional<EquipInfo> optObj = equipInfoRepository.findById(id);
		if (optObj.isPresent()) {
			EquipInfo equipInfoUpd = optObj.get();
			equipInfoUpd.setDelFlag(1);
			equipInfoRepository.update(equipInfoUpd);
		}
	}

	@Override
	public Long getCountSum() {
		return equipInfoRepository.count();
	}
}
