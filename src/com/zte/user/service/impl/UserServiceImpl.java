package com.zte.user.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseServiceImpl;
import com.zte.user.dao.UserDao;
import com.zte.user.domain.User;
import com.zte.user.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User findUserByUsernameAndPwd(Map<String, Object> parameter) {
		//return this.selectOne("com.zte.user.dao.UserDao.findUserByUsernameAndPwd", parameter);
		return userDao.findUserByUsernameAndPwd(parameter);
	}
	
}
