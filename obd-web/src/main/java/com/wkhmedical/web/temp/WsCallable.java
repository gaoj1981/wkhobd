/**
 * 
 */
package com.wkhmedical.web.temp;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.message.Message;
import com.wkhmedical.message.event.SendEvent;

/**
 * @author Administrator
 */
@Component
public class WsCallable implements Callable<T> {

	/** The messaging template. */
	@Resource
	SimpMessagingTemplate messagingTemplate;

	/** The application context. */
	@Resource
	ApplicationContext applicationContext;

	@Override
	public T call() {
		ObdCarDTO obdCar = new ObdCarDTO();
		Integer[] speedArr = new Integer[] { 20, 50, 77, 99, 188, 220, 110, 65, 11, 88 };
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(150);
				obdCar.setVehicleSpeed(speedArr[random.nextInt(10)]);
				obdCar.setLng(new BigDecimal("119.681336").add(new BigDecimal(i * 0.0001)).setScale(6, BigDecimal.ROUND_HALF_DOWN));
				obdCar.setLat(new BigDecimal("29.691734").add(new BigDecimal(i * 0.0001)).setScale(6, BigDecimal.ROUND_HALF_DOWN));
				obdCar.setRemainingGasValue(29 - i / 100);
				obdCar.setDashboardTotalMileage(
						new BigDecimal(3456.999).add(new BigDecimal(i * 299)).setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue());
				obdCar.setEnvironmentTemperature(28 + random.nextInt(10));
				obdCar.setAirDoorTemperature(78 + random.nextInt(10));
				obdCar.setCoolWaterTemperature(90 + random.nextInt(10));
				messagingTemplate.convertAndSend("/queue/obd.data.11111111", obdCar);
				Message message = new Message("11111111", ObdCarDTO.class.getSimpleName(), obdCar);
				applicationContext.publishEvent(new SendEvent(message));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
