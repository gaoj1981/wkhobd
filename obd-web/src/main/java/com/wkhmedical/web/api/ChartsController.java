/**
 * 
 */
package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.service.CarInfoService;
import com.wkhmedical.service.ObdLicService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

/**
 * @author Administrator
 */
@Log4j2
@RestController
@Api(tags = "车辆接口")
@RequestMapping("/api/charts")
public class ChartsController {

	@Autowired
	CarInfoService carInfoService;

	@Autowired
	ObdLicService obdLicService;

	/**
	 * 车辆图表汇总
	 * 
	 * @return
	 */
	@ApiOperation(value = "车辆图表汇总")
	@PostMapping("/car.group")
	public List<ChartCarDTO> getCountSum(@ApiParam(value = "Group类型", required = true) @RequestParam Integer groupType) {
		log.info("Car GroupByProv Chart");
		return carInfoService.getChartCarList(groupType);
	}

	@ApiOperation(value = "车辆体检汇总")
	@PostMapping("/car.check")
	public Long getCheckSum(@RequestBody @Valid DeviceCheckSumBody paramBody) {
		return obdLicService.getCheckSum(paramBody);
	}

}
