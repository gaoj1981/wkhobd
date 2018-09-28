package com.wkhmedical.po;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "car_insur")
@DynamicInsert
@DynamicUpdate
public class CarInsur implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private Long cid;
	
	private String insurNum;
	
	private String insurLtd;
	
	private String servTel;
	
	private Date effectDate;
	
	private Date expDate;
	
	private Integer insurType;
	
	private String salesName;
	
	private String salesTel;
	
	private String insurImgs;
	
	private Integer delFlag;
	
}