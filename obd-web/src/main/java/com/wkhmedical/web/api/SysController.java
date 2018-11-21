package com.wkhmedical.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.dto.SessionUserDTO;
import com.wkhmedical.po.UserExinfo;
import com.wkhmedical.security.TUserDetails;
import com.wkhmedical.service.UserExinfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "系统级接口")
@RequestMapping("/api")
public class SysController {

	@Autowired
	UserExinfoService userExinfoService;

	@ApiOperation(value = "获取当前用户")
	@GetMapping("/get.current_user")
	public SessionUserDTO getCurUser(@CurrentUser TUserDetails user) {
		if (user == null) {
			return null;
		}
		UserExinfo exinfo = userExinfoService.getUserExinfo(user.getUid());
		SessionUserDTO rtnUser = new SessionUserDTO();
		rtnUser.setName(user.getRealName());
		rtnUser.setRole(user.getRole());
		rtnUser.setUserid(user.getUid());
		if (exinfo != null) {
			rtnUser.setAvatar(exinfo.getAvatar());
			rtnUser.setEmail(exinfo.getEmail());
			rtnUser.setSignature(exinfo.getSignature());
			rtnUser.setTitle(exinfo.getOffice());
			rtnUser.setGroup(exinfo.getDept());
			String labels = exinfo.getLabels();
			List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
			Map<String, Object> tmpTag = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(labels)) {
				String[] labelArr = labels.split(",");
				for (int i = 0; i < labelArr.length; i++) {
					tmpTag = new HashMap<String, Object>();
					tmpTag.put("key", i + "");
					tmpTag.put("label", labelArr[i]);
					tags.add(tmpTag);
				}
			}
			rtnUser.setTags(tags);
			rtnUser.setNotifyCount(0);
			Map<String, Object> geographic = new HashMap<String, Object>();
			Map<String, Object> sub2 = new HashMap<String, Object>();
			sub2.put("key", exinfo.getProvId() + "");
			sub2.put("label", "");
			geographic.put("province", sub2);
			sub2 = new HashMap<String, Object>();
			sub2.put("key", exinfo.getCityId() + "");
			sub2.put("label", "");
			geographic.put("city", sub2);
			rtnUser.setGeographic(geographic);
			rtnUser.setAddress(exinfo.getAddr());
			rtnUser.setPhone(exinfo.getTel());
		}
		return rtnUser;
	}

	@ApiOperation(value = "获取当前用户权限")
	@GetMapping("/get.current_role")
	public Set<String> getCurRole(@CurrentUser TUserDetails user) {
		Set<String> authSet = AuthorityUtils.authorityListToSet(user.getAuthorities());
		return authSet;
	}
}
