<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<title>主界面</title>
<!-- Bootstrap -->
<%@include file="/jsp/common/bootstrap.jsp"%>
<link type="text/css" href="css/chat.css" rel="stylesheet" />
</head>
<body>
	<input id="username" type="hidden" value="${user.username}"/>
	<input id="friendId" type="hidden" value="10000"/>
	<div class="container">
		<%@include file="/jsp/common/header.jsp" %>
		<div class="row">
		  	<div class="col-md-8 no_padding_right">
				<div class="panel panel-default chat_warp">
					<div class="panel-heading">我与10000的聊天（共2条）<span id="btn_reload" title="刷新" class="glyphicon glyphicon-refresh pull-right"></span></div>
					<div class="panel-body chat_warp_out">
						<ul>
							<li class="chat_left">
								<div class="left chat_user">
									<a href="javascript:;"><img class="border-radius-5 border-shadow-img user_img" src="${base}images/default_user.png"/></a>
								</div>
								<div class="left tip"></div>
								<div class="border-radius-5 left chat_body">
									<a class="close" href="javascript:;">×</a>
									<p class="content">你说话呢，在不在?</p>
									<p class="create_time"><span class="time">2013-01-04 15:20:27</span><a href="javascript:;" class="reply hide">回复</a></p>
								</div>
							</li>
							<li class="chat_right">
								<div class="right chat_user">
									<a href="javascript:;"><img class="border-radius-5 border-shadow-img user_img" src="${base}images/default_user.png"/></a>
								</div>
								<div class="right tip"></div>
								<div class="border-radius-5 right chat_body">
									<a class="close" href="javascript:;">×</a>
									<p class="content">在啊，你昨天晚上跑哪里去了啊，我都找不到你了呢？死丫头</p>
									<p class="create_time"><span class="user">用户名：99.1.67.245</span><span class="time">2013-01-04 15:20:27</span><a href="javascript:;" class="reply hide">回复</a></p>
								</div>
							</li>
						</ul>
					</div>
					<div class="face_panel">
						<span class="glyphicon glyphicon-dashboard"></span>
						<span class="caret"></span>
					</div>
					<s:textarea name="user.name" id="content" cssClass="form-control reply_content"></s:textarea>
					<div class="send_panel clearfix">
						<button id="input_ok" type="button" class="btn btn-primary btn-xs pull-right">发送消息</button>
					</div>
				</div>
			</div>
		  	<div class="col-md-4">
				<div class="panel panel-default">
				  	<div class="panel-heading clearfix">
						<a class="btn btn-default btn-xs">批量删除</a>
						<a class="btn btn-default btn-xs">标为已读</a>
					</div>
					<div class="list-group">
					  <s:iterator value="userList" var="t">
					  	 <a class="list-group-item user_cursor" friendId="${t.friend.id}">${t.friend.name}</a>
					  </s:iterator>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>


<script type="text/javascript">

var $out=$(".chat_warp_out");
var service={
		//发送消息
		sendMsg:function(item){
			service.clear();
			var option={
				"parameter.content":$("#content").val(),
				"parameter.friendId":$("#friendId").val()
			};
			$.post("${base}message/send.action",option).done(function(result){
				$out.find("ul").append(service.getOneself(result.data));
			});
		},
		//刷新新消息列表
		getNewMessageList:function(){
			$.get("message/getMessageList.action?parameter.friendId="+$("#friendId").val()).done(function(result){
				if(typeof result=="object" && result.data!=null && result.data.length>0){
					var ids=[],msgListCache=[],data=result.data;
					for(var i=0,j=data.length;i<j;i++){
						var item=result.data[i];
			    		if(item.createBy==$("#username").val()){
			    			msgListCache.push(service.getOneself(item));
						}
						else{
							msgListCache.push(service.getHimself(item));
						}
			    		ids.push(item.id);
			    	}
					$out.find("ul").removeClass("wait").append(msgListCache);
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
			$out.find("ul").scrollTop(99999);
		},
		clear:function(){
			$out.find("ul").empty();
		},
		wait:function(){
			$out.html('<ul class="wait"></ul>');
		}
	};
	
	service.timer();
	//点击用户列表
	$(".user_cursor").on("click",function(){
		var $this=$(this);
		var friendId=$this.attr("friendId");
		$("#friendId").val(friendId);
		service.clear();
		service.wait();
		service.getNewMessageList();
	});
	
	//点击发送消息
	$("#input_ok").on("click",function(){
		service.sendMsg();
	});
	//点击发送消息
	$("#btn_reload").on("click",function(){
		service.getNewMessageList();
	});
	
</script>