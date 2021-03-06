package com.wkhmedical.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wkhmedical.constant.Gender;

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

	private Long areaId;

	private Integer utype;

	private String uname;

	@Enumerated(EnumType.STRING)
	private Gender sex;

	private String ltd;

	private String job;

	private String tel;

	private String urName;

	private String urTel;

	private Integer isDefault;

	private Integer delFlag;

	@Column(insertable = false, updatable = false)
	private Date insTime;

	@Column(insertable = false, updatable = false)
	private Date updTime;
}
