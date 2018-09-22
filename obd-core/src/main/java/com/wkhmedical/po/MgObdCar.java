package com.wkhmedical.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_car")
public class MgObdCar {

	@Id
	private String id;

	private Integer adId;

	@Indexed(unique = true)
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

}
