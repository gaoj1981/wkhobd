package com.wkhmedical.service.impl;

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
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.dto.WareRecordPage;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.WareRecord;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.WareRecordRepository;
import com.wkhmedical.service.WareRecordService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class WareRecordServiceImpl implements WareRecordService {

	@Resource
	WareRecordRepository wareRecordRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public WareRecord getInfo(WareRecordBody paramBody) {
		Long id = paramBody.getId();
		Optional<WareRecord> optObj = wareRecordRepository.findById(id);
		if (!optObj.isPresent()) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		return optObj.get();
	}

	@Override
	public List<WareRecordDTO> getList(WareRecordPage paramBody) {
		WareRecordBody queryObj = paramBody.getQuery();
		return wareRecordRepository.findWareRecordList(queryObj, paramBody.getPage(), paramBody.getSize());
	}

	@Override
	public Page<WareRecord> getPgList(WareRecordPage paramBody) {
		WareRecordBody queryObj = paramBody.getQuery();
		return wareRecordRepository.findPgWareRecord(queryObj, paramBody.getPage(), paramBody.getSize());
	}

	@Override
	public void addInfo(WareRecordBody infoBody) {
		// 校验eid
		String eid = infoBody.getEid();
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		// 组装Bean
		WareRecord wareRecord = AssistUtil.coverBean(infoBody, WareRecord.class);
		wareRecord.setId(BizUtil.genDbId());
		wareRecord.setCid(carInfo.getId());
		// 入库
		wareRecordRepository.save(wareRecord);
	}

	@Override
	@Transactional
	public void updateInfo(WareRecordBody infoBody) {
		// 判断待修改记录唯一性
		Long id = infoBody.getId();
		WareRecord wareRecordUpd = wareRecordRepository.findByKey(id);
		if (wareRecordUpd == null) {
			throw new BizRuntimeException("info_not_exists", id + "");
		}
		// 校验eid
		String eid = infoBody.getEid();
		if (StringUtils.isNotBlank(eid)) {
			CarInfo carInfo = carInfoRepository.findByEid(eid);
			if (carInfo == null) {
				throw new BizRuntimeException("carinfo_not_exists", eid);
			}
			wareRecordUpd.setCid(carInfo.getId());
		}
		// merge修改body与原记录对象
		BeanUtils.merageProperty(wareRecordUpd, infoBody);
		// 更新库记录
		wareRecordRepository.update(wareRecordUpd);
	}

	@Override
	public void deleteInfo(Long id) {
		try {
			wareRecordRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("物理删除id不存在" + id);
		}
	}

	@Override
	public void delInfo(Long id) {
		Optional<WareRecord> optObj = wareRecordRepository.findById(id);
		if (optObj.isPresent()) {
			WareRecord wareRecordUpd = optObj.get();
			wareRecordUpd.setDelFlag(1);
			wareRecordRepository.update(wareRecordUpd);
		}
	}

	@Override
	public Long getCountSum() {
		return wareRecordRepository.count();
	}
}
