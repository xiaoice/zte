package com.zte.message.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseService;
import com.zte.framework.util.Page;
import com.zte.message.domain.Message;
import com.zte.message.mapper.MessageMapper;

@Service("messageService")
public class MessageService extends BaseService<Message> implements MessageMapper{

	@Override
	public List<Message> getMessageList(Map<String,Object> map) {
		MessageMapper mapper=(MessageMapper)getBean("messageMapper");
		return mapper.getMessageList(map);
	}

	@Override
	public int getMessageCount(Map<String,Object> map) {
		MessageMapper mapper=(MessageMapper)getBean("messageMapper");
		return mapper.getMessageCount(map);
	}
	
	@Override
	public List<Message> findMessageListByPage(Map<String,Object> map) {
		MessageMapper mapper=(MessageMapper)getBean("messageMapper");
		return mapper.findMessageListByPage(map);
	}

	@Override
	public int updateMessageIsRead(Map<String, Object> map) {
		MessageMapper mapper=(MessageMapper)getBean("messageMapper");
		return mapper.updateMessageIsRead(map);
	}

}
