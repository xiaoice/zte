package com.zte.framework.jdbc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.stereotype.Service;

import com.zte.framework.util.SysConfigHelper;

@Service("baseService")
public class BaseService<T>  implements BaseMapper<T>{
	private BaseMapper<T> baseMapper;
	
	//根据beanname获取对象实例
	protected BaseMapper getBean(String beanName){
		return (BaseMapper)SysConfigHelper.getAppContext().getBean(beanName);
	}
	
	//获取并缓存Mapper实例
	private BaseMapper<T> getMapper(){
		if(baseMapper==null){
			Type type = this.getClass().getGenericSuperclass();
			Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
			Class<T> entityClass=(Class<T>) trueType;
			String mapper =entityClass.getSimpleName().toLowerCase()+"Mapper";
			System.out.println(mapper);
			baseMapper= getBean(mapper);
		}
		return baseMapper;
	}
	
	@Override
	public int insert(T t) {
		return getMapper().insert(t);
	}

	@Override
	public int deleteById(int id) {
		return getMapper().deleteById(id);
	}

	@Override
	public int update(T t) {
		return getMapper().update(t);
	}

	@Override
	public T getById(int id) {
		return (T) getMapper().getById(id);
	}

}
