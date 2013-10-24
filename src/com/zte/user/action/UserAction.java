package com.zte.user.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts2.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.user.domain.User;
import com.zte.user.service.UserService;

@Controller("userAction")
public class UserAction extends AjaxAction{
	@Autowired
	private UserService userService;
	
	@Autowired
	SqlSession sqlSession;
	
	
	private User user;
	
	public String insert() throws IOException, JSONException{
		if(user!=null){
			user.setUsername("zhangshan");
			user.setPassword("123456");
			//user.setName("张三");
			user.setPhoto("default.png");
			//ActionContext.getContext().getSession().put("user", "");
			userService.insert(user);
			ajaxUtil.setSuccess("dddddd", user);
			//String str = JSONUtil.serialize(ajaxResult);
		    //ServletActionContext.getResponse().getWriter().print("233");
			return INPUT;
		}else{
			return INPUT;
		}
	}
	
	//登录
	public String login(){
		List<User> list =userService.findAll();
		System.out.println(list);
		if(user==null){
			return INPUT;
		}else{
			String username=user.getUsername();
			String password=user.getPassword();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("username", username);
			map.put("password", password);
			User result = userService.findUserByUsernameAndPwd(map);
			if(result!=null){
				setSessionProperty("user", result);
				return SUCCESS;
			}
		}
		return INPUT;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
