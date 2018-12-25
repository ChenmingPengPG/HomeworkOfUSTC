<%--
  Created by IntelliJ IDEA.
  User: pcmpc
  Date: 2018/12/22
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login action</title>
</head>
<body>
<div>
    <h3>请登录：</h3><br>
    <form action="login.sc" name="loginForm">
        <div>账&nbsp&nbsp号：<input type="text" name="id" /></div>
        <div>密&nbsp&nbsp码：<input type="password" name="password" /></div>
        <div><input type="submit" name="login" value="登录" /></div>
    </form>
    <div>
        <span style="color: blue; ">${sessionScope.loginMessage }</span>
    </div>
    <br>
    <div>
        如果您还不是我们的用户，请<a href="/pages/registeraction.jsp">点击注册</a>
    </div>
    <%
        session.removeAttribute("loginMessage");
    %>
</div>
</body>
</html>
