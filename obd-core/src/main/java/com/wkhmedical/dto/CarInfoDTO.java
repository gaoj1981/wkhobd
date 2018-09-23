package com.wkhmedical.dto;

import java.io.Serializable;

import com.wkhmedical.constant.FuelType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoDTO implements Serializable {

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

	private FuelType fuelType;

	private Long prinId;

	private String prinName;

	private String prinJob;

	private String prinTel;

	private String prinUrName;

	private String prinUrTel;

	private Long maintId;

	private String maintName;

	private String maintTel;

	private String maintUrName;

	private String maintUrTel;

	private Integer provId;

	private Integer cityId;

	private Integer areaId;
}
