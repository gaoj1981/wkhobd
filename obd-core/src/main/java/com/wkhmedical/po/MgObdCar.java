package com.wkhmedical.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_car")
public class MgObdCar {

	/**
	 * 主Key
	 */
	@Id
	private String id;

	@CreatedDate
	private Date insTime;

	/**
	 * 广告标识符，只支持单个查询
	 */
	private Integer adId;

	/**
	 * 设备编号
	 */
	@Indexed
	private String deviceNumber;

	/**
	 * 电瓶电压 V
	 */
	@Column(name = "bv")
	private Double batteryvoltage;
	/**
	 * 总里程 KM
	 */
	@Column(name = "totalMile")
	private Double totalMileage;
	/**
	 * 总油耗量 ML
	 */
	@Column(name = "totalGas")
	private Integer totalGasUsed;
	/**
	 * 百公里油耗 L
	 */
	@Column(name = "baiGas")
	private Double averageGasUsed;
	/**
	 * 发动机转速
	 */
	@Column(name = "egSpeed")
	private Integer engineTurnSpeed;
	/**
	 * 车辆速度 KM/H
	 */
	@Column(name = "curSpeed")
	private Integer vehicleSpeed;
	/**
	 * 空气流量
	 */
	@Column(name = "airFlow")
	private Integer airflow;
	/**
	 * 进气口温度 °
	 */
	@Column(name = "airTemp")
	private Integer airDoorTemperature;
	/**
	 * 故障灯状态 故障/无
	 */
	@Column(name = "errLightStatus")
	private String faultLightStatus;
	/**
	 * 故障灯个数 个
	 */
	@Column(name = "errLigth")
	private Integer faultLigthNumber;
	/**
	 * 故障内容描述 （如：P1641,P1642）
	 */
	@Column(name = "errInfo")
	private String faulContext;
	/**
	 * 冷却液温度 °
	 */
	@Column(name = "coolTemp")
	private Integer coolWaterTemperature;
	/**
	 * 车辆环境温度 °
	 */
	@Column(name = "envTemp")
	private Integer environmentTemperature;
	/**
	 * 本次发动机运行时间 秒
	 */
	@Column(name = "curRunTime")
	private Integer engineRuntime;
	/**
	 * 故障行驶里程 米
	 */
	@Column(name = "errMile")
	private Integer faultRunMileage;
	/**
	 * 剩余油量 %
	 */
	@Column(name = "remainGas")
	private Integer remainingGasValue;
	/**
	 * 发动机负荷 %
	 */
	@Column(name = "egPress")
	private Integer enginePressure;
	/**
	 * 汽车仪表总里程 KM
	 */
	@Column(name = "dashTotalMile")
	private Double dashboardTotalMileage;
	/**
	 * 车辆总运行时间 （秒）
	 */
	@Column(name = "runTime")
	private Integer carRunningTime;
	/**
	 * 设备上传数据时间（年月日时分秒）
	 */
	@Column(name = "dcTime")
	private String deviceCreatTime;
	/**
	 * ACC状态 0关闭 1打开
	 */
	private Integer accStatus;
	/**
	 * 本次行驶里程 KM
	 */
	@Column(name = "curDistance")
	private Double oneTimeDriveDistance;
	/**
	 * 本次行驶油耗 L
	 */
	@Column(name = "curGasTotal")
	private Double oneTimeDirveGasTotal;
	/**
	 * 本次行驶平均速度 KM/时
	 */
	@Column(name = "avSpeed")
	private Double averageSpeed;
	/**
	 * 位置经度
	 */
	private BigDecimal lng;
	/**
	 * 位置纬度
	 */
	private BigDecimal lat;
}
