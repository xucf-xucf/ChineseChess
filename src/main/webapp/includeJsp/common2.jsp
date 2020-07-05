<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.ChineseChess.model.UserBean"%>

<script type="text/javascript" src="../role/js/jquery.min.js"></script>
<script type="text/javascript">
var temp ;
window.onload = function(){
}
function sc(){
	if(temp !=null){
		setInnerHTML(temp);
	}
}
function getChat() {
	 return this.websocket;
};
function setInnerHTML(innerHTML) {
	document.getElementById('message').innerHTML += innerHTML + '<br/>';
};
function sendMsgChat() {
	 var message = document.getElementById('editMsg').value;  
	 parent.window.sendMsg(message);
	
};
</script>