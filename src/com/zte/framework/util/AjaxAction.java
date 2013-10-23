package com.zte.framework.util;

import java.io.IOException;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.zte.framework.jdbc.BaseAction;

public class AjaxAction extends BaseAction{
	public static final String JSON = "json";
	
	protected AjaxUtil ajaxUtil=new AjaxUtil();
	
	public void print(Object obj) throws JSONException, IOException {
	    getResponse().getWriter().print(JSONUtil.serialize(obj));
	}

	public AjaxUtil getAjaxUtil() {
		return ajaxUtil;
	}

	public void setAjaxUtil(AjaxUtil ajaxUtil) {
		this.ajaxUtil = ajaxUtil;
	}

}
