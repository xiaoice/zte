package com.zte.framework.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;

import com.zte.user.domain.User;

public class MapUtil {
	
	public static <T> Map<String,Object> fromT(T t) throws Exception{
        Map<String,Object> map=new HashMap<String, Object>();
        Class<? extends Object> clazz=t.getClass();
        Field[] fields=clazz.getDeclaredFields();
        PropertyDescriptor[] ps = BeanUtils.getPropertyDescriptors(clazz);
        for(PropertyDescriptor p : ps){
        	String name = p.getName();
        	String type=p.getPropertyType().toString();
        	//String w=p.getWriteMethod().toGenericString();
        	String d=p.getDisplayName();
        	//String r=p.getReadMethod().getDefaultValue().toString();
        	String s=p.getShortDescription();
        	String value=(String) p.getValue(name);
        	System.out.println(name+"|"+type+"|"+value+"|"+"|"+d+"|"+"|"+s);
        }

        
        for(Field field:fields){
            String name=getMethodName(field.getName());
            Type type =field.getGenericType();
            //System.out.println(type.toString()+"|"+name);
            Method method;
            if(type==boolean.class){
                method=clazz.getMethod("is"+name);
            }else{
            	method=clazz.getMethod("get"+name);
            }
            map.put(name, method.invoke(t));
        }
        return map;
    }
    
    // 把一个字符串的第一个字母大写、效率是最高的、  
    private static String getMethodName(String fildeName) throws Exception{
        char[] items = fildeName.toCharArray();  
        items[0] = (char) (items[0] -32);  
        return new String(items);  
    }
    
    @SuppressWarnings("unchecked")
	public static void main(String[] agrs) throws Exception{
    	User user=new User();
    	user.setAge(11);
    	user.setA1(true);
    	user.setA2(true);
    	user.setA3(new Date());
    	user.setA4(4.3d);
    	user.setA5(8.0);
    	user.setA6(null);
    	user.setA7(new BigDecimal(12322));
    	user.setA8((short)2);
    	user.setName("dddd");
    	user.setA0(null);
    	user.setListInt(Arrays.asList(new Integer[]{1,2}));
    	user.setListStr(Arrays.asList(new String[]{"dd","cc"}));
    	
    	Map<String,Object> map=MapUtil.fromT(user);
    	//Map<String,Object> map=JSONObject.fromObject(user);
    	System.out.println(map);
    	
    }
}
