package com.zte.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.framework.util.DateUtil;
import com.zte.user.domain.UserFriend;
import com.zte.user.service.UserFriendService;

@Controller
@Scope("prototype")
public class UserFriendAction extends AjaxAction{
	@Resource(name="userFriendServiceImpl")
	private UserFriendService userFriendService;
	
	//用户消息中心
	public String userIndex(){
		Integer userId=getUserId();
		if(userId!=null){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("userId", userId);
			List<Map<String, Object>> userList = userFriendService.findByUserId(map);
			List<Map<String, Object>> friendList = userFriendService.findByFriendId(map);
			setContextProperty("userList", userList);
			setContextProperty("friendList", friendList);
		}
		return SUCCESS;
	}
	
	//用户消息中心-Ajax
	public String findUserListAjax(){
		JSONObject jo=new JSONObject();
		Integer userId=getUserId();
		if(userId!=null){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("userId", userId);
			List<Map<String, Object>> userList = userFriendService.findByUserId(map);
			List<Map<String, Object>> friendList = userFriendService.findByFriendId(map);
			jo.accumulate("userList", userList);
			jo.accumulate("friendList", friendList);
			return ajaxUtil.setSuccess(jo);
		}
		return ajaxUtil.setFail(jo);
	}
	
	//加为好友
	public String saveUserFriend(){
		if(!(parameter!=null&&parameter.containsKey("friendId")&&StringUtils.isNotBlank(parameter.get("friendId")))){
			return ajaxUtil.setFail("参数错误！");
		}
		
		String friendId=parameter.get("friendId");
		String[] friendIds=friendId.split(",");
		for(String friendid : friendIds){
			UserFriend userFriend = new UserFriend();
			userFriend.setCreateBy(getUser().getUsername());
			userFriend.setCreateTime(DateUtil.getCurrentDateTime());
			userFriend.setFriendId(Integer.valueOf(friendid));
			userFriend.setStatus("1");
			userFriend.setUserId(getUserId());
			userFriendService.insert(userFriend);
		}
		return ajaxUtil.setSuccess();
	}
}
