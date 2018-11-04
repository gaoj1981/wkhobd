package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.EquipDetailDTO;
import com.wkhmedical.dto.EquipInfoBody;
import com.wkhmedical.dto.EquipInfoDTO;
import com.wkhmedical.dto.ValiAdd;
import com.wkhmedical.dto.ValiEdit;
import com.wkhmedical.po.EquipInfo;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.EquipInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "设备详情接口")
@RequestMapping("/api/equipinfo")
public class EquipInfoController {

	@Autowired
	EquipInfoService equipInfoService;

	@ApiOperation(value = "获取设备详情基本信息")
	@GetMapping("/get")
	public EquipInfo getEquipInfo(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		EquipInfoBody paramBody = new EquipInfoBody();
		paramBody.setId(id);
		return equipInfoService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取设备详情分页列表")
	@PostMapping("/get.list")
	public List<EquipInfoDTO> getEquipInfoData(@RequestBody @Valid Paging<EquipInfoBody> paramBody) {
		return equipInfoService.getList(paramBody);
	}

	@ApiOperation(value = "获取设备详情分页对象")
	@PostMapping("/get.page")
	public Page<EquipInfo> getEquipInfoPage(@RequestBody @Valid Paging<EquipInfoBody> paramBody) {
		return equipInfoService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加设备详情")
	@PostMapping("/add")
	public void equipInfoAdd(@RequestBody @Validated({ ValiAdd.class }) EquipInfoBody paramBody, @CurrentUser TUserDetails user) {
		equipInfoService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改设备详情")
	@PostMapping("/edit")
	public void equipInfoEdit(@RequestBody @Validated({ ValiEdit.class }) EquipInfoBody paramBody) {
		equipInfoService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除设备详情")
	@DeleteMapping("/delete")
	public boolean equipInfoDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		equipInfoService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "批量删除选中设备")
	@DeleteMapping("/delete.batch")
	public boolean equipInfoDeleteBatch(@ApiParam(value = "id主KEY群（逗号分隔）", required = true) @RequestParam String ids) {
		equipInfoService.deleteBatch(ids);
		return true;
	}

	@ApiOperation(value = "逻辑删设备详情")
	@DeleteMapping("/del")
	public boolean equipInfoDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		equipInfoService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}

	@ApiOperation(value = "获取设备详情总数")
	@GetMapping("/count.sum")
	public Long getCountSum() {
		return equipInfoService.getCountSum();
	}

	@ApiOperation(value = "获取设备列表详情信息")
	@GetMapping("/get.detail")
	public EquipDetailDTO getEquipDetail(@ApiParam(name = "eid", value = "车辆ID", required = true) @RequestParam String eid) {
		return equipInfoService.getDetail(eid);
	}

}
