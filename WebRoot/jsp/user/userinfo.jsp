<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<title>修改用户信息</title>
<!-- Bootstrap -->
<%@include file="/jsp/common/bootstrap.jsp"%>
</head>
<body>
	<div class="container">
		<%@include file="/jsp/common/header.jsp" %>
	  	<s:if test="#result_tip=='success'">
	  	<div class="alert alert-success">修改用户信息成功！<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button></div>
	  	</s:if>
	  	<s:if test="#result_tip=='error'">
	  	<div class="alert alert-danger">修改用户信息失败！<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button></div>
	  	</s:if>
		<div class="panel panel-default">
	  		<div class="panel-heading no_border">修改用户信息</div>
			<s:form action="/user/updateUserinfo.action" role="form-horizontal">
				<table class="table no_margin">
					<tr>
						<td width="100" align="right"><label>用户名：</label></td>
						<td width="300">${user.username}</td>
						<td></td>
					</tr>
					<tr>
						<td align="right"><label>邮箱：</label></td>
						<td>137743212@qq.com  <a class="text-info">立即验证</a></td>
						<td></td>
					</tr>
					<tr>
						<td align="right"><label for="user_photo">头像：</label></td>
						<td valign="top"><img alt="头像" src="${user.photo}" onerror="this.onerror=null;this.src='images/github.png'" wdith="32" height="32"/> <a class="text-warning">更换头像</a></td>
						<td></td>
					</tr>
					<tr>
						<td align="right"><label for="user_name">昵称：</label></td>
						<td><input type="text" class="form-control" value="${user.name}" id="user_name" name="parameter.name" placeholder="请输入昵称"></td>
						<td></td>
					</tr>
					<tr>
						<td align="right"><label for="user_age">年龄：</label></td>
						<td><input type="text" class="form-control" value="${user.age}" id="user_age" name="parameter.age" placeholder="请输入年龄"></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="2">
							<button type="submit" class="btn btn-primary">保存</button>
						</td>
					</tr>
				</table>
			</s:form>
		</div>
	</div>

</body>
</html>

