package com.wkhmedical.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.po.WareRecord;

public interface WareRecordService {

	WareRecord getInfo(WareRecordBody paramBody);

	List<WareRecordDTO> getList(Paging<WareRecordBody> paramBody);

	Page<WareRecord> getPgList(Paging<WareRecordBody> paramBody);

	void addInfo(WareRecordBody infoBody);

	void updateInfo(WareRecordBody infoBody);

	void deleteInfo(Long id);

	void delInfo(Long id);

	Long getCountSum();
}
