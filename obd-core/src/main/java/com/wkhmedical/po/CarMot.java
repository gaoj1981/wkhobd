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
@Table(name = "car_mot")
@DynamicInsert
@DynamicUpdate
public class CarMot implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String eid;

	private Date motDate;

	private Date expDate;

	private String dealLtd;

	private String motImgs;

	private Integer delFlag;

	@Column(insertable = false, updatable = false)
	private Date insTime;

	@Column(insertable = false, updatable = false)
	private Date updTime;
}