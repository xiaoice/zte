<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<title>登录界面</title>
<!-- Bootstrap -->
<%@include file="/jsp/common/bootstrap.jsp"%>
</head>
<body>
	<div class="container">
		<h2>用户登录</h2>
		<s:form action="user/login.action" role="form-horizontal">
			<div class="form-group">
				<label for="user_username">用户名： </label> 
				<input type="text" class="form-control" id="user_username" name="user.username" placeholder="请输入用户名">
			</div>
			<div class="form-group">
				<label for="user_password">密码：</label>
				<input type="password" class="form-control" id="user_password" name="user.password" placeholder="请输入密码">
			</div>
			<button type="submit" class="btn btn-primary">登录</button>
			<button type="button" class="btn btn-default">取消</button>
		</s:form>
	</div>
</body>
</html>

