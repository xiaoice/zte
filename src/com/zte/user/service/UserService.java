package com.zte.user.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseService;
import com.zte.user.domain.User;
import com.zte.user.mapper.UserMapper;

@Service("userService")
public class UserService extends BaseService<User> implements UserMapper{

	@Override
	public User findUserByUsernameAndPwd(Map<String, Object> map) {
		UserMapper mapper=(UserMapper)getBean("userMapper");
		return mapper.findUserByUsernameAndPwd(map);
	}
	
}
