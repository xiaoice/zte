package com.zte.user.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.zte.framework.jdbc.BaseMapper;
import com.zte.user.domain.User;

@Component("userMapper")
public interface UserMapper extends BaseMapper<User>{
	
	/**
	 * 根据用户名、密码登录
	 * @param map
	 * @return
	 */
	User findUserByUsernameAndPwd(Map<String,Object> map);
}
