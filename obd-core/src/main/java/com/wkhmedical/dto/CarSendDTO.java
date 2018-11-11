package com.wkhmedical.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarSendDTO {

	private Integer id;
	private String deviceNumber;
	private Double batteryvoltage;
	private Double totalMileage;
	private Integer totalGasUsed;
	private Double averageGasUsed;
	private Integer engineTurnSpeed;
	private Integer vehicleSpeed;
	private Integer airflow;
	private Integer airDoorTemperature;
	private String faultLightStatus;
	private Integer faultLigthNumber;
	private String faulContext;
	private Integer coolWaterTemperature;
	private Integer environmentTemperature;
	private Integer engineRuntime;
	private Integer faultRunMileage;
	private Integer remainingGasValue;
	private Integer enginePressure;
	private Double dashboardTotalMileage;
	private Integer carRunningTime;
	private String deviceCreatTime;
	private Integer accStatus;
	private Double oneTimeDriveDistance;
	private Double oneTimeDirveGasTotal;
	private Double averageSpeed;
	private BigDecimal lng;
	private BigDecimal lat;

	private String eid;
}
