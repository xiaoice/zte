<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>聊天室</title>
	<%@include file="/jsp/common/head.jsp"%>
	<link type="text/css" href="css/chat.css" rel="stylesheet" />
	<script src="${base}js/plugins/ember.js/js/libs/handlebars-1.0.0.js" type="text/javascript"></script>
	<script src="${base}js/plugins/ember.js/js/libs/ember-1.0.0.js" type="text/javascript"></script>
</head>
<body>
	<div class="chat_warp">
		<ul class="chat_warp_out">
			<li class="more_message_list">查看更多消息</li>
		</ul>
		<div class="chat_warp_in">
			<table class="edit_table" cellpadding="1" cellspacing="1">
				<tr><td>当前用户名：<input id="createBy" value="${ip}"/> 用户id<input id="userId" value="11"/></td></tr>
				<tr><td><s:textarea name="user.name" id="content" cssClass="content"></s:textarea></td></tr>
				<tr><td>
					<input id="input_ok" value="提交" type="button"/>
				</td></tr>
			</table>
		</div>
	</div>
</body>
</html>

<script type="text/javascript">
	//参数列表
	var page={
		pageTotal:0,
		pageIndex:1,	//默认第一页
		pageSize:10		//每页显示10条
	};
	
	//缓存消息框
	var $msgOut=$(".chat_warp_out"),$more=$(".more_message_list");
	var service={
		//发送消息
		sendMsg:function(){
			var option={
				"message.content":$("#content").val(),
				"message.sendReceiveGroup":"10-11",
				"message.createBy":$("#userId").val(),
				"message.sendUsername":$("#createBy").val()
			};
			$.post("message/insert.action",option).done(function(result){
				service.getNewMessageList();
			});
		},
		//刷新新消息列表
		getNewMessageList:function(){
			$.get("message/getMessageList.action").done(function(result){
				if(typeof result=="object" && result.data!=null && result.data.length>0){
					var ids=[],msgListCache=[],data=result.data;
					for(var i=0,j=data.length;i<j;i++){
						var item=result.data[i];
			    		if(item.createBy==$("#userId").val()){
			    			msgListCache.push(service.getOneself(item));
						}
						else{
							msgListCache.push(service.getHimself(item));
						}
			    		ids.push(item.id);
			    	}
					$msgOut.append(msgListCache);
					service.updateMsgRead(ids);
					service.scrollEnd();
				}
			});
		},
		//标记消息为已读
		updateMsgRead:function(ids){
			if(ids.length>0){
				$.ajax({url:"message/updateMessageIsRead.action?para.ids="+ids.join(","),async:false}).done(function(result){
					console.log(result.message);
				});
			}
		},
		//显示自己
		getHimself:function(data){
			var li ="<li class=\"chat_left\"> <div class=\"left tip\"></div> <div class= " 
				+"\"border-radius-5 left chat_body\"> <a class=\"close\" href=\"javascript:;\">×</a> <p class= " 
				+"\"content\">"+data.content+"</p> <p class=\"create_time\"><span class=\"user\"> " 
				+"用户名："+data.sendUsername+"</span><span class=\"time\">"+data.createTime+"</span><a href=\"javascript:;\" " 
				+"class=\"reply hide\">回复</a></p> </div> </li>";
			return li;
		},
		//显示他人
		getOneself:function(data){
			var li ="<li class=\"chat_right\"> <div class=\"right tip\"></div> <div class= " 
				+"\"border-radius-5 right chat_body\"> <a class=\"close\" href=\"javascript:;\">×</a> <p class= " 
				+"\"content\">"+data.content+"</p> <p class=\"create_time\"><span class=\"user\"> " 
				+"用户名："+data.sendUsername+"</span><span class=\"time\">"+data.createTime+"</span><a href=\"javascript:;\" " 
				+"class=\"reply hide\">回复</a></p> </div> </li>";
			return li;
		},
		pageNext:function(){
			$.post("message/findMessageListByPage.action",{"para.pageIndex":page.pageIndex,"para.pageSize":page.pageSize}).done(function(result){
				if(typeof result=="object" && result.data!=null){
					if(result.data.list!=null && result.data.list.length>0){
						page.pageTotal=result.data.pageTotal;
						var ids=[],msgListCache=[],data=result.data.list;
						for(var i=0,j=data.length;i<j;i++){
							var item=data[i];
				    		if(item.createBy==$("#userId").val()){
				    			msgListCache.push(service.getOneself(item));
							}
							else{
								msgListCache.push(service.getHimself(item));
							}
				    		ids.push(item.id);
				    	}
						$more.after(msgListCache.reverse());
				    	service.updateMsgRead(ids);
				    	page.pageIndex++;
					}
				}
			});
			if(page.pageIndex==page.pageTotal){
				$more.remove();
			}
		},
		//循环获取消息
		timer:function(){
			setInterval(function(){
				service.getNewMessageList();
			},2000);
		},
		//滚屏置底
		scrollEnd:function(){
			$msgOut.scrollTop(99999);
		}
	};
	
	$("#input_ok").on("click",service.sendMsg);
	$more.on("click",service.pageNext);
	service.timer();
	
</script>
