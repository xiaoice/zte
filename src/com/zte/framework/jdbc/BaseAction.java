package com.zte.framework.jdbc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.zte.framework.util.SysConfigHelper;

public class BaseAction extends ActionSupport {
	
	/**
	 * 获取服务器目录
	 * @return
	 */
	public String getAppPath() {
		return ServletActionContext.getServletContext().getRealPath("/");
	}

	/**
	 * 获取session的值
	 * @param key
	 * @return
	 */
	public Object getSessionProperty(String key) {
		return ActionContext.getContext().getSession().get(key);
	}
	
	/**
	 * 设置session的值
	 * @param key
	 * @return
	 */
	public void setSessionProperty(String key, Object value) {
		ActionContext.getContext().getSession().put(key, value);
	}
	
	/**
	 * 获取spring容器
	 * @return
	 */
	public ApplicationContext getApplicationContext(){
		return SysConfigHelper.getAppContext();
	}
	
	/**
	 * 获取servlet request
	 * @return
	 */
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 获取servlet response
	 * @return
	 */
	public HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 获取servlet session
	 * @return
	 */
	public HttpSession getSession(){
		return ServletActionContext.getRequest().getSession();
	}

}
