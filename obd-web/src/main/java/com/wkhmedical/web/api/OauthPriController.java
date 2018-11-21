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
import com.wkhmedical.dto.OauthPriBody;
import com.wkhmedical.dto.OauthPriDTO;
import com.wkhmedical.dto.ValiAdd;
import com.wkhmedical.dto.ValiEdit;
import com.wkhmedical.po.OauthPri;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.OauthPriService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "权限路径信息接口")
@RequestMapping("/api/oauthpri")
public class OauthPriController {

	@Autowired
	OauthPriService oauthPriService;

	@ApiOperation(value = "获取权限路径信息基本信息")
	@GetMapping("/get")
	public OauthPri getOauthPri(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		OauthPriBody paramBody = new OauthPriBody();
		paramBody.setId(id);
		return oauthPriService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取权限路径信息分页列表")
	@PostMapping("/get.list")
	public List<OauthPriDTO> getOauthPriData(@RequestBody @Valid Paging<OauthPriBody> paramBody) {
		return oauthPriService.getList(paramBody);
	}

	@ApiOperation(value = "获取权限路径信息分页对象")
	@PostMapping("/get.page")
	public Page<OauthPri> getOauthPriPage(@RequestBody @Valid Paging<OauthPriBody> paramBody) {
		return oauthPriService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加权限路径信息")
	@PostMapping("/add")
	public void oauthPriAdd(@RequestBody @Validated({ValiAdd.class}) OauthPriBody paramBody, @CurrentUser TUserDetails user) {
		oauthPriService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改权限路径信息")
	@PostMapping("/edit")
	public void oauthPriEdit(@RequestBody @Validated({ValiEdit.class}) OauthPriBody paramBody) {
		oauthPriService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除权限路径信息")
	@DeleteMapping("/delete")
	public boolean oauthPriDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		oauthPriService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删权限路径信息")
	@DeleteMapping("/del")
	public boolean oauthPriDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		oauthPriService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
	
	@ApiOperation(value = "获取权限路径信息总数")
	@GetMapping("/count.sum")
	public Long getCountSum() {
		return oauthPriService.getCountSum();
	}
	
}
