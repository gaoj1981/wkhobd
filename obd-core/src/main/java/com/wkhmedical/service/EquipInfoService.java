package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.EquipDetailDTO;
import com.wkhmedical.dto.EquipExcelDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.po.EquipInfo;

public interface EquipInfoService {

	EquipInfo getInfo(EquipInfoBody paramBody);

	List<EquipInfoDTO> getList(Paging<EquipInfoBody> paramBody);

	Page<EquipInfo> getPgList(Paging<EquipInfoBody> paramBody);

	void addInfo(EquipInfoBody infoBody);

	void updateInfo(EquipInfoBody infoBody);

	void deleteInfo(String id);

	void deleteBatch(String ids);

	void delInfo(String id);

	Long getCountSum();

	EquipExcelDTO getExcelList(String excelPath, Long areaId);

	boolean importEquipExcel(String excelPath, Long areaId, String eid);

	EquipDetailDTO getDetail(String eid);

}
