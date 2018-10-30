package com.wkhmedical.service.impl;

import java.io.File;
import java.util.ArrayList;
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
import com.wkhmedical.dto.EquipExcelDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.po.EquipInfo;
import com.wkhmedical.repository.jpa.EquipInfoRepository;
import com.wkhmedical.service.EquipInfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.ExcelUtil;

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
			throw new BizRuntimeException("info_not_exists", id);
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
			throw new BizRuntimeException("info_not_exists", id);
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
		}
		catch (EmptyResultDataAccessException e) {
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

	@Override
	public List<EquipExcelDTO> getExcelList(String excelPath, Integer areaId) {
		List<EquipExcelDTO> rtnList = new ArrayList<EquipExcelDTO>();
		// 判断区县ID是否合法
		// TODO 需要Guava Cache
		// 读取Excel数据
		File file = new File(BizUtil.getUploadPath() + excelPath);
		try {
			List<String[]> lstStrArrs = ExcelUtil.readExcel(file, null);
			// 解析Excel内容
			int type = 0;// 分类。0：主料；1：辅料
			int rowNum = 1;// 当前行数
			int colNum = 1;// 当前列数
			EquipExcelDTO equipExcel = null;
			// 循环行
			for (String[] strArr : lstStrArrs) {
				colNum = 1;
				if (rowNum == 1) {
					if (type == 0) {
						// 获取设备名称，即车辆名称
						for (String str : strArr) {
							log.info("车辆设备名：" + str);
							break;
						}
					}
				}
				else if (rowNum == 2) {
					// 标题栏不做处理
				}
				else {
					equipExcel = new EquipExcelDTO();
					equipExcel.setType(type);
					// 循环列
					for (String str : strArr) {
						if (ExcelUtil.sheetSplit.equals(str)) {
							// 开启新的sheet内容
							type = 1;
							rowNum = 0;
							break;
						}

						if (type == 0) {
							switch (colNum) {
							case 1:
								equipExcel.setName(str);
								break;
							case 2:
								equipExcel.setFactory(str);
								break;
							case 3:
								equipExcel.setXhNum(str);
								break;
							case 4:
								equipExcel.setBhNum(str);
								break;
							case 5:
								equipExcel.setVersion(str);
								break;
							case 6:
								equipExcel.setBirthDate(str);
								break;
							case 7:
								equipExcel.setNote(str);
								break;
							default:
								break;
							}
						}
						else if (type == 1) {
							switch (colNum) {
							case 1:
								equipExcel.setName(str);
								break;
							case 2:
								equipExcel.setFactory(str);
								break;
							case 3:
								equipExcel.setXhNum(str);
								break;
							case 4:
								equipExcel.setBhNum(str);
								break;
							case 5:
								equipExcel.setCountNum(str);
								break;
							case 6:
								equipExcel.setNote(str);
								break;
							default:
								break;
							}
						}
						colNum++;
					}
					// 此判断防止换新Sheet时，添加空记录到列表中
					if (rowNum > 0) {
						rtnList.add(equipExcel);
					}
				}
				rowNum++;
			}
		}
		catch (Exception e) {
			throw new BizRuntimeException("file_analysis_error");
		}
		return rtnList;
	}
}
