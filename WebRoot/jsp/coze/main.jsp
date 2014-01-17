<%@page language="java" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
 <head>
  	<title>coze</title>
  	<meta charset="utf-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1.0">
  	<!-- Bootstrap -->
	<link charset="utf-8" rel="stylesheet" href="../js/plugins/bootstrap/css/bootstrap.min.css" />
	<link charset="utf-8" type="text/css" href="../js/plugins/jquery-ui-bootstrap/css/custom-theme/jquery-ui-1.10.0.custom.css" rel="stylesheet" />
	<link charset="utf-8" rel="stylesheet" href="../js/plugins/bootstrap/css/font-awesome.css" />
	<link charset="utf-8" rel="stylesheet" href="../js/plugins/coze/css/coze.css" />
 </head>
 <body>
 	<div class="panel_wrap window_warp_model">
		<div class="panel_box">
		  <div class=panel_tool><i class="icon-remove"></i></div>
		  <div class="panel_head">窗体标题</div>
		  <div class="panel_body">
		    <ul class="panel_body_ui">
		    	<li a-repeat={{friendList}} id="li_{{$index}}">{{user.name}}</li>
		    </ul>
		  </div>
		  <div class="panel_foot">Panel footer</div>
		</div>
 	</div>
 	
 	
 	<div class="window_warp window_warp_model">
 		<div class="window_box">
 		<div class=window_tool><i class="icon-remove windows_close"></i></div>
 		<div class="window_head">{{name}}</div>
		  <div class="window_body">
		    <ul class="window_body_ui">
		    	<li class="clearfix">
		    		<div class="li_left">
		    			<img class="li_photo" alt="" src="../images/default_user.png">
		    			<div class="li_body">
		    				<i class="li_caret icon-caret-left"></i><div class="li_content">床前明月光</div>
		    				<div class="li_foot">发送时间：2014-01-13 11:56:52</div>
		    			</div>
		    		</div>
		    	</li>
		    	<li class="clearfix">
		    		<div class="li_right">
		    			<img class="li_photo" alt="" src="../images/default_user.png">
		    			<div class="li_body">
		    				<i class="li_caret icon-caret-right"></i><div class="li_content">疑似地上霜疑似地上霜疑似地上霜疑似地上霜疑似地上霜</div>
		    				<div class="li_foot">发送时间：2014-01-13 11:56:52</div>
		    			</div>
		    		</div>
		    	</li>
		    	<li class="clearfix" a-repeat="{{msgs}}">
		    		<div class="li_right">
		    			<img class="li_photo" alt="" src="../images/default_user.png">
		    			<div class="li_body">
		    				<i class="li_caret icon-caret-right"></i><div class="li_content">{{content}}</div>
		    				<div class="li_foot">发送时间：{{time}}</div>
		    			</div>
		    		</div>
		    	</li>
		    </ul>
		  </div>
		  <div class="window_foot">
		  	<div class="window_edit">
		  		<div class="window_edit_text" contentEditable=true a-model='{{text}}'>请输入内容</div>
		  		<div class="window_edit_tool">
		  			<button type="button" class="btn btn-primary btn-xs send_msg">发送消息</button>
		  			<button type="button" class="btn btn-default btn-xs windows_close">关闭</button>
		  		</div>
		  	</div>
		  </div>
 		</div>
 	</div>
 	
 	<div class="login_wrap">
 		<div class="login_box">
	 		<div class="login_head">登录系统</div>
	 		<div class="login_body">
				<div class="form-group">
					<label for="user_username">用户名： </label> 
					<input type="text" class="form-control" id="input_username" placeholder="请输入用户名">
				</div>
				<div class="form-group">
					<label for="user_password">密码：</label>
					<input type="password" class="form-control" id="input_password" placeholder="请输入密码">
				</div>
				<p>用户名为6-16位；密码为6位以上</p>
				<button id="bt_login" type="submit" class="btn btn-primary btn-lg btn-block">登录</button>
	 		</div>
 		</div>
 	</div>
 	
 </body>
</html>
<script charset="utf-8" src="../js/sea-debug.js"></script>
<script charset="utf-8" src="../js/plugins/coze/js/config.js"></script>