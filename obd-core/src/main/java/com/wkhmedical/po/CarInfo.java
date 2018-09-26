package com.wkhmedical.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wkhmedical.constant.FuelType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "car_info")
@DynamicInsert
@DynamicUpdate
public class CarInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7201913572709965510L;

	@Id
	private Long id;

	private String eid;

	private String deviceNumber;

	private String groupId;

	private String carName;

	private Double baiOilUsed;

	private String plateNum;

	private String carModel;

	private String carColor;

	private String carSize;

	private String engineNum;

	private String frameNum;

	private String enginePower;

	@Enumerated(EnumType.STRING)
	private FuelType fuelType;

	private Long prinId;

	private Long maintId;

	private Integer provId;

	private Integer cityId;

	private Integer areaId;

	private Integer delFlag;
}