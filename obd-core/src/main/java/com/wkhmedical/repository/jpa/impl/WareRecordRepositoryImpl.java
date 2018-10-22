package com.wkhmedical.repository.jpa.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.taoxeo.repository.HibernateSupport;
import com.taoxeo.repository.JdbcQuery;
import com.wkhmedical.dto.WareRecordBody;
import com.wkhmedical.dto.WareRecordDTO;
import com.wkhmedical.po.WareRecord;
import com.wkhmedical.repository.jpa.IWareRecordRepository;
import com.wkhmedical.repository.jpa.WareRecordRepository;
import com.wkhmedical.util.BizUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class WareRecordRepositoryImpl implements IWareRecordRepository {

	@Resource
	HibernateSupport hibernateSupport;
	@Resource
	JdbcQuery jdbcQuery;

	@Resource
	WareRecordRepository wareRecordRepository;

	@Value("#{query.findCount}")
	private String findCount;

	@Override
	public List<WareRecordDTO> findWareRecordList(WareRecordBody paramBody, Pageable pageable) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT wr.*");
		sqlBuf.append(" FROM ware_record wr");
		sqlBuf.append(" LEFT JOIN car_info ci ON ci.id=wr.cid");
		sqlBuf.append(" WHERE wr.delFlag = 0");
		BizUtil.setSqlJoin(paramBody, "id", sqlBuf, paramList, " AND wr.id = ?");
		BizUtil.setSqlJoin(paramBody, "eid", sqlBuf, paramList, " AND ci.eid = ?");
		BizUtil.setSqlJoin(paramBody, "excRes", sqlBuf, paramList, " AND wr.excRes = ?");
		BizUtil.setSqlJoin(paramBody, "excType", sqlBuf, paramList, " AND wr.excType = ?");
		BizUtil.setSqlJoin(paramBody, "excDate", sqlBuf, paramList, " AND wr.excDate = ?");
		BizUtil.setSqlJoin(paramBody, "excDateMax", sqlBuf, paramList, " AND wr.excDate <= ?");
		BizUtil.setSqlJoin(paramBody, "excDateMin", sqlBuf, paramList, " AND wr.excDate >= ?");
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY wr.insTime DESC");
		sqlBuf.append(sqlOrder);
		//
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		return hibernateSupport.findByNativeSql(WareRecordDTO.class, sqlBuf.toString(), paramList.toArray(), page * size, size);
	}

	@Override
	public Page<WareRecord> findPgWareRecord(WareRecordBody paramBody, Pageable pageable) {
		// SQL主语句
		String sql = "SELECT * FROM ware_record";
		String sqlCount = "SELECT COUNT(1) FROM ware_record";
		// 组装where语句
		List<Object> objList = new ArrayList<Object>();
		StringBuffer sqlWhere = new StringBuffer(" WHERE delFlag = 0");
		if (paramBody != null) {
			List<String> sqlStrList = new ArrayList<String>();
			sqlStrList.add(" AND id = ?");
			BizUtil.setSqlWhere(paramBody, "id", sqlWhere, objList, sqlStrList);
		}
		//
		Sort sort = pageable.getSort();
		String[] fnamesArr = new String[] {};
		String[] onamesArr = new String[] {};
		String sqlOrder = BizUtil.getSqlOrder(sort, fnamesArr, onamesArr, " ORDER BY id DESC");
		return wareRecordRepository.findPageByNativeSql(sql + sqlWhere + sqlOrder, sqlCount + sqlWhere, objList.toArray(), pageable);
	}

	@Override
	public WareRecord findByKey(String id) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sqlBuf = new StringBuffer("");
		sqlBuf.append(" SELECT *");
		sqlBuf.append(" FROM ware_record");
		sqlBuf.append(" WHERE id = ?");
		paramList.add(id);

		List<WareRecord> lstWareRecord = hibernateSupport.findByNativeSql(WareRecord.class, sqlBuf.toString(), paramList.toArray());
		if (lstWareRecord != null && lstWareRecord.size() > 0) {
			return lstWareRecord.get(0);
		}
		return null;
	}

	@Override
	public Integer findCount(String id) {
		@SuppressWarnings("rawtypes")
		List<Map> count = jdbcQuery.find(findCount, Map.class, id);
		log.info("jdbcQuery测试" + count.get(0));
		return 0;
	}

}
