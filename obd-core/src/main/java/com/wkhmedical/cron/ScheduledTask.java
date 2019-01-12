package com.wkhmedical.cron;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wkhmedical.service.ObdLicService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ScheduledTask {

	@Resource
	ObdLicService obdLicService;

	@Scheduled(cron = "0 0/5 23 * * ?")
	public void scheduled() {
		log.info("使用Cron：It's time to rest!", System.currentTimeMillis());
	}

	@Scheduled(cron = "0 0/2 01,02 * * ?")
	public void qzCheckTime() {
		obdLicService.qzCheckTime();
	}

	@Scheduled(cron = "0 0/2 03,04,05 * * ?")
	public void qzDeviceTimeRate() {
		obdLicService.qzDeviceTimeRate();
	}

	@Scheduled(cron = "0 12 23 * * ?")
	public void qzMonthSum() {
		obdLicService.qzMonthSum();
	}

}
