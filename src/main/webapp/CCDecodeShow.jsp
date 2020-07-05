<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="dist/css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery.min.js"></script>

<link rel="stylesheet" href="dist/css/bootstrap.min.css" />
<title>走棋棋谱显示</title>
</head>

<script type="text/javascript">
var userName = "<%=userName %>";
	var websocket = new WebSocket("ws://" + url + "/CCDecodeRoom");
	var msgBody = new Object();
	msgBody.local = "<%=userId %>";
	msgBody.localName = userName;
	msgBody.sendName = userName;
	websocket.onerror = function() {
		alert("通讯建立失败！");
		console.log("通讯建立失败！");
	};
	
	//连接成功建立的回调方法
	websocket.onopen = function() {
		msgBody.type = "20";
		var json = JSON.stringify(msgBody); 
		websocket.send(json);
	};
	//接收到消息的回调方法]o
	websocket.onmessage = function(event) {
		var result = event.data;
		console.log("接收到服务器消息: " + result);
		var json = JSON.parse(result);
		if(json.type == "1"){//消息
			result = json.msgText + '<br/>';
			document.getElementById('show').innerHTML += result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;
		}else if(json.type == "11"){//设置对家消息
			result = "对局开始**红方:"+json.requesterName+"**黑方:"+json.responserName + '<br/>';
			document.getElementById('show').innerHTML += result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;
		}else if(json.type == "5"){//走棋数据
			result = json.msgText+ '<br/>';
			document.getElementById('show').innerHTML += result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;
		}else if(json.type == "6"){//倒退一步
			result = json.msgText;
			document.getElementById('show').innerHTML = result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;
		}
	};
	//连接关闭的回调方法
	websocket.onclose = function() {
		console.log("通讯已断开！");
		/* setMessageInnerHTML("WebSocket连接关闭"); */
	};
	window.onbeforeunload = function() {
		closeWebSocket();
	};
	//关闭WebSocket连接
	function closeWebSocket() {
		msgBody.type = "0"; 
		var json = JSON.stringify(msgBody); 
		websocket.send(json);
		websocket.close();
	};
	//发送消息
	function send() {
		 var message = document.getElementById('msg').value;
		websocket.send(message);
		//document.getElementById('msg').value = "";
	};


</script>

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
	<!-- <div class="container">
		<div class="row clearfix">
			<div class="col-md-8 column">
				<div class="panel-heading">
				棋谱
				</div>
				<div class="panel-body"
					style="width: 100%; height: 100%; overflow-y: auto; border: 1px solid #333;"
					id="show">
				</div>
			</div>
			<div class="col-md-4 column">
				<input type="button" class="btn btn-default" value="导出"
					onclick="send();" />
			</div>
		</div>
	</div> -->



</body>
</html>