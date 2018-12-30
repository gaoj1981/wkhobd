package com.wkhmedical.web.api;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.taoxeo.lang.BeanUtils;
import com.wkhmedical.dto.CarInfoParam;
import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.message.Message;
import com.wkhmedical.message.event.SendEvent;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.DeviceTimeTemp;
import com.wkhmedical.repository.jpa.DeviceTimeTempRepository;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.service.ObdCarService;
import com.wkhmedical.util.BizUtil;
import com.wkhmedical.util.DateUtil;
import com.wkhmedical.util.MapGPSUtil;
import com.wkhmedical.util.SnowflakeIdWorker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "OBD接口")
@RequestMapping("/obd")
public class ObdController {

	@Autowired
	ObdCarService obdCarService;

	@Autowired
	CarInfoService carInfoService;

	/** The messaging template. */
	@Resource
	SimpMessagingTemplate messagingTemplate;

	/** The application context. */
	@Resource
	ApplicationContext applicationContext;

	@Resource
	DeviceTimeTempRepository deviceTimeTempRepository;

	@ApiOperation(value = "OBD推送实时车辆信息")
	@PostMapping("/receiveData.json")
	public void receiveData(@RequestBody @Valid String carStr) {
		log.info("============BEGIN==============");
		log.info("OBD请求数据：" + carStr);
		carStr = carStr.replaceAll("=", ":'").replaceAll("&", "',");
		CarSendDTO obdSend = JSON.parseObject("{" + carStr + "'}", CarSendDTO.class);
		obdCarService.saveObdCar(obdSend);
		log.info("============END==============");
		// 业务其它处理
		CarInfo carInfo = null;
		try {
			CarInfoParam paramBody = new CarInfoParam();
			paramBody.setDeviceNumber(obdSend.getDeviceNumber());
			carInfo = carInfoService.getCarInfo(paramBody);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// 执行ws推送
		try {
			// 是否推送数据到前端
			if (carInfo != null) {
				// 推送
				ObdCarDTO obdCar = new ObdCarDTO();
				BeanUtils.merageProperty(obdCar, carInfo);
				BeanUtils.merageProperty(obdCar, obdSend);
				String eid = carInfo.getEid();
				obdSend.setEid(carInfo.getEid());
				BigDecimal lat = obdCar.getLat();
				BigDecimal lng = obdCar.getLng();
				BigDecimal[] bdArr = MapGPSUtil.Transform(lat.doubleValue(), lng.doubleValue());
				if (bdArr != null && bdArr.length == 2) {
					obdCar.setLat(bdArr[0]);
					obdCar.setLng(bdArr[1]);
				}
				messagingTemplate.convertAndSend("/queue/obd.data." + eid, obdCar);
				Message message = new Message(eid, ObdCarDTO.class.getSimpleName(), obdCar);
				applicationContext.publishEvent(new SendEvent(message));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// 开关机时间入库
		try {
			if (carInfo != null) {
				String eid = carInfo.getEid();
				Date dtNow = new Date();
				Date dtSend = DateUtil.getCurDateByFormat("yyyyMMdd");
				DeviceTimeTemp deviceTimeTemp = deviceTimeTempRepository.findByEidAndDt(eid, dtSend);
				if (deviceTimeTemp == null) {
					//
					SnowflakeIdWorker idWorker = new SnowflakeIdWorker(BizUtil.getDbWorkerId(), BizUtil.getDbDatacenterId());
					//
					deviceTimeTemp = new DeviceTimeTemp();
					deviceTimeTemp.setId(BizUtil.genDbIdStr(idWorker));
					deviceTimeTemp.setEid(eid);
					deviceTimeTemp.setDt(dtSend);
					deviceTimeTemp.setSdt(dtNow);
					deviceTimeTemp.setEdt(dtNow);
					deviceTimeTemp.setFlag(0);
					deviceTimeTempRepository.save(deviceTimeTemp);
				}
				else {
					// 是否需要更改
					boolean isUpd = false;
					Date sdt = deviceTimeTemp.getSdt();
					if (sdt == null || sdt.compareTo(dtNow) > 0) {
						deviceTimeTemp.setSdt(dtNow);
						isUpd = true;
					}
					Date edt = deviceTimeTemp.getEdt();
					if (edt == null || edt.compareTo(dtNow) < 0) {
						deviceTimeTemp.setEdt(dtNow);
						isUpd = true;
					}
					if (isUpd) {
						deviceTimeTempRepository.update(deviceTimeTemp);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
