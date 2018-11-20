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
import com.wkhmedical.dto.UserExinfoBody;
import com.wkhmedical.dto.UserExinfoDTO;
import com.wkhmedical.dto.ValiAdd;
import com.wkhmedical.dto.ValiEdit;
import com.wkhmedical.po.UserExinfo;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.UserExinfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "用户扩展信息接口")
@RequestMapping("/api/userexinfo")
public class UserExinfoController {

	@Autowired
	UserExinfoService userExinfoService;

	@ApiOperation(value = "获取用户扩展信息基本信息")
	@GetMapping("/get")
	public UserExinfo getUserExinfo(@ApiParam(value = "id主Key", required = true) @RequestParam String id) {
		UserExinfoBody paramBody = new UserExinfoBody();
		paramBody.setId(id);
		return userExinfoService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取用户扩展信息分页列表")
	@PostMapping("/get.list")
	public List<UserExinfoDTO> getUserExinfoData(@RequestBody @Valid Paging<UserExinfoBody> paramBody) {
		return userExinfoService.getList(paramBody);
	}

	@ApiOperation(value = "获取用户扩展信息分页对象")
	@PostMapping("/get.page")
	public Page<UserExinfo> getUserExinfoPage(@RequestBody @Valid Paging<UserExinfoBody> paramBody) {
		return userExinfoService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加用户扩展信息")
	@PostMapping("/add")
	public void userExinfoAdd(@RequestBody @Validated({ValiAdd.class}) UserExinfoBody paramBody, @CurrentUser TUserDetails user) {
		userExinfoService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改用户扩展信息")
	@PostMapping("/edit")
	public void userExinfoEdit(@RequestBody @Validated({ValiEdit.class}) UserExinfoBody paramBody) {
		userExinfoService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除用户扩展信息")
	@DeleteMapping("/delete")
	public boolean userExinfoDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		userExinfoService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删用户扩展信息")
	@DeleteMapping("/del")
	public boolean userExinfoDel(@ApiParam(value = "id主KEY", required = true) @RequestParam String id) {
		userExinfoService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
	
	@ApiOperation(value = "获取用户扩展信息总数")
	@GetMapping("/count.sum")
	public Long getCountSum() {
		return userExinfoService.getCountSum();
	}
	
}
