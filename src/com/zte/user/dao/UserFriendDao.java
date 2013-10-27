package com.zte.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserFriendDao {
	
	/**
	 * 根据用户名id得到好友列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> findByUserId(Map<String,Object> parameter);
}
