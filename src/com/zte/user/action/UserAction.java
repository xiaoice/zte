package com.zte.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.framework.util.DateUtil;
import com.zte.framework.util.Page;
import com.zte.message.domain.Message;
import com.zte.message.util.MessageConstant;
import com.zte.user.domain.User;
import com.zte.user.domain.UserFriend;
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
	
	//根据条件进行分页
	public String findListByPage(){
		if(!(parameter!=null&&parameter.containsKey("pageIndex")&&StringUtils.isNotBlank(parameter.get("pageIndex")) 
							&& parameter.containsKey("pageSize")&&StringUtils.isNotBlank(parameter.get("pageSize"))
							&&parameter.containsKey("name")&&StringUtils.isNotBlank(parameter.get("name")))){
			return ajaxUtil.setFail("参数错误！");
		}
		Map<String,Object> map=new HashMap<String, Object>();
		int pageIndex=Integer.valueOf(parameter.get("pageIndex"));
		int pageSize=Integer.valueOf(parameter.get("pageSize"));
		map.put("userId", getUserId());
		map.put("name", parameter.get("name"));
		map.put("startNum", Page.getStartNum(pageIndex, pageSize));
		map.put("endNum",Page.getEndNum(pageIndex, pageSize));
		List<Map<String,String>> list =userService.findListByPage(map);
		int pageTotal = userService.findListCountByPage(map);
		Page<Map<String,String>> page=new Page<Map<String,String>>(pageIndex,pageSize,list,pageTotal);
		return ajaxUtil.setSuccess(page);
	}
	
	//修改用户信息
	public String updateUserinfo(){
		if(parameter==null){
			return ajaxUtil.setFail("参数错误！");
		}
		User user=getUser();
		user.setName(parameter.get("name"));
		user.setPhoto(parameter.get("photo"));
		user.setAge(Integer.valueOf(parameter.get("age")));
		int result =userService.update(user);
		if(result>0){
			setContextProperty("result_tip", SUCCESS);
			setSessionProperty("user",user);
			return ERROR;
		}
		else{
			setContextProperty("result_tip", ERROR);
			return ERROR;
		}
	}
	
	//加为好友
	public String saveUserFriend(){
		if(parameter==null){
			return ajaxUtil.setFail("参数错误！");
		}
		
		int friendId=Integer.valueOf(parameter.get("friendId"));
		UserFriend userFriend = new UserFriend();
		userFriend.setCreateBy(getUser().getUsername());
		userFriend.setCreateTime(DateUtil.getCurrentDateTime());
		userFriend.setFriendId(friendId);
		userFriend.setStatus("1");
		userFriend.setUserId(getUserId());
		userFriendService.insert(userFriend);
		return ajaxUtil.setSuccess();
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
}
