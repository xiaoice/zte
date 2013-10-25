package com.zte.framework.jdbc;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.zte.framework.util.SysConfigHelper;
import com.zte.user.domain.User;

@Repository
public class BaseDao<T> extends SqlSessionDaoSupport {
	
	//根据beanname获取对象实例
	protected T getBean(String beanName){
		return (T)SysConfigHelper.getAppContext().getBean(beanName);
	}
}
