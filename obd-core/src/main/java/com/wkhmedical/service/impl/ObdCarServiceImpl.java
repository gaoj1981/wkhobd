package com.wkhmedical.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.taoxeo.lang.BeanUtils;
import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.dto.CarSendDTO;
import com.wkhmedical.dto.ObdCarDTO;
import com.wkhmedical.po.CarInfo;
import com.wkhmedical.po.MgObdCar;
import com.wkhmedical.repository.jpa.CarInfoRepository;
import com.wkhmedical.repository.mongo.ObdCarRepository;
import com.wkhmedical.service.ObdCarService;
import com.wkhmedical.util.AssistUtil;
import com.wkhmedical.util.MapGPSUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ObdCarServiceImpl implements ObdCarService {

	@Resource
	ObdCarRepository obdCarRepository;
	@Resource
	CarInfoRepository carInfoRepository;

	@Override
	public void saveObdCar(CarSendDTO carSend) {
		String deviceNumber = carSend.getDeviceNumber();
		if (deviceNumber == null) {
			log.error("OBD推送无deviceNumber");
			throw new BizRuntimeException("obdcar_not_devicenumber");
		}
		MgObdCar obdCar = new MgObdCar();
		BeanUtils.merageProperty(obdCar, carSend);
		obdCarRepository.save(obdCar);
	}

	@Override
	public ObdCarDTO getObdCar(String eid) {
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		String deviceNumber = carInfo.getDeviceNumber();
		if (StringUtils.isBlank(deviceNumber)) {
			throw new BizRuntimeException("obdcar_not_related", eid);
		}
		//
		List<MgObdCar> ObdCarList = obdCarRepository.findTopByDeviceNumberOrderByInsTimeDesc(carInfo.getDeviceNumber());
		if (ObdCarList == null || ObdCarList.size() == 0) {
			throw new BizRuntimeException("obdcar_devicenumber_not_exists", carInfo.getDeviceNumber());
		}
		else {
			MgObdCar obdCar = ObdCarList.get(0);
			ObdCarDTO obdCarDTO = AssistUtil.coverBean(obdCar, ObdCarDTO.class);
			obdCarDTO.setAreaId(carInfo.getAreaId());
			obdCarDTO.setEid(carInfo.getEid());
			obdCarDTO.setCarName(carInfo.getCarName());
			// 地图坐标纠偏
			BigDecimal lat = obdCarDTO.getLat();
			BigDecimal lng = obdCarDTO.getLng();
			BigDecimal[] bdArr = MapGPSUtil.Transform(lat.doubleValue(), lng.doubleValue());
			if (bdArr != null && bdArr.length == 2) {
				obdCarDTO.setLat(bdArr[0]);
				obdCarDTO.setLng(bdArr[1]);
			}
			return obdCarDTO;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdCarService#getObdCarPage(java.lang.String)
	 */
	@Override
	public List<ObdCarDTO> getObdCarList(String eid) {
		List<ObdCarDTO> rtnList = new ArrayList<ObdCarDTO>();
		//
		CarInfo carInfo = carInfoRepository.findByEid(eid);
		if (carInfo == null) {
			throw new BizRuntimeException("carinfo_not_exists", eid);
		}
		String deviceNumber = carInfo.getDeviceNumber();
		if (StringUtils.isBlank(deviceNumber)) {
			throw new BizRuntimeException("obdcar_not_related", eid);
		}
		// 获取最大批次号
		MgObdCar obdCar = obdCarRepository.findTopByDeviceNumberOrderByAccOpenTimeDesc(deviceNumber);
		if (obdCar == null) {
			throw new BizRuntimeException("obdcar_eid_data_not_exists", eid);
		}
		String accOpenTime = obdCar.getAccOpenTime();
		// 获取最近记录
		if (accOpenTime == null) {
			return rtnList;
		}
		List<MgObdCar> lstObd = obdCarRepository.findByDeviceNumberAndAccOpenTimeOrderByRecordCountAsc(deviceNumber, accOpenTime);
		// 处理MgObdCar->ObdCarDTO
		ObdCarDTO obdCarDTO = new ObdCarDTO();
		for (MgObdCar obdTmp : lstObd) {
			obdCarDTO = new ObdCarDTO();
			BeanUtils.merageProperty(obdCarDTO, obdTmp);
			rtnList.add(obdCarDTO);
		}
		return rtnList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wkhmedical.service.ObdCarService#getObdCarPage(java.lang.String)
	 */
	@Override
	public Page<ObdCarDTO> getObdCarPage(String eid) {
		// TODO Auto-generated method stub
		return null;
	}

}
