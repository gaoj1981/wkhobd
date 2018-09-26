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

	/**
	 * 主Key
	 */
	@Id
	private String id;

	/**
	 * 广告标识符，只支持单个查询
	 */
	private Integer adId;

	/**
	 * 车辆ID，对应设备用
	 */
	@Indexed
	private String eid;

	/**
	 * 设备编号
	 */
	@Indexed(unique = true)
	private String deviceNumber;

	/**
	 * 电瓶电压  V
	 */
	private Double batteryvoltage;
	/**
	 * 总里程  KM
	 */
	private Double totalMileage;
	/**
	 * 总油耗量 ML
	 */
	private Integer totalGasUsed;
	/**
	 * 百公里油耗 L
	 */
	private Double averageGasUsed;
	/**
	 * 发动机转速 
	 */
	private Integer engineTurnSpeed;
	/**
	 * 车辆速度  KM/H
	 */
	private Integer vehicleSpeed;
	/**
	 * 空气流量
	 */
	private Integer airflow;
	/**
	 * 进气口温度 °
	 */
	private Integer airDoorTemperature;
	/**
	 * 故障灯状态 故障/无
	 */
	private String faultLightStatus;
	/**
	 * 故障灯个数 个
	 */
	private Integer faultLigthNumber;
	/**
	 * 故障内容描述 （如：P1641,P1642）
	 */
	private String faulContext;
	/**
	 * 冷却液温度 °
	 */
	private Integer coolWaterTemperature;
	/**
	 * 车辆环境温度 °
	 */
	private Integer environmentTemperature;
	/**
	 * 本次发动机运行时间 秒
	 */
	private Integer engineRuntime;
	/**
	 * 故障行驶里程 米
	 */
	private Integer faultRunMileage;
	/**
	 * 剩余油量 %
	 */
	private Integer remainingGasValue;
	/**
	 * 发动机负荷 %
	 */
	private Integer enginePressure;
	/**
	 * 汽车仪表总里程 KM
	 */
	private Double dashboardTotalMileage;
	/**
	 * 车辆总运行时间 （秒）
	 */
	private Integer carRunningTime;
	/**
	 * 设备上传数据时间（年月日时分秒）
	 */
	private String deviceCreatTime;
	/**
	 * ACC状态 0关闭 1打开
	 */
	private Integer accStatus;
	/**
	 * 本次行驶里程 KM
	 */
	private Double oneTimeDriveDistance;
	/**
	 * 本次行驶油耗 L
	 */
	private Double oneTimeDirveGasTotal;
	/**
	 * 本次行驶平均速度  KM/时
	 */
	private Double averageSpeed;

}
