package com.wkhmedical.po;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "obd_car")
@CompoundIndexes({ @CompoundIndex(name = "idx_deviceNumber_act", def = "{'deviceNumber': 1, 'act': -1}") })
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
	@Field("bv")
	private Double batteryvoltage;
	/**
	 * 总里程 KM
	 */
	@Field("totalMile")
	private Double totalMileage;
	/**
	 * 总油耗量 ML
	 */
	@Field("totalGas")
	private Integer totalGasUsed;
	/**
	 * 百公里油耗 L
	 */
	@Field("baiGas")
	private Double averageGasUsed;
	/**
	 * 发动机转速
	 */
	@Field("egSpeed")
	private Integer engineTurnSpeed;
	/**
	 * 车辆速度 KM/H
	 */
	@Field("curSpeed")
	private Integer vehicleSpeed;
	/**
	 * 空气流量
	 */
	@Field("airFlow")
	private Integer airflow;
	/**
	 * 进气口温度 °
	 */
	@Field("airTemp")
	private Integer airDoorTemperature;
	/**
	 * 故障灯状态 故障/无
	 */
	@Field("errLightStatus")
	private String faultLightStatus;
	/**
	 * 故障灯个数 个
	 */
	@Field("errLigth")
	private Integer faultLigthNumber;
	/**
	 * 故障内容描述 （如：P1641,P1642）
	 */
	@Field("errInfo")
	private String faulContext;
	/**
	 * 冷却液温度 °
	 */
	@Field("coolTemp")
	private Integer coolWaterTemperature;
	/**
	 * 车辆环境温度 °
	 */
	@Field("envTemp")
	private Integer environmentTemperature;
	/**
	 * 本次发动机运行时间 秒
	 */
	@Field("curRunTime")
	private Integer engineRuntime;
	/**
	 * 故障行驶里程 米
	 */
	@Field("errMile")
	private Integer faultRunMileage;
	/**
	 * 剩余油量 %
	 */
	@Field("remainGas")
	private Integer remainingGasValue;
	/**
	 * 发动机负荷 %
	 */
	@Field("egPress")
	private Integer enginePressure;
	/**
	 * 汽车仪表总里程 KM
	 */
	@Field("dashTotalMile")
	private Double dashboardTotalMileage;
	/**
	 * 车辆总运行时间 （秒）
	 */
	@Field("runTime")
	private Integer carRunningTime;
	/**
	 * 设备上传数据时间（年月日时分秒）
	 */
	@Field("dcTime")
	private String deviceCreatTime;
	/**
	 * ACC状态 0关闭 1打开
	 */
	private Integer accStatus;
	/**
	 * 本次行驶里程 KM
	 */
	@Field("curDistance")
	private Double oneTimeDriveDistance;
	/**
	 * 本次行驶油耗 L
	 */
	@Field("curGasTotal")
	private Double oneTimeDirveGasTotal;
	/**
	 * 本次行驶平均速度 KM/时
	 */
	@Field("avSpeed")
	private Double averageSpeed;
	/**
	 * 位置经度
	 */
	private BigDecimal lng;
	/**
	 * 位置纬度
	 */
	private BigDecimal lat;

	@Field("rc")
	private Integer recordCount;

	@Field("act")
	private String accOpenTime;
}
