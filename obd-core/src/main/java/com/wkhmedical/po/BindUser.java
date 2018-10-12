package com.wkhmedical.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bind_user")
@DynamicInsert
@DynamicUpdate
public class BindUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8212087115733357084L;

	@Id
	private String id;

	private Integer areaId;
	
	private Integer utype;

	private String uname;

	private String job;

	private String tel;

	private String urName;

	private String urTel;

	private Integer isDefault;
	
	private Integer delFlag;
}
