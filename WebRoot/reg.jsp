<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<title>注册界面</title>
<!-- Bootstrap -->
<%@include file="/jsp/common/bootstrap.jsp"%>
</head>
<body>
	<div class="container">
		<%@include file="/jsp/common/header.jsp" %>
		<h2>新用户注册</h2>
		<s:form action="join.action" role="form-horizontal">
			<div class="form-group">
				<label for="user_username">用户名： </label> 
				<input type="text" class="form-control" id="user_username" name="parameter.username" placeholder="请输入用户名">
			</div>
			<div class="form-group">
				<label for="user_password">密码：</label>
				<input type="password" class="form-control" id="user_password" name="parameter.password" placeholder="请输入密码">
			</div>
			<div class="form-group">
				<label for="user_password">确定密码：</label>
				<input type="password" class="form-control" id="user_password" placeholder="请再次输入密码">
			</div>
			<button type="submit" class="btn btn-primary">注册</button>
			<button type="button" class="btn btn-default">取消</button>
		</s:form>
	</div>
</body>
</html>

