package com.zte.user.dao;

import java.util.Map;

import com.zte.user.domain.User;

public interface UserDao {
	
	/**
	 * 根据用户名、密码登录
	 * @param map
	 * @return
	 */
	User findUserByUsernameAndPwd(Map<String,Object> map);
}
