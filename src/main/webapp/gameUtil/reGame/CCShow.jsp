<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="../dist/css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../js/jquery.min.js"></script>

<link rel="stylesheet" href="dist/css/bootstrap.min.css" />
<title>走棋棋谱显示</title>
</head>

<body>
<!-- <div class="panel-heading">
				棋谱
				</div> -->
<div class="panel-heading">
				<input type="button" class="btn btn-default" value="导出	棋谱"
					onclick="send();" />
				</div>
				<div class="panel-body"
					style="width: 95%; height: 85%; overflow-y: auto; float:right ;border: 1px solid #333;"
					id="show">
				</div>
</body>
</html>