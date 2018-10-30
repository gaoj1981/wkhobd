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
@Table(name = "equip_info")
@DynamicInsert
@DynamicUpdate
public class EquipInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String eid;
	private Integer type;
	private String equipId;
	private String name;
	private String bhNum;
	private String xhNum;
	private String factoryId;
	private String factory;
	private Date birthDate;
	private String version;
	private Long countNum;
	private String note;
	private Integer delFlag;
	@Column(insertable = false, updatable = false)
	private Date insTime;
	@Column(insertable = false, updatable = false)
	private Date updTime;
}