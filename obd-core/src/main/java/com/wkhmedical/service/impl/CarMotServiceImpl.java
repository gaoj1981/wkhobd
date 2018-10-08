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
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.CarMot;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.CarMotRepository;
import com.wkhmedical.service.CarMotService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarMotServiceImpl implements CarMotService {

	@Resource
	CarMotRepository carMotRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public CarMot getInfo(CarMotBody paramBody) {
		Long id = paramBody.getId();
		Optional<CarMot> optObj = carMotRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public List<CarMotDTO> getList(CarMotBody paramBody) {
		return carMotRepository.findCarMotList(paramBody);
	}

	@Override
	public Page<CarMot> getPgList(CarMotBody paramBody) {
		return carMotRepository.findPgCarMot(paramBody);
	}

	@Override
	public void addInfo(CarMotBody infoBody) {
		// 校验eid
		String eid = infoBody.getEid();
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		// 组装Bean
		CarMot carMot = AssistUtil.coverBean(infoBody, CarMot.class);
		carMot.setId(BizUtil.genDbId());
		carMot.setCid(carInfo.getId());
		// 入库
		carMotRepository.save(carMot);
	}

	@Override
	@Transactional
	public void updateInfo(CarMotBody infoBody) {
		// 判断待修改记录唯一性
		Long id = infoBody.getId();
		CarMot carMotUpd = carMotRepository.findByKey(id);
		if (carMotUpd == null) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(carMotUpd, infoBody);
		// 更新库记录
		carMotRepository.update(carMotUpd);
	}

	@Override
	public void deleteInfo(Long id) {
		try {
			carMotRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(Long id) {
		Optional<CarMot> optObj = carMotRepository.findById(id);
		if (!optObj.isPresent()) {
			CarMot carMotUpd = optObj.get();
			carMotUpd.setDelFlag(1);
			carMotRepository.update(carMotUpd);
		}
	}

}