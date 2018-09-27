package com.wkhmedical.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurPage;
import com.wkhmedical.dto.CarInsurParam;
import com.wkhmedical.po.CarInsur;
import com.wkhmedical.repository.jpa.CarInsurRepository;
import com.wkhmedical.service.CarInsurService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarInsurServiceImpl implements CarInsurService {

	@Resource
	CarInsurRepository carInsurRepository;

	@Override
	public CarInsur getInfo(CarInsurParam paramBody) {
		Long id = paramBody.getId();
		Optional<CarInsur> optObj = carInsurRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public List<CarInsurDTO> getList(CarInsurPage paramBody) {
		return carInsurRepository.findCarInsurList(paramBody);
	}

	@Override
	public Page<CarInsur> getPgList(CarInsurPage paramBody) {
		return carInsurRepository.findPgCarInsur(paramBody);
	}

	@Override
	public void addInfo(CarInsurBody infoBody) {
		// 组装Bean
		CarInsur carInsur = AssistUtil.coverBean(infoBody, CarInsur.class);
		carInsur.setId(BizUtil.genDbId());
		carInsur.setDelFlag(0);
		// 入库
		carInsurRepository.save(carInsur);
	}

	@Override
	@Transactional
	public void updateInfo(CarInsurBody infoBody) {
		// id检查
		Long id = infoBody.getId();
		if (id == null) {
			throw new BizRuntimeException("info_edit_id_must", id);
		}
		// 判断待修改记录唯一性
		Optional<CarInsur> optObj = carInsurRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		CarInsur carInsurUpd = optObj.get();
		// merge修改body与原记录对象
		BeanUtils.merageProperty(carInsurUpd, infoBody);
		// 更新库记录
		carInsurRepository.update(carInsurUpd);
	}

	@Override
	public void deleteInfo(Long id) {
		carInsurRepository.deleteById(id);
	}

	@Override
	public void delInfo(Long id) {
		Optional<CarInsur> optObj = carInsurRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		log.info("逻辑删除");
		CarInsur carInsurUpd = optObj.get();
		carInsurUpd.setDelFlag(1);
		carInsurRepository.update(carInsurUpd);
	}

}
