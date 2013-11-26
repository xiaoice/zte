package com.zte.framework.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

public class CacheMessage {
	private CacheMessage(){
		super();
	}
	
	public static Map<String,List<JSONObject>> cacheMessageList=null;
	public static Map<String, List<JSONObject>> getCacheList(){
		if(cacheMessageList!=null){
			return cacheMessageList;
		}
		return new HashMap<String, List<JSONObject>>();
	}
	
	public static void putCache(Integer key,JSONObject message){
		putCache(String.valueOf(key), message);
	}
	
	public static void putCache(String key,JSONObject message){
		cacheMessageList=getCacheList();
		if(cacheMessageList.containsKey(key)){
			cacheMessageList.get(key).add(message);
		}else{
			List<JSONObject> list=new ArrayList<JSONObject>();
			list.add(message);
			cacheMessageList.put(key,list);
		}
	}
	
	public static List<JSONObject> getCache(Integer key){
		return getCache(String.valueOf(key));
	}
	
	public static List<JSONObject> getCache(String key){
		cacheMessageList=getCacheList();
		if(cacheMessageList.containsKey(key)){
			return cacheMessageList.get(key);
		}else{
			return null;
		}
	}
	
	public static void remove(String key){
		getCacheList().remove(key);
	}
	
	public static void remove(Integer key){
		remove(String.valueOf(key));
	}
}
