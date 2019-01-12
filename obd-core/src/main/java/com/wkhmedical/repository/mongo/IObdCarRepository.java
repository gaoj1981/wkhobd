package com.wkhmedical.repository.mongo;

import java.math.BigDecimal;
import java.util.Date;

import com.wkhmedical.po.MgObdCar;

public interface IObdCarRepository {

	MgObdCar findObdExample(String deviceNumber);

	BigDecimal findObdDayDis(String deviceNumber, Date dt, int mmFlag);
}
