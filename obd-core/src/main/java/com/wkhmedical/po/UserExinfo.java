package com.wkhmedical.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "user_exinfo")
@DynamicInsert
@DynamicUpdate
public class UserExinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String nick;
	private String note;
	private String country;
	private Integer provId;
	private Integer cityId;
	private String addr;
	private String tel;
	private String avatar;
	private String dept;
	private String office;
	private String email;
	private String signature;
	private String labels;
	private Integer delFlag;
	@Column(insertable = false, updatable = false)
	private Date insTime;
	@Column(insertable = false, updatable = false)
	private Date updTime;
}