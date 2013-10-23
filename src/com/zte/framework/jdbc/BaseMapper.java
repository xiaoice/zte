package com.zte.framework.jdbc;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component("baseMapper")
public interface BaseMapper<T>{
	public int insert(T user);
	public int deleteById(int id);
	public int update(T user);
	public T getById(int id);
}
