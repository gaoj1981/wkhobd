package com.wkhmedical.repository.mongo;

import com.wkhmedical.po.MgObdCar;

public interface IObdCarRepository {

	MgObdCar findObdExample(String deviceNumber);
}
