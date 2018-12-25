<%--
  Created by IntelliJ IDEA.
  User: pcmpc
  Date: 2018/12/22
  Time: 23:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    /*String path = request.getContextPath();
    System.out.println(path);
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";*/
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
<form action="register.sc" name="loginForm" accept-charset="utf-8">
    <div>账&nbsp&nbsp号：<input type="text" name="id" /></div>
    <div>密&nbsp&nbsp码：<input type="text" name="password" /></div>
    <div><input type="submit" name="regist" value="注册" /></div>
</form>
<div>
    <span style="color: blue; ">${sessionScope.registMessage }</span>
</div>
<%
    session.removeAttribute("registMessage");
%>
</body>
</html>
