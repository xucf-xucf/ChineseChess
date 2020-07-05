<%@ page language="java" pageEncoding="UTF-8"%>

<% 
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
 	<link rel="stylesheet" href="<%=path%>/role/css/index.css" type="text/css">
    <link rel="stylesheet" href="<%=path%>/role/css/Chess.css" type="text/css">
    <script src="<%=path%>/role/js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=path%>/role/js/Show.js" type="text/javascript"></script>
    <script src="<%=path%>/role/js/OnDoing.js" type="text/javascript"></script>
    <script src="<%=path%>/role/js/CMoveRule.js" type="text/javascript"></script>
    <script src="<%=path%>/role/js/CChess.js" type="text/javascript"></script>
    <script src="<%=path%>/role/js/socket.js" type="text/javascript"></script>
</head>
</html>