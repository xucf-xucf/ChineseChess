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
<title>公共聊天室</title>
</head>

<script type="text/javascript">
var userName = "<%=userName %>";
	var websocket = new WebSocket("ws://" + url + "/commonRoom");
	var msgBody = {};
	msgBody.local = "<%=userId %>";
	msgBody.localName = userName;
	websocket.onerror = function() {
		alert("公聊通讯建立失败！");
	};
	
	//连接成功建立的回调方法
	websocket.onopen = function() {
		console.log("通讯建立成功！");
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
			var myDate = new Date();
			var h = myDate.getHours()+"";       //获取当前小时数(0-23)
			if(h.length<2){
				h = "0"+h;
			}
			var m = myDate.getMinutes()+"";     //获取当前分钟数(0-59)
			console.log(m.length);
			if(m.length<2){
				m = "0"+m;
			}
			var s = myDate.getSeconds()+"";     //获取当前秒数(0-59)
			if(s.length<2){
				s = "0"+s;
			}
			result = "["+h+":"+m+":"+s+"]  "+json.sendName+" ： "+json.msgText + '<br/>';
			document.getElementById('show').innerHTML += result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;//显示最下行
		}else if(json.type == "12"){//有人请求对战
			var result = confirm("有人挑战");
			msgBody.requester = json.requester;
			msgBody.requesterName = json.requesterName;
			if(result == true){
				msgBody.type = "13";//同意对战
				self.parent.frames["rightFrame"].location.href = "gameFram.jsp";
				self.parent.frames["topFrame"].$(".nav li a.selected").removeClass("selected");
				self.parent.frames["topFrame"].$(".nav li a.game").addClass("selected");
				var jsonAgree = JSON.stringify(msgBody); 
				websocket.send(jsonAgree);//同意对战消息
				
				msgBody.type = "21";//建立私聊连接
				msgBody.responser = json.responser;
				var json = JSON.stringify(msgBody); 
				websocket.send(json);
			}else{
				msgBody.type = "14";//拒绝对战
			}
		}else if(json.type == "13"){//同意对战
			self.parent.frames["rightFrame"].location.href = "gameFram.jsp";
			self.parent.frames["topFrame"].$(".nav li a.selected").removeClass("selected");
			self.parent.frames["topFrame"].$(".nav li a.game").addClass("selected");
		}else if(json.type == "99"){
			alert("对局结束！");
		}
	};
	//连接关闭的回调方法
	websocket.onclose = function() {
		console.log("通讯已断开！");
	};
	
	window.onbeforeunload = function() {
		//closeWebSocket();
		websocket.close();
	};
	//关闭WebSocket连接
	function closeWebSocket() {
		msgBody.type = "0"; 
		var json = JSON.stringify(msgBody); 
		websocket.send(json);
	};
	//发送消息
	function send() {
		var message = document.getElementById('msg').value;
		msgBody.msgText = message; 
		msgBody.type = "1"; 
		var json = JSON.stringify(msgBody); 
		websocket.send(json);
		//document.getElementById('msg').value = "";
	};

</script>

<body>
	 <div class="panel-heading">公共聊天室</div> 
	<div class = "form-control" 
		style="width: 100%; height: 70%; overflow-y: auto; border: 1px solid #333;"
		id="show"></div>
		输入公共频道聊天消息：
		<textarea name="a" id = "msg" name="msg" style="width:100%;height:80px;" class="form-control"></textarea>
	<div class="btn-group btn-group-justified" role="group"
		aria-label="...">
		<div class="btn-group" role="group">
			<input type="button" class="btn btn-warning" value="发送"
				onclick="send();" />
		</div>
	</div>

</body>
</html>