/**
 * 
 */
package com.wkhmedical.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
public class SearchSqlParam {

	private String sql;
	private List<Object> paramList;
	private String countSql;
}
