package com.zte.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.user.domain.User;
import com.zte.user.service.UserFriendService;
import com.zte.user.service.UserService;

@Controller
@Scope("prototype")
public class UserAction extends AjaxAction{
	@Resource(name="userServiceImpl")
	private UserService userService;
	@Resource(name="userFriendServiceImpl")
	private UserFriendService userFriendService;
	Map<String,String> parameter;
	
	//登录
	public String session(){
		User user = userService.findUserByUsernameAndPwd(parameter);
		if(user!=null){
			user.setPassword(null);
			setSessionProperty("user", user);
			return SUCCESS;
		}
		setContextProperty("tip", "用户名或者密码错误！");
		return INPUT;
	}
	
	//注销
	public String logout(){
		removeSessionProperty("user");
		return SUCCESS;
	}
	
	//登录
	public String join(){
		if(parameter==null){
			return INPUT;
		}else{
			User user=new User();
			user.setUsername(parameter.get("username"));
			user.setPassword(parameter.get("password"));
			int result = userService.insert(user);
			if(result>0){
				setSessionProperty("user", user);
				return SUCCESS;
			}
		}
		return INPUT;
	}
	
	//用户消息中心
	public String index(){
		Integer userId=getUserId();
		if(userId!=null){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("userId", userId);
			List<Map<String, Object>> userList = userFriendService.findByUserId(map);
			setContextProperty("userList", userList);
		}
		return SUCCESS;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
}
