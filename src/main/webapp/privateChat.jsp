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
<title>Insert title here</title>
</head>

<script type="text/javascript">
var userName = "<%=userName %>";
	var websocket = new WebSocket("ws://" + url + "/privateRoom");
	var msgBody = new Object();
	msgBody.local = "<%=userId %>";
	msgBody.localName = userName;
	msgBody.sendName = userName;
	websocket.onerror = function() {
		alert("通讯建立失败!!！");
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
			div.scrollTop = div.scrollHeight;
		}else if(json.type == "11"){//设置对家消息
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
			if(json.requester != null){
				result = "["+h+":"+m+":"+s+"] 玩家 "+json.requesterName+" ： "+json.msgText + '<br/>';
				if(json.requester!=msgBody.local){
					msgBody.rival = json.requester;
				}
			}
			if(json.responser != null){
				result += "["+h+":"+m+":"+s+"] 玩家 "+json.responserName+" ： "+json.msgText + '<br/>';
				if(json.responser!=msgBody.local){
					msgBody.rival = json.responser;
				}
			}
			document.getElementById('show').innerHTML += result;
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
	function send(flag) {
		
		msgBody.type = flag+"";
		if(flag == 1){
			var msgText = document.getElementById('msg').value;
			msgBody.msgText = msgText;
			var myDate = new Date();
			var h = myDate.getHours()+"";       //获取当前小时数(0-23)
			if(h.length<2){
				h = "0"+h;
			}
			var m = myDate.getMinutes()+"";     //获取当前分钟数(0-59)
			if(m.length<2){
				m = "0"+m;
			}
			var s = myDate.getSeconds()+"";     //获取当前秒数(0-59)
			if(s.length<2){
				s = "0"+s;
			}
			var result = "["+h+":"+m+":"+s+"]  "+userName+" ： "+msgText + '<br/>';
			document.getElementById('show').innerHTML += result;
			var div = document.getElementById("show");
			div.scrollTop = div.scrollHeight;
		}else if(flag = 3){//悔棋
			
		}
		var json = JSON.stringify(msgBody); 
		sendMsg(json);
	};

	function sendMsg(message){
		websocket.send(message);
	}

</script>

<body>
	 <div class="panel-heading">私人聊天室</div> 
	<div class = "panel-body" 
		style="width: 100%; height: 50%; overflow-y: auto; border: 1px solid #333;"
		id="show"></div>
	
		输入私聊频道聊天消息：
		<textarea name="a" id = "msg" name="msg" style="width:100%;height:80px;" class="form-control"></textarea>
	
	<div class="btn-group btn-group-justified" role="group"
		aria-label="...">
		<div class="btn-group" role="group">
			<input type="button" class="btn btn-warning" value="发送"
				onclick="send(1);" />
		</div>
	</div>
	
	 <div class="panel-heading">请求</div> 
	
	<div class="btn-group btn-group-justified" role="group" aria-label="...">
  <div class="btn-group" role="group">
    <button type="button" class="btn btn-danger" onclick="send(2);">认输</button>
  </div>
  <div class="btn-group" role="group">
    <button type="button" class="btn btn-success" onclick="send(3);">悔棋</button>
  </div>
  <div class="btn-group" role="group">
    <button type="button" class="btn btn-info" onclick="send(4);">求和</button>
  </div>
</div>

</body>
</html>