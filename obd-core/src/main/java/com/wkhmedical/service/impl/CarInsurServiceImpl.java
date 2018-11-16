package com.wkhmedical.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarInsurBody;
import com.wkhmedical.dto.CarInsurBodyAdd;
import com.wkhmedical.dto.CarInsurBodyEdit;
import com.wkhmedical.dto.CarInsurDTO;
import com.wkhmedical.dto.CarInsurParam;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.CarInsur;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.CarInsurCopyRepository;
import com.wkhmedical.repository.jpa.CarInsurRepository;
import com.wkhmedical.service.CarInsurService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarInsurServiceImpl implements CarInsurService {

	@Resource
	CarInsurRepository carInsurRepository;
	@Resource
	CarInsurCopyRepository carInsurCopyRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public CarInsur getInfo(CarInsurParam paramBody) {
		String id = paramBody.getId();
		Optional<CarInsur> optObj = carInsurRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		return optObj.get();
	}

	@Override
	public List<CarInsurDTO> getList(Paging<CarInsurBody> paramBody) {
		CarInsurBody queryObj = paramBody.getQuery();
		return carInsurRepository.findCarInsurList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<CarInsurDTO> getPgList(Paging<CarInsurBody> paramBody) {
		CarInsurBody queryObj = paramBody.getQuery();
		// 判断是否只查询过期类型
		Integer expDayFlag = queryObj.getExpDayFlag();
		if (expDayFlag != null && expDayFlag.intValue() >= 1) {
			int dayFlag = expDayFlag.intValue();
			//
			Date dtNow = new Date();
			String expDateMin = DateUtil.formatDate(dtNow, "yyyy-MM-dd");
			String expDateMax = DateUtil.formatDate(dtNow, "yyyy-MM-dd");
			if (dayFlag == 1) {
				expDateMax = DateUtil.formatDate(DateUtil.getDateAddDay(dtNow, 30), "yyyy-MM-dd");
			}
			else if (dayFlag == 2) {
				expDateMax = DateUtil.formatDate(DateUtil.getDateAddDay(dtNow, 60), "yyyy-MM-dd");
			}
			else if (dayFlag == 3) {
				expDateMax = DateUtil.formatDate(DateUtil.getDateAddDay(dtNow, 90), "yyyy-MM-dd");
			}
			else if (dayFlag == 4) {
				expDateMax = DateUtil.formatDate(dtNow, "yyyy-MM-dd");
				expDateMin = null;
			}
			return carInsurCopyRepository.findByExpDay(expDateMin, expDateMax, paramBody.toPageable());
		}
		else {
			return carInsurRepository.findPgCarInsurDTO(queryObj, paramBody.toPageable());
		}
	}

	@Override
	public void addInfo(CarInsurBodyAdd infoBody) {
		// 校验eid
		String eid = infoBody.getEid();
		// 此处不需要findByEidAndDelFlag
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		// 校验保单号是否重复
		String insurNum = infoBody.getInsurNum();
		CarInsur insurTmp = carInsurRepository.findByInsurNum(insurNum);
		if (insurTmp != null) {
			throw new BizRuntimeException("carinsur_insurnum_already_exists", insurNum);
		}
		// 组装Bean
		CarInsur carInsur = AssistUtil.coverBean(infoBody, CarInsur.class);
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		carInsur.setId(BizUtil.genDbIdStr(idWorker));
		carInsur.setCid(carInfo.getId());
		// 入库
		carInsurRepository.save(carInsur);
	}

	@Override
	@Transactional
	public void updateInfo(CarInsurBodyEdit infoBody) {
		String id = infoBody.getId();
		// 判断待修改记录唯一性
		CarInsur carInsurUpd = carInsurRepository.findByKey(id);
		if (carInsurUpd == null) {
			// 不加""exception中产生千分位
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		// 校验保单号是否重复
		String insurNum = infoBody.getInsurNum();
		if (StringUtils.isNotEmpty(insurNum)) {
			CarInsur insurTmp = carInsurRepository.findByInsurNum(insurNum);
			if (insurTmp != null && !insurTmp.getId().equals(id)) {
				throw new BizRuntimeException("carinsur_insurnum_already_exists", insurNum);
			}
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(carInsurUpd, infoBody);
		// 更新库记录
		carInsurRepository.update(carInsurUpd);
	}

	@Override
	public void deleteInfo(String id) {
		try {
			carInsurRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}

	}

	@Override
	public void delInfo(String id) {
		Optional<CarInsur> optObj = carInsurRepository.findById(id);
		if (optObj.isPresent()) {
			CarInsur carInsurUpd = optObj.get();
			carInsurUpd.setInsurNum(BizUtil.getDelBackupVal(carInsurUpd.getInsurNum()));
			carInsurUpd.setDelFlag(1);
			carInsurRepository.update(carInsurUpd);
		}
	}

}
