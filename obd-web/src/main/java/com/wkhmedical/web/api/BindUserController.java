package com.wkhmedical.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserPage;
import com.wkhmedical.dto.BindUserParam;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.BindUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "绑定人员接口")
@RequestMapping("/api/binduser")
public class BindUserController {

	@Autowired
	BindUserService bindUserService;

	@ApiOperation(value = "获取绑定人员基本信息")
	@GetMapping("/get")
	public BindUser getBindUser(@ApiParam(value = "人员ID", required = true) @RequestParam Long id) {
		BindUserParam paramBody = new BindUserParam();
		paramBody.setId(id);
		return bindUserService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取绑定人员分页列表")
	@PostMapping("/get.list")
	public List<BindUserDTO> getBindUserData(@RequestBody @Valid BindUserPage paramBody) {
		return bindUserService.getList(paramBody);
	}

	@ApiOperation(value = "获取绑定人员分页对象")
	@PostMapping("/get.page")
	public Page<BindUser> getBindUserPage(@RequestBody @Valid BindUserPage paramBody) {
		return bindUserService.getPgList(paramBody);
	}

	@ApiOperation(value = "添加绑定人员")
	@PostMapping("/add")
	public void bindUserAdd(@RequestBody @Valid BindUserBody paramBody, @CurrentUser TUserDetails user) {
		bindUserService.addInfo(paramBody);
	}

	@ApiOperation(value = "修改绑定人员")
	@PostMapping("/edit")
	public void bindUserEdit(@RequestBody @Valid BindUserBody paramBody) {
		bindUserService.updateInfo(paramBody);
	}

	@ApiOperation(value = "删除绑定人员")
	@DeleteMapping("/delete")
	public boolean bindUserDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam Long id) {
		bindUserService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "逻辑删绑定人员")
	@DeleteMapping("/del")
	public boolean bindUserDel(@ApiParam(value = "id主KEY", required = true) @RequestParam Long id) {
		bindUserService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
}