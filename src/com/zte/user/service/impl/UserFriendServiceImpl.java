package com.zte.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseServiceImpl;
import com.zte.user.dao.UserFriendDao;
import com.zte.user.domain.User;
import com.zte.user.service.UserFriendService;

@Service
public class UserFriendServiceImpl extends BaseServiceImpl<User> implements UserFriendService {
	@Autowired
	private UserFriendDao userFriendDao;

	@Override
	public List<Map<String, Object>> findByUserId(Map<String, Object> parameter) {
		return userFriendDao.findByUserId(parameter);
	}
	
	

}
