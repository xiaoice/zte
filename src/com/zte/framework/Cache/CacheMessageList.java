package com.zte.framework.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zte.message.domain.Message;

public class CacheMessageList<T> {
	
	/**
	 * 获取所有用户的缓存
	 * @return
	 */
	public static Map<String, Cache> getAllMessage(){
		return CacheManage.getCacheMap();
	}
	
	public static List<Message> getMessageList(String key){
		if(CacheManage.getCacheMap().containsKey(key)&&CacheManage.getCacheMap()!=null){
			return (List<Message>) CacheManage.getCacheMap();
		}
		return null;
	}
	
	public static void putMessage(String key,String value){
		Cache cache=CacheManage.getCache(key);
		if(cache!=null){
			List<String> list=(List<String>) cache.getValue();
			cache.setKey(key);
			list.add(value);
			cache.setValue(list);
		}
	}
	
	
}
