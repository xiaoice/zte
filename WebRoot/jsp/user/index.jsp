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
					<div class="panel-heading"><span id="chat_title">与</span><span id="btn_reload" title="刷新" class="glyphicon glyphicon-refresh pull-right"></span></div>
					<div class="panel-body chat_warp_out">
						<div class="more_message">加载更早记录</div>
						<ul class="chat_warp_out_ui"></ul>
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

//参数列表
var page={
	pageTotal:0,
	pageIndex:1,	//默认第一页
	pageSize:10		//每页显示10条
};
var $out=$(".chat_warp_out_ui"),$more=$(".more_message");
var service={
		//发送消息
		sendMsg:function(item){
			var option={
				"parameter.content":$("#content").val(),
				"parameter.friendId":$("#friendId").val()
			};
			$.post("${base}message/send.action",option).done(function(result){
				$out.append(service.getOneself(result.data));
				service.scrollEnd();
			});
		},
		//刷新新消息列表
		getNewMessageList:function(){
			$.get("message/getMessageList.action?parameter.friendId="+$("#friendId").val()).done(function(result){
				if(typeof result=="object" && result.data!=null && result.data.length>0){
					var ids=[],msgListCache=[],data=result.data;
					for(var i=0,j=data.length;i<j;i++){
						var item=result.data[i];
						if($("#chat_"+item.id).size()==0){
				    		if(item.createBy==$("#username").val()){
				    			msgListCache.push(service.getOneself(item));
							}
							else{
								msgListCache.push(service.getHimself(item));
							}
				    		ids.push(item.id);
						}
			    	}
					$out.removeClass("wait").append(msgListCache);
					service.updateMsgRead(ids);
					service.scrollEnd();
				}
			});
		},
		//标记消息为已读
		updateMsgRead:function(ids){
			if(ids.length>0){
				$.ajax({url:"message/updateMessageIsRead.action?parameter.ids="+ids.join(","),async:false}).done(function(result){
					console.log(result.message);
				});
			}
		},
		//显示自己
		getHimself:function(data){
			var li ="<li id=\"chat_"+data.id+"\" class=\"chat_left\"> <div class=\"left tip\"></div> <div class= " 
				+"\"border-radius-5 left chat_body\"> <a class=\"close\" href=\"javascript:;\">×</a> <p class= " 
				+"\"content\">"+data.content+"</p> <p class=\"create_time\"><span class=\"user\"> " 
				+"用户名："+data.sendUsername+"</span><span class=\"time\">"+data.createTime.replace("T"," ")+"</span><a href=\"javascript:;\" " 
				+"class=\"reply hide\">回复</a></p> </div> </li>";
			return li;
		},
		//显示他人
		getOneself:function(data){
			var li ="<li id=\"chat_"+data.id+"\" class=\"chat_right\"> <div class=\"right tip\"></div> <div class= " 
				+"\"border-radius-5 right chat_body\"> <a class=\"close\" href=\"javascript:;\">×</a> <p class= " 
				+"\"content\">"+data.content+"</p> <p class=\"create_time\"><span class=\"user\"> " 
				+"用户名："+data.sendUsername+"</span><span class=\"time\">"+data.createTime.replace("T"," ")+"</span><a href=\"javascript:;\" " 
				+"class=\"reply hide\">回复</a></p> </div> </li>";
			return li;
		},
		pageNext:function(){
			$.post("message/findMessageListByPage.action",{"parameter.pageIndex":page.pageIndex,"parameter.pageSize":page.pageSize,"parameter.friendId":$("#friendId").val()}).done(function(result){
				if(typeof result=="object" && result.data!=null){
					if(result.data.list!=null && result.data.list.length>0){
						page.pageTotal=result.data.pageTotal;
						var ids=[],msgListCache=[],data=result.data.list;
						for(var i=0,j=data.length;i<j;i++){
							var item=data[i];
				    		if(item.createBy==$("#username").val()){
				    			msgListCache.push(service.getOneself(item));
							}
							else{
								msgListCache.push(service.getHimself(item));
							}
				    		ids.push(item.id);
				    	}
						$out.prepend(msgListCache.reverse());
						console.log(page.pageIndex,page.pageTotal);
						if(page.pageIndex==page.pageTotal){
							$more.hide();
						}else{
							page.pageIndex++;
						}
					}
				}
			});
		},
		//循环获取消息
		timer:function(){
			setInterval(function(){
				service.getNewMessageList();
			},2000);
		},
		//滚屏置底
		scrollEnd:function(){
			$out.scrollTop(99999);
		},
		clear:function(){
			$out.empty();
		},
		wait:function(){
			$out.addClass("wait");
		}
	};
	
	//service.timer();
	//点击用户列表
	$(".user_cursor").on("click",function(){
		var $this=$(this);
		var friendId=$this.attr("friendId"),
			friendName=$this.html();
		$("#friendId").val(friendId);
		$("#chat_title").html("我与"+friendName+"的聊天");
		service.clear();
		service.wait();
		service.getNewMessageList();
	});
	
	//点击发送消息
	$("#input_ok").on("click",function(){
		service.sendMsg();
	});
	
	//点击加载更早记录
	$more.on("click",function(){
		service.pageNext();
	});
	//点击发送消息
	$("#btn_reload").on("click",function(){
		service.getNewMessageList();
	});
	
</script>