package com.zte.message.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.framework.util.DateUtil;
import com.zte.framework.util.Page;
import com.zte.message.domain.Message;
import com.zte.message.service.MessageService;
import com.zte.message.util.MessageConstant;
import com.zte.user.domain.User;

@Controller("messageAction")
@Scope("prototype")
public class MessageAction extends AjaxAction {
	@Autowired
	private MessageService messageService;
	//参数
	Map<String,String> parameter;
	
	public String index(){
		return INPUT;
	}
	
	public String send(){
		if(parameter!=null){
			int friendId=Integer.valueOf(parameter.get("friendId"));
			User user=getUser();
			Message message=new Message();
			message.setContent(parameter.get("content"));
			message.setSendUsername(user.getName());
			message.setCreateBy(user.getUsername());
			message.setUserId(user.getId());
			message.setFriendId(friendId);
			message.setCreateTime(DateUtil.getCurrentDateTime());
			messageService.insert(message);
			ajaxUtil.setSuccess("发送成功！",message);
		}else{
			ajaxUtil.setFail("发送失败！");
		}
		return JSON;
	}
	
	//获取未读的新消息列表
	public String getMessageList(){
		if(!( parameter==null&&parameter.containsKey("friendId")&&StringUtils.isNotBlank(parameter.get("friendId")))){
			return ajaxUtil.setFail("参数错误！");
		}
			
		Map<String,Object> map=new HashMap<String, Object>();
		int friendId=Integer.valueOf(parameter.get("friendId"));
		map.put("userId", friendId);
		map.put("friendId", getUserId());
		map.put("createBy", getUser().getUsername());
		List<Message> list = messageService.getMessageList(map);
		if(list.size()>0){
			return ajaxUtil.setSuccess(list);
		}
		return ajaxUtil.setFail("暂无消息！");
	}
	
	//获取消息总数
	public String getMessageCount(){
		Map<String,Object> map=new HashMap<String, Object>();
		//map.put("sendReceiveGroup", message.getSendReceiveGroup());
		map.put("isRead",MessageConstant.ISREAD_TRUE);
		int count = messageService.getMessageCount(map);
		ajaxUtil.setSuccess(count);
		return JSON;
	}
	
	//分页获取消息列表
	public String findMessageListByPage(){
		if(parameter==null){
			return ajaxUtil.setFail("参数不能为空！");
		}
		else if(!(parameter.containsKey("pageIndex")&&StringUtils.isNotBlank(parameter.get("pageIndex")) 
				&& parameter.containsKey("pageSize")&&StringUtils.isNotBlank(parameter.get("pageSize")))){
			return ajaxUtil.setFail("参数错误！");
		}
		Map<String,Object> map=new HashMap<String, Object>();
		int pageIndex=Integer.valueOf(parameter.get("pageIndex"));
		int pageSize=Integer.valueOf(parameter.get("pageSize"));
		int friendId=Integer.valueOf(parameter.get("friendId"));
		map.put("userId", getUserId());
		map.put("friendId", friendId);
		map.put("isRead",MessageConstant.ISREAD_TRUE);
		map.put("startNum", Page.getStartNum(pageIndex, pageSize));
		map.put("endNum",Page.getEndNum(pageIndex, pageSize));
		List<Message> list = messageService.findMessageListByPage(map);
		int pageTotal = messageService.getMessageCount(map);
		Page<Message> page=new Page<Message>(pageIndex,pageSize,list,pageTotal);
		return ajaxUtil.setSuccess(page);
	}
	
	//将消息置为已读
	public String updateMessageIsRead(){
		User user=(User)getSessionProperty("user");
		if(user==null){
			return ajaxUtil.setFail("请先登录！");
		}
		else if(parameter==null){
			return ajaxUtil.setFail("参数不能为空！");
		}
		else if(!parameter.containsKey("ids")){
			return ajaxUtil.setFail("参数错误！");
		}
		Map<String,Object> map=new HashMap<String, Object>();
		
		map.put("friendId", user.getId());
		map.put("ids", parameter.get("ids"));
		try{
			int result =messageService.updateMessageIsRead(map);
			if(result==0){
				return ajaxUtil.setFail("数据更新完成，但均未成功！");
			}
		}catch(Exception e){
			return ajaxUtil.setFail("数据更新失败！");
		}
		return ajaxUtil.setSuccess("数据更新为已读状态！");
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}

}
