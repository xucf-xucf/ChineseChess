var url = "localhost/ChineseChess";
var websocket = null;
var localSessionId = 0;
var rivalSessionId = 0;
websocket = new WebSocket("ws://"+url+"/websocket");
//判断当前浏览器是否支持WebSocket
/* if ('WebSocket' in window) {
		console.log("=============");
		websocket = new WebSocket("ws://localhost:/websocket");
	} else {
		alert('当前浏览器 Not support websocket');
	} */
//连接发生错误的回调方法
websocket.onerror = function() {
	/* setMessageInnerHTML("WebSocket连接发生错误"); */
	alert("通讯建立失败！");
	console.log("通讯建立失败！");
};
//连接成功建立的回调方法
websocket.onopen = function() {
	/* setMessageInnerHTML("WebSocket连接成功"); */
	console.log("通讯建立成功！id为");
}
//接收到消息的回调方法]o
websocket.onmessage = function(event) {
	var result = event.data;
	console.log("接收到服务器消息: "+result);
	if(result.indexOf("move")>-1){
		var str = result.split(",");
		move(str[0],str[1],str[2],str[3]);
	}else if(result.indexOf("local_session=")>-1){
		localSessionId = result.split("=")[1];
		console.log("通讯建立成功！本家id为"+localSessionId);
	}else if(result.indexOf("rival_session=")>-1){
		rivalSessionId = result.split("=")[1];
		console.log("通讯建立成功！对家id为"+localSessionId);
	}
	
	/* setMessageInnerHTML(event.data); */
}
//连接关闭的回调方法
websocket.onclose = function() {
	console.log("通讯已断开！");
	/* setMessageInnerHTML("WebSocket连接关闭"); */
}
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	closeWebSocket();
}
//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
	document.getElementById('message').innerHTML += innerHTML + '<br/>';
}
//关闭WebSocket连接
function closeWebSocket() {
	websocket.close();
}
//发送消息
function send(message){
	/* var message = document.getElementById('text').value; */
	websocket.send(message);
}
//设置对家
function setRival(){
	var rival = "";
	send("rivalSessionId="+rival);
}
function sendReMove(signo){
	//requestreMove  发起悔棋
	//agreereMove  同意悔棋
	//disAgreereMove   不同意悔棋
	send(signo);
}