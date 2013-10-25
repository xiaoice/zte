package com.zte.user.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.struts2.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.message.service.MessageService;
import com.zte.user.domain.User;
import com.zte.user.service.UserService;

@Controller
public class UserAction extends AjaxAction{
	@Resource(name="userServiceImpl")
	private UserService userService;
	private User user;
	
	public String insert() throws IOException, JSONException{
		if(user!=null){
			user.setUsername("zhangshan");
			user.setPassword("123456");
			//user.setName("张三");
			user.setPhoto("default.png");
			//ActionContext.getContext().getSession().put("user", "");
			//userService.insert(user);
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
		/*
		 * Map<String, Object> parameter=new HashMap<String, Object>();
		parameter.put("sendReceiveGroup", "10-11");
		parameter.put("isRead",MessageConstant.ISREAD_TRUE);
		MessageService s=new MessageServiceImpl();
		//int dd =messageServiceImpl.getMessageCount(parameter);
		parameter.put("username", "admin");
		parameter.put("password", "admin");
		List<User> dd = sqlSession.selectList("com.zte.user.dao.UserDao.findAll");
		User a1 = sqlSession.selectOne("com.zte.user.dao.UserDao.findUserByUsernameAndPwd", parameter);
		User a2 = userService.findUserByUsernameAndPwd(parameter);
		*/
		
		if(user==null){
			return INPUT;
		}else{
			String username=user.getUsername();
			String password=user.getPassword();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("username", username);
			map.put("password", password);
			User user = userService.findUserByUsernameAndPwd(map);
			if(user!=null){
				setSessionProperty("user", user);
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
