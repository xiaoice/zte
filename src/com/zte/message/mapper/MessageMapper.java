package com.zte.message.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zte.framework.jdbc.BaseMapper;
import com.zte.framework.util.Page;
import com.zte.message.domain.Message;

@Service("messageMapper")
public interface MessageMapper extends BaseMapper<Message> {
	
	/**
	 * 获取未读的新消息列表
	 * @return
	 */
	List<Message> getMessageList(Map<String,Object> map);
	
	/**
	 * 获取消息总数
	 * @param map
	 * @return
	 */
	int getMessageCount(Map<String,Object> map);
	
	/**
	 * 分页获取消息列表
	 * @param map
	 * @return
	 */
	List<Message> findMessageListByPage(Map<String, Object> map);
	
	/**
	 * 将消息置为已读
	 * @param sendReceiveGroup 2人消息的组号
	 * @param ids	逗号隔开  如：10,12,13
	 * @return
	 */
	int updateMessageIsRead(Map<String, Object> map);
}
