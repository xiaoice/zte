package com.zte.message.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseServiceImpl;
import com.zte.message.domain.Message;
import com.zte.message.service.MessageService;

@Service("messageServiceImpl")
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {
	@Autowired
	MessageService messageService;
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public List<Message> getMessageList(Map<String,Object> parameter) {
		return this.selectList("messageService.getMessageList",parameter);
	}

	@Override
	public int getMessageCount(Map<String,Object> parameter) {
		String a="eeeeeeeeeeeee";
		System.out.println(a);
		sqlSession.select("messageService.getMessageCount", parameter,null);
		//messageService.select("MessageService.getMessageCount", parameter,null);
		return 3;
	}
	
	@Override
	public List<Message> findMessageListByPage(Map<String,Object> parameter) {
		return this.selectList("messageService.findMessageListByPage",parameter);
	}

	@Override
	public int updateMessageIsRead(Map<String, Object> parameter) {
		return this.update("messageService.updateMessageIsRead", parameter);
	}

	@Override
	public int insert(Message message) {
		return this.insert("messageService.insert", message);
	}
}
