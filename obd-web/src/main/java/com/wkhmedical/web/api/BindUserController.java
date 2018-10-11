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
import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.BindUserDTO;
import com.wkhmedical.dto.BindUserBody;
import com.wkhmedical.dto.BindUserParam;
import com.wkhmedical.dto.BindUserAddBody;
import com.wkhmedical.dto.BindUserEditBody;
import com.wkhmedical.po.BindUser;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.BindUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@Api(tags = "相关人员接口")
@RequestMapping("/api/binduser")
public class BindUserController {

	@Autowired
	BindUserService bindUserService;

	// @ApiOperation(value = "添加相关人员")
	@PostMapping("/add")
	public void addBu(@CurrentUser TUserDetails user, @RequestBody @Valid BindUserAddBody addBody) {
		bindUserService.addInfo(addBody);
	}

	@ApiOperation(value = "获取相关人员基本信息")
	@GetMapping("/get")
	public BindUser getBindUser(@ApiParam(value = "人员ID", required = true) @RequestParam Long id) {
		BindUserParam paramBody = new BindUserParam();
		paramBody.setId(id);
		return bindUserService.getInfo(paramBody);
	}

	@ApiOperation(value = "获取相关人员分页列表（APP用）")
	@PostMapping("/get.list")
	public List<BindUserDTO> getBindUserData(@RequestBody @Valid Paging<BindUserBody> paramBody) {
		return bindUserService.getList(paramBody);
	}

	@ApiOperation(value = "获取相关人员分页对象")
	@PostMapping("/get.page")
	public Page<BindUser> getBindUserPage(@RequestBody @Valid Paging<BindUserBody> paramBody) {
		return bindUserService.getPgList(paramBody);
	}

	@ApiOperation(value = "删除相关人员")
	@DeleteMapping("/delete")
	public boolean bindUserDelete(@ApiParam(value = "id主KEY", required = true) @RequestParam Long id) {
		bindUserService.deleteInfo(id);
		return true;
	}

	@ApiOperation(value = "修改相关人员")
	@PostMapping("/edit")
	public void editBindUser(@RequestBody @Valid BindUserEditBody editBody, @CurrentUser TUserDetails user) {
		bindUserService.updateInfo(editBody);
	}

	@ApiOperation(value = "逻辑删相关人员")
	@DeleteMapping("/del")
	public boolean bindUserDel(@ApiParam(value = "id主KEY", required = true) @RequestParam Long id) {
		bindUserService.delInfo(id);
		log.info("非物理删除。id：" + id);
		return true;
	}
}
