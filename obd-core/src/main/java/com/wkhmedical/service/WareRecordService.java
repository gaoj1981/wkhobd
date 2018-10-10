package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.dto.WareRecordPage;
import com.wkhmedical.po.WareRecord;

public interface WareRecordService {

	WareRecord getInfo(WareRecordBody paramBody);

	List<WareRecordDTO> getList(WareRecordPage paramBody);

	Page<WareRecord> getPgList(WareRecordPage paramBody);

	void addInfo(WareRecordBody infoBody);

	void updateInfo(WareRecordBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);

	Long getCountSum();
}
