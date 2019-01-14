/**
 * 
 */
package com.wkhmedical.web.api;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.AreaCarBody;
import com.wkhmedical.dto.AreaCarDTO;
import com.wkhmedical.dto.ChartCarDTO;
import com.wkhmedical.dto.CheckItemTotal;
import com.wkhmedical.dto.CheckPeopleTotal;
import com.wkhmedical.dto.CheckTypeTotal;
import com.wkhmedical.dto.DeviceCheckSumBody;
import com.wkhmedical.dto.DeviceTimeBody;
import com.wkhmedical.dto.DeviceTimeDTO;
import com.wkhmedical.dto.DisTotal;
import com.wkhmedical.dto.MonthAvgCarDTO;
import com.wkhmedical.dto.MonthAvgDisDTO;
import com.wkhmedical.dto.MonthAvgExamDTO;
import com.wkhmedical.dto.MonthAvgTimeDTO;
import com.wkhmedical.dto.TimeTotal;
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
@Api(tags = "图表统计接口")
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

	@ApiOperation(value = "车辆体检各项汇总")
	@PostMapping("/car.item.check")
	public CheckItemTotal getCheckItemSum(@RequestBody @Valid DeviceCheckSumBody paramBody) {
		return obdLicService.getCheckItemSum(paramBody);
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

	@ApiOperation(value = "月平均行驶距离")
	@PostMapping("/dis.month.avg")
	public List<MonthAvgDisDTO> disMonthAvg(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getDisMonthAvg(paramBody);
	}

	@ApiOperation(value = "月平均体检人数")
	@PostMapping("/check.month.avg")
	public List<MonthAvgExamDTO> checkMonthAvg(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCheckMonthAvg(paramBody);
	}

	@ApiOperation(value = "月平均运营时长")
	@PostMapping("/time.month.avg")
	public List<MonthAvgTimeDTO> timeMonthAvg(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getTimeMonthAvg(paramBody);
	}

	@ApiOperation(value = "月云巡诊车出车比例")
	@PostMapping("/car.month.avg")
	public List<MonthAvgCarDTO> carMonthAvg(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCarMonthAvg(paramBody);
	}

	@ApiOperation(value = "累积运营时长")
	@PostMapping("/time.total")
	public TimeTotal getTimeTotal(@RequestBody @Valid AreaCarBody paramBody) {
		TimeTotal rtnObj = new TimeTotal();
		Long timeTotal = obdLicService.getTimeTotal(paramBody);
		rtnObj.setOperationDuration(timeTotal);
		return rtnObj;
	}

	@ApiOperation(value = "体检人数统计")
	@PostMapping("/check.people.total")
	public CheckPeopleTotal getCheckPeopleTotal(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCheckPeopleTotal(paramBody);
	}

	@ApiOperation(value = "检测项总数统计")
	@PostMapping("/check.type.total")
	public CheckTypeTotal getCheckTypeTotal(@RequestBody @Valid AreaCarBody paramBody) {
		return obdLicService.getCheckTypeTotal(paramBody);
	}

	@ApiOperation(value = "累积服务里程")
	@PostMapping("/dis.total")
	public DisTotal getDisTotal(@RequestBody @Valid AreaCarBody paramBody) {
		DisTotal rtnObj = new DisTotal();
		BigDecimal disTotal = obdLicService.getDisTotal(paramBody);
		rtnObj.setServiceMileage(disTotal);
		return rtnObj;
	}

	@ApiOperation(value = "获取日汇总分页对象")
	@PostMapping("/get.page")
	public Page<DeviceTimeDTO> getDeviceTimePage(@RequestBody @Valid Paging<DeviceTimeBody> paramBody) {
		return obdLicService.getDeviceTimePage(paramBody);
	}

}
