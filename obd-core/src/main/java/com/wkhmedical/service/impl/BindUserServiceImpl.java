package com.wkhmedical.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.BindUserAddBody;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserEditBody;
import com.wkhmedical.dto.BindUserParam;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.repository.jpa.BindUserRepository;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.service.BindUserService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BindUserServiceImpl implements BindUserService {

	@Resource
	BindUserRepository bindUserRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public BindUser getInfo(BindUserParam paramBody) {
		String id = paramBody.getId();
		BindUser bindUser = bindUserRepository.findByIdAndDelFlag(id, 0);
		if (bindUser == null) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return bindUser;
	}

	@Override
	public List<BindUserDTO> getList(Paging<BindUserBody> paramBody) {
		BindUserBody queryObj = paramBody.getQuery();
		return bindUserRepository.findBindUserList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<BindUser> getPgList(Paging<BindUserBody> paramBody) {
		BindUserBody queryObj = paramBody.getQuery();
		return bindUserRepository.findPgBindUser(queryObj, paramBody.toPageable());
	}

	@Override
	@Transactional
	public void addInfo(BindUserAddBody infoBody) {
		String id = BizUtil.genDbIdStr();
		// 校验默认
		Integer isDefault = infoBody.getIsDefault();
		int utype = infoBody.getUtype();
		int areaId = infoBody.getAreaId();
		if (isDefault != null && isDefault.intValue() == 1) {
			BindUser buTmp = bindUserRepository.findByUtypeAndAreaIdAndIsDefault(utype, areaId, isDefault);
			if (buTmp != null) {
				// 已存在默认人员设置，则不允许添加默认人员
				throw new BizRuntimeException("binduser_already_default");
			}
			// 新增默认人员时，将相应区县车辆与相关人员对应
			carInfoRepository.updateCarInfoBindUser(id, utype, areaId);
		}

		// 组装Bean
		BindUser bindUser = AssistUtil.coverBean(infoBody, BindUser.class);
		bindUser.setId(id);
		bindUser.setDelFlag(0);
		// 入库
		bindUserRepository.save(bindUser);
	}

	@Override
	@Transactional
	public void updateInfo(BindUserEditBody infoBody) {
		// id检查
		String id = infoBody.getId();
		if (id == null) {
			throw new BizRuntimeException("info_edit_id_must", id);
		}
		// 判断待修改记录唯一性
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		BindUser bindUserUpd = optObj.get();
		// 处理人员分类更改的情况
		Integer utype = infoBody.getUtype();
		if (utype != null) {
			if (!utype.equals(bindUserUpd.getUtype())) {
				// 更新人员分类时，则需将原对应车辆设置为空
				carInfoRepository.updateCarInfoBindUserNull(id, bindUserUpd.getUtype());
			}
		}
		else {
			utype = bindUserUpd.getUtype();
		}
		// 校验默认
		Integer isDefault = infoBody.getIsDefault();
		if (isDefault != null) {
			// 人员更改设置为默认的情况
			if (isDefault.intValue() == 1) {
				// 校验是否已存在默认人员
				BindUser buTmp = bindUserRepository.findByUtypeAndAreaIdAndIsDefault(utype, bindUserUpd.getAreaId(), isDefault);
				if (buTmp != null && !id.equals(buTmp.getId())) {
					// 已存在默认人员设置，则不允许更改默认人员
					throw new BizRuntimeException("binduser_already_default");
				}
				// 将相应区县车辆与相关人员对应
				carInfoRepository.updateCarInfoBindUser(id, utype, bindUserUpd.getAreaId());
			}
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(bindUserUpd, infoBody);
		// 更新库记录
		bindUserRepository.update(bindUserUpd);
	}

	@Override
	public void deleteInfo(String id) {
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (optObj.isPresent()) {
			BindUser bindUserUpd = optObj.get();
			// 设置相应车辆负责人为空
			carInfoRepository.updateCarInfoBindUserNull(id, bindUserUpd.getUtype());
			// 删除
			bindUserRepository.deleteById(id);
		}

	}

	@Override
	public void delInfo(String id) {
		Optional<BindUser> optObj = bindUserRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		log.info("逻辑删除");
		BindUser bindUserUpd = optObj.get();
		bindUserUpd.setDelFlag(1);
		bindUserRepository.update(bindUserUpd);
	}

	@Override
	@Transactional
	public void updateDefault(String id, Integer isDefault, Integer isCoverAll) {
		BindUser bindUserUpd = bindUserRepository.findByKey(id);
		if (bindUserUpd == null) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		Integer utype = bindUserUpd.getUtype();
		Integer areaId = bindUserUpd.getAreaId();
		if (isCoverAll == 1) {
			// 覆盖所有当前区县对应车辆负责人
			carInfoRepository.updateCarInfoBindUser(id, utype, areaId);
		}
		// 取消之前默认负责人
		if (isDefault == 1) {
			bindUserRepository.updateBindUserDefault(areaId, utype, 0);
		}
		// 更新当前负责人默认状态
		bindUserUpd.setIsDefault(isDefault);
		bindUserRepository.update(bindUserUpd);
	}

}
