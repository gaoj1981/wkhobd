package com.wkhmedical.service.impl;

import java.util.Date;
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
import com.wkhmedical.dto.CarMotBody;
import com.wkhmedical.dto.CarMotDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.CarMot;
import com.wkhmedical.po.CarMotCopy;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.CarMotCopyRepository;
import com.wkhmedical.repository.jpa.CarMotRepository;
import com.wkhmedical.service.CarMotService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CarMotServiceImpl implements CarMotService {

	@Resource
	CarMotRepository carMotRepository;
	@Resource
	CarMotCopyRepository carMotCopyRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public CarMot getInfo(CarMotBody paramBody) {
		String id = paramBody.getId();
		Optional<CarMot> optObj = carMotRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		return optObj.get();
	}

	@Override
	public CarMotDTO getExInfo(CarMotBody paramBody) {
		CarMotDTO rtnDTO = new CarMotDTO();
		String id = paramBody.getId();
		Optional<CarMot> optObj = carMotRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		CarMot carMot = optObj.get();
		// 获取车辆对象
		Optional<CarInfo> optCar = carInfoRepository.findById(carMot.getCid());
		if (!optCar.isPresent()) {
			throw new BizRuntimeException("carmot_nocar_exists");
		}
		CarInfo carInfo = optCar.get();
		// 合并年检对象（此处开发需注意同名属性覆盖）
		BeanUtils.merageProperty(rtnDTO, carInfo);
		BeanUtils.merageProperty(rtnDTO, carMot);
		return rtnDTO;
	}

	@Override
	public List<CarMotDTO> getList(Paging<CarMotBody> paramBody) {
		CarMotBody queryObj = paramBody.getQuery();
		return carMotRepository.findCarMotList(queryObj, paramBody.toPageable());
	}

	@Override
	public Page<CarMotDTO> getPgList(Paging<CarMotBody> paramBody) {
		CarMotBody queryObj = paramBody.getQuery();
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
			return carMotCopyRepository.findByExpDay(expDateMin, expDateMax, paramBody.toPageable());
		}
		else {
			return carMotRepository.findPgCarMotDTO(queryObj, paramBody.toPageable());
		}
	}

	@Override
	@Transactional
	public void addInfo(CarMotBody infoBody) {
		// 校验eid
		String eid = infoBody.getEid();
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		// 校验是否已在有效期
		Date maxExpDate = carMotRepository.findMaxExpDate(carInfo.getId());
		if (maxExpDate != null) {
			Date expDate = infoBody.getExpDate();
			if (maxExpDate.compareTo(expDate) > 0) {
				throw new BizRuntimeException("carmot_exists_expired");
			}
		}
		// 组装Bean
		CarMot carMot = AssistUtil.coverBean(infoBody, CarMot.class);
		String idKey = BizUtil.genDbIdStr();
		carMot.setId(idKey);
		carMot.setCid(carInfo.getId());
		// 入库
		carMotRepository.save(carMot);
		// 拷贝年检最大有效记录
		Date dtNow = new Date();
		// step1:删除当前最大有效记录
		carMotCopyRepository.deleteByCid(carInfo.getId());
		// step2:同步年检最大有效记录
		CarMotCopy carMotCopy = new CarMotCopy();
		BeanUtils.merageProperty(carMotCopy, carMot);
		carMotCopy.setInsTime(dtNow);
		carMotCopyRepository.save(carMotCopy);
	}

	@Override
	@Transactional
	public void updateInfo(CarMotBody infoBody) {
		// 判断待修改记录唯一性
		String id = infoBody.getId();
		CarMot carMotUpd = carMotRepository.findByKey(id);
		if (carMotUpd == null || carMotUpd.getDelFlag().intValue() == 1) {
			throw new BizRuntimeException("info_not_exists", id);
		}
		// 判断是否同步最大值记录
		boolean isNeedCopy = false;
		// 年检有效期判断
		String cid = carMotUpd.getCid();
		Date expDate = carMotUpd.getExpDate();
		Date maxExpDate = carMotRepository.findMaxExpDate(cid);
		Date pageExpDate = infoBody.getExpDate();
		if (expDate.compareTo(maxExpDate) == 0) {
			// 当前修改记录是最大记录
			if (maxExpDate.compareTo(pageExpDate) > 0) {
				List<CarMot> lstObjs = carMotRepository.findByCidAndDelFlagAndExpDateGreaterThanAndExpDateLessThanOrderByExpDateDesc(cid, 0,
						pageExpDate, maxExpDate);
				if (lstObjs != null && lstObjs.size() > 0) {
					CarMot carMotMin = lstObjs.get(0);
					throw new BizRuntimeException("carmot_expdate_over_min", DateUtil.formatDate(carMotMin.getExpDate(), "yyyyMM"));
				}
				isNeedCopy = true;
			}
			else if (maxExpDate.compareTo(pageExpDate) < 0) {
				isNeedCopy = true;
			}
		}
		else {
			// 当前修改记录非最大记录
			if (maxExpDate.compareTo(pageExpDate) < 0) {
				throw new BizRuntimeException("carmot_expdate_over_max", maxExpDate);
			}
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(carMotUpd, infoBody);
		// 更新库记录
		carMotRepository.update(carMotUpd);
		// 同步年检最大记录
		if (isNeedCopy) {
			// step1:删除当前最大有效记录
			carMotCopyRepository.deleteByCid(carMotUpd.getCid());
			// step2:同步年检最大有效记录
			CarMotCopy carMotCopy = new CarMotCopy();
			BeanUtils.merageProperty(carMotCopy, carMotUpd);
			carMotCopyRepository.save(carMotCopy);
		}
	}

	@Override
	public void deleteInfo(String id) {
		try {
			carMotRepository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(String id) {
		Optional<CarMot> optObj = carMotRepository.findById(id);
		if (optObj.isPresent()) {
			CarMot carMotUpd = optObj.get();
			carMotUpd.setDelFlag(1);
			carMotRepository.update(carMotUpd);
			// 同步年检最大记录
			Optional<CarMotCopy> carMotCopyOpt = carMotCopyRepository.findById(id);
			if (carMotCopyOpt.isPresent()) {
				CarMotCopy carMotCopy = carMotCopyOpt.get();
				// step1:删除当前最大有效记录
				carMotCopyRepository.delete(carMotCopy);
				List<CarMot> lstObjs = carMotRepository.findByCidAndDelFlagOrderByExpDateDesc(carMotUpd.getCid(), 0);
				if (lstObjs != null && lstObjs.size() > 0) {
					CarMot carMot = lstObjs.get(0);
					// step2:同步年检最大有效记录
					carMotCopy = new CarMotCopy();
					BeanUtils.merageProperty(carMotCopy, carMot);
					carMotCopyRepository.save(carMotCopy);
				}
			}
		}
	}

}
