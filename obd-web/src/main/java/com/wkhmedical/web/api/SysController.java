package com.wkhmedical.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoxeo.boot.security.CurrentUser;
import com.wkhmedical.dto.SessionUserDTO;
import com.wkhmedical.security.TUserDetails;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "系统级接口")
@RequestMapping("/api")
public class SysController {

	@ApiOperation(value = "获取当前用户")
	@GetMapping("/get.current_user")
	public SessionUserDTO carMotAdd(@CurrentUser TUserDetails user) {
		if (user == null) {
			return null;
		}
		SessionUserDTO rtnUser = new SessionUserDTO();
		rtnUser.setName(user.getRealName());
		rtnUser.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png");
		rtnUser.setUserid(user.getUid());
		rtnUser.setEmail("user@maxnoo.com");
		rtnUser.setSignature("Just Do It");
		rtnUser.setTitle("工程师");
		rtnUser.setGroup("攻程一科-技术二部-北京分公司-集团总部");
		List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmpTag = new HashMap<String, Object>();
		tmpTag.put("key", "0");
		tmpTag.put("label", "工作狂");
		tags.add(tmpTag);
		tmpTag = new HashMap<String, Object>();
		tmpTag.put("key", "1");
		tmpTag.put("label", "典型处女座");
		tags.add(tmpTag);
		tmpTag = new HashMap<String, Object>();
		tmpTag.put("key", "2");
		tmpTag.put("label", "格物致知");
		tags.add(tmpTag);
		tmpTag = new HashMap<String, Object>();
		tmpTag.put("key", "3");
		tmpTag.put("label", "知行合一");
		tags.add(tmpTag);
		rtnUser.setTags(tags);
		rtnUser.setNotifyCount(0);
		Map<String, Object> geographic = new HashMap<String, Object>();
		Map<String, Object> sub2 = new HashMap<String, Object>();
		sub2.put("key", "330000");
		sub2.put("label", "浙江省");
		geographic.put("province", sub2);
		sub2 = new HashMap<String, Object>();
		sub2.put("key", "330100");
		sub2.put("label", "杭州市");
		geographic.put("city", sub2);
		rtnUser.setGeographic(geographic);
		rtnUser.setAddress("白宫 666 号");
		rtnUser.setPhone("010-12345678");
		return rtnUser;
	}

	@ApiOperation(value = "获取当前用户权限")
	@GetMapping("/get.current_role")
	public Set<String> getCurRole(@CurrentUser TUserDetails user) {
		Set<String> authSet = AuthorityUtils.authorityListToSet(user.getAuthorities());
		return authSet;
	}
}
