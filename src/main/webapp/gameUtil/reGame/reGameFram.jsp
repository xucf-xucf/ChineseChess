<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <%
    String gameId = request.getParameter("gameId");  
    %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台010管理</title>
</head>
  <frameset cols="50%,*" rows="100%,*" frameborder="no" border="0" framespacing="0">
     <frame src="myGame.jsp?gameId='<%=gameId %>'"name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame" title="leftFrame" />
    <frame src="myGameUtil.jsp" name="rightFrame" id="rightFrame" title="rightFrame" width="" height=""/>
  </frameset>
</frameset>
<noframes>
<body>
</body></noframes>
</html>