package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.po.WareRecord;

public interface WareRecordService {

	WareRecord getInfo(WareRecordBody paramBody);

	WareRecordDTO getExInfo(WareRecordBody paramBody);

	List<WareRecordDTO> getList(Paging<WareRecordBody> paramBody);

	Page<WareRecordDTO> getPgList(Paging<WareRecordBody> paramBody);

	void addInfo(WareRecordBody infoBody);

	void updateInfo(WareRecordBody infoBody);

	void deleteInfo(String id);

	void delInfo(String id);

	Long getCountSum();
}
