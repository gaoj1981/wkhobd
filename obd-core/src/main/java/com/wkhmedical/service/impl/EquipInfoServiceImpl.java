package com.wkhmedical.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.taoxeo.repository.Paging;
import com.wkhmedical.constant.EquipConstant;
import com.wkhmedical.dto.EquipExcelDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.EquipInfo;
import com.wkhmedical.po.MgEquipExcel;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.jpa.EquipInfoRepository;
import com.wkhmedical.repository.mongo.EquipExcelRepository;
import com.wkhmedical.service.EquipInfoService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.ExcelUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EquipInfoServiceImpl implements EquipInfoService {

	@Resource
	EquipInfoRepository equipInfoRepository;

	@Resource
	EquipExcelRepository equipExcelRepository;

	@Resource
	CarInfoRepository carInfoRepository;

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
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		equipInfo.setId(BizUtil.genDbIdStr(idWorker));
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
	public EquipExcelDTO getExcelList(String excelPath, Integer areaId) {
		EquipExcelDTO rtnDTO = new EquipExcelDTO();
		List<MgEquipExcel> rtnList = new ArrayList<MgEquipExcel>();
		int errNum = 0;
		// 判断区县ID是否合法
		// TODO 需要Guava Cache
		File file = new File(BizUtil.getUploadPath() + excelPath);
		// 校验Excel
		ExcelUtil.checkFileValid(file, 2, new int[] { 18, 20 }, new int[] { 7, 6 });
		// 读取Excel数据
		String errType = null;
		boolean isLockDog = false;
		boolean isHaveEid = false;
		String lockdogId = "";
		String equipName = "";
		try {
			List<String[]> lstStrArrs = ExcelUtil.readExcel(file, null);
			// 解析Excel内容
			int type = 0;// 分类。0：主料；1：辅料
			int rowNum = 1;// 当前行数
			int colNum = 1;// 当前列数
			MgEquipExcel equipExcel = null;
			// 循环行
			for (String[] strArr : lstStrArrs) {
				colNum = 1;
				if (rowNum == 1) {
					if (type == 0) {
						// 获取设备名称，即车辆名称
						for (String str : strArr) {
							equipName = str;
							break;
						}
					}
				}
				else if (rowNum == 2) {
					// 标题栏不做处理
				}
				else {
					equipExcel = new MgEquipExcel();
					equipExcel.setExcelPath(excelPath);
					equipExcel.setType(type);
					equipExcel.setEquipName(equipName);
					// 循环列
					int delFlag = 0;
					String description = "";
					String icon = "";
					for (String str : strArr) {
						if (StringUtils.isBlank(str)) {
							str = "";
						}
						str = str.trim();
						if (ExcelUtil.sheetSplit.equals(str)) {
							// 开启新的sheet内容
							type = 1;
							rowNum = 0;
							break;
						}

						if (type == 0) {
							switch (colNum) {
							case 1:
								if (str.length() > 50) {
									delFlag = 1;
									description += "设备名称不能超过50个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								else {
									int containsIndex = AssistUtil.getArrContainsIndex(EquipConstant.equipMainArr, str);
									if (containsIndex < 0) {
										icon = "tool";
										delFlag = 1;
										description += "不存在的设备名（" + rowNum + "行，" + colNum + "列）；";
									}
									else {
										icon = EquipConstant.equipMainIconArr[containsIndex];
									}
								}
								if (EquipConstant.equipMainArr[15].equals(str)) {
									// 加密狗判断
									isLockDog = true;
								}
								equipExcel.setIcon(icon);
								equipExcel.setName(str);
								break;
							case 2:
								if (str.length() > 255) {
									delFlag = 1;
									description += "厂家不能超过255个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setFactory(str);
								break;
							case 3:
								if (str.length() > 100) {
									delFlag = 1;
									description += "型号不能超过100个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setXhNum(str);
								break;
							case 4:
								if (str.length() > 100) {
									delFlag = 1;
									description += "编号不能超过100个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								if (isLockDog) {
									isLockDog = false;
									// 加密狗判断
									if (StringUtils.isBlank(str)) {
										errType = "file_excel_biz_lockdog_need";
										throw new BizRuntimeException(errType);
									}
									else if (str.length() > 18) {
										errType = "file_excel_biz_lockdog_eid_max";
										throw new BizRuntimeException(errType);
									}
									lockdogId = str;
									isHaveEid = true;
								}
								equipExcel.setBhNum(str);
								break;
							case 5:
								if (str.length() > 100) {
									delFlag = 1;
									description += "软件版本不能超过100个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setVersion(str);
								break;
							case 6:
								if (str.length() > 8) {
									delFlag = 1;
									description += "出厂时间格式为YYYYMMDD（" + rowNum + "行，" + colNum + "列）；";
								}
								else if (str.length() > 0) {
									if (DateUtil.parseToDate(str, "yyyyMMdd") == null) {
										delFlag = 1;
										description += "出厂时间格式为YYYYMMDD（" + rowNum + "行，" + colNum + "列）；";
									}
								}
								equipExcel.setBirthDate(str);
								break;
							case 7:
								if (str.length() > 500) {
									delFlag = 1;
									description += "备注不能超过500个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setNote(str);
								break;
							default:
								break;
							}
						}
						else if (type == 1) {
							switch (colNum) {
							case 1:
								if (str.length() > 50) {
									delFlag = 1;
									description += "设备名称不能超过50个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								else {
									int containsIndex = AssistUtil.getArrContainsIndex(EquipConstant.equipAssistArr, str);
									if (containsIndex < 0) {
										icon = "tool";
										delFlag = 1;
										description += "不存在的设备名（" + rowNum + "行，" + colNum + "列）；";
									}
									else {
										icon = EquipConstant.equipAssistIconArr[containsIndex];
									}
								}
								equipExcel.setIcon(icon);
								equipExcel.setName(str);
								break;
							case 2:
								if (str.length() > 255) {
									delFlag = 1;
									description += "厂家不能超过255个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setFactory(str);
								break;
							case 3:
								if (str.length() > 100) {
									delFlag = 1;
									description += "型号不能超过100个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setXhNum(str);
								break;
							case 4:
								if (str.length() > 100) {
									delFlag = 1;
									description += "编号不能超过100个字符（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setBhNum(str);
								break;
							case 5:
								if (str.length() > 11) {
									delFlag = 1;
									description += "数量不能超过11位（" + rowNum + "行，" + colNum + "列）；";
								}
								try {
									Long.valueOf(str);
								}
								catch (Exception e) {
									delFlag = 1;
									description += "数量必须为数字（" + rowNum + "行，" + colNum + "列）；";
								}
								equipExcel.setCountNum(str);
								break;
							case 6:
								if (str.length() > 500) {
									delFlag = 1;
									description += "备注不能超过500个字符（" + rowNum + "行，" + colNum + "列）；";
								}
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
						if (delFlag == 1) {
							errNum++;
						}
						equipExcel.setDelFlag(delFlag);
						equipExcel.setDescription(description);
						rtnList.add(equipExcel);
					}
				}
				rowNum++;
			}
			// 校验加密狗合法性
			if (isHaveEid) {
				// excel数据临时保存
				equipExcelRepository.saveAll(rtnList);
				rtnDTO.setTotal(rtnList.size());
				rtnDTO.setErrNum(errNum);
				rtnDTO.setSucNum(rtnList.size() - errNum);
				rtnDTO.setLstData(rtnList);
				rtnDTO.setEid(lockdogId);
				return rtnDTO;
			}
			else {
				errType = "file_excel_biz_lockdog_need";
				throw new BizRuntimeException(errType);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			if (errType != null) {
				throw new BizRuntimeException(errType);
			}
			else {
				throw new BizRuntimeException("file_analysis_error");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.EquipInfoService#importEquipExcel(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional
	public boolean importEquipExcel(String excelPath, Integer areaId, String eid) {
		List<EquipInfo> lstBatch = new ArrayList<EquipInfo>();
		//
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		//
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
		//
		List<MgEquipExcel> lstExcel = equipExcelRepository.findByExcelPathAndDelFlag(excelPath, 0);
		String name = "";// 查找车辆用
		for (MgEquipExcel tmpExcel : lstExcel) {
			name = tmpExcel.getName();
			if (carInfo == null) {
				// 针对车辆更新车辆主表
				if ("车辆".equals(name)) {
					carInfo = new CarInfo();
					carInfo.setId(BizUtil.genDbIdStr(idWorker));
					carInfo.setEid(eid);
					carInfo.setCarName(tmpExcel.getEquipName());
					carInfo.setFrameNum(tmpExcel.getBhNum());
					carInfo.setGroupId(areaId + "");
					carInfo.setProvId(BizUtil.getProvId(areaId));
					carInfo.setCityId(BizUtil.getCityId(areaId));
					carInfo.setAreaId(areaId);
					carInfoRepository.save(carInfo);
				}
			}
			EquipInfo equipInfo = new EquipInfo();
			equipInfo.setId(BizUtil.genDbIdStr(idWorker));
			equipInfo.setEid(eid);
			equipInfo.setType(tmpExcel.getType());
			equipInfo.setName(name);
			equipInfo.setBhNum(tmpExcel.getBhNum());
			equipInfo.setXhNum(tmpExcel.getXhNum());
			equipInfo.setFactory(tmpExcel.getFactory());
			equipInfo.setBirthDate(DateUtil.parseToDate(tmpExcel.getBirthDate(), "yyyyMMdd"));
			equipInfo.setVersion(tmpExcel.getVersion());
			equipInfo.setCountNum(StringUtils.isBlank(tmpExcel.getCountNum()) ? 0L : Long.valueOf(tmpExcel.getCountNum()));
			equipInfo.setNote(tmpExcel.getNote());
			lstBatch.add(equipInfo);
		}
		if (lstBatch.size() > 0) {
			equipInfoRepository.saveAll(lstBatch);
		}
		return true;
	}
}
