/**
 * 
 */
package com.wkhmedical.web.api;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.AreaCarDTO;
import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.MonthAvgExamDTO;
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

	@ApiOperation(value = "车辆区域覆盖汇总")
	@PostMapping("/car.area.num")
	public AreaCarDTO getAreaCar(@RequestBody @Valid AreaCarBody paramBody) {
		return carInfoService.getAreaCar(paramBody);
	}

	@ApiOperation(value = "车辆月出车率")
	@PostMapping("/car.month.rate")
	public BigDecimal getCarMonthRate(@RequestBody @Valid AreaCarBody paramBody) {
		return carInfoService.getCarMonthRate(paramBody);
	}

	@ApiOperation(value = "检测异常百分比")
	@PostMapping("/check.exp.rate")
	public BigDecimal checkExpRate(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCheckExpRate(paramBody);
	}

	@ApiOperation(value = "月平均体检人数")
	@PostMapping("/check.month.avg")
	public MonthAvgExamDTO checkMonthAvg(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCheckMonthAvg(paramBody);
	}

}
