package com.zte.framework.Cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManage {
	private static Map<String,Cache> cacheMap=null;
	
	private CacheManage() {
		super();
	}
	
	public static Map<String, Cache> getCacheMap(){
		if(cacheMap!=null){
			return cacheMap;
		}
		return new HashMap<String, Cache>();
	}
	
	public static void putCache(String key,Cache cache){
		if(cache.getTimeOut()>0){
			long timeOut = cache.getTimeOut()+getTimes();
			cache.setTimeOut(timeOut);
		}
		cacheMap.put(key, cache);
	}
	
	public static Cache getCache(String key){
		if(cacheMap.containsKey(key)){
			Cache cache = cacheMap.get(key);
			if(cache.getTimeOut()==0||cache.getTimeOut()<getTimes()){
				return cache;
			}
		}
		return null;
	}
	
	private static long getTimes(){
		return System.nanoTime();
	}
}
