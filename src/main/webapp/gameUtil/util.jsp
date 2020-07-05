<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台010管理</title>
</head>

<link href="../css/style.css" rel="stylesheet" type="text/css" />
  <script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	qryGame(1);
	
});
function layoutClick(){
	console.log(self.parent.frames["leftFrame"].chessLayout);
	if(!self.parent.frames["leftFrame"].chessLayout){
		var flag = confirm("重新布局？");
		if(flag){
			self.parent.frames["leftFrame"].isFirst = true;
		}else{
			return;
		}
	}
	self.parent.frames["leftFrame"].layoutClick();
	if($("#layout").val()=="开始布局"){
		$("#layout").val("停止布局");
		$("#layout").removeClass("btn-primary");
		$("#layout").addClass("btn-danger");
		$("#tip").val("布局中。。。");
		clear();
	}else{
		$("#layout").val("开始布局");
		$("#tip").val("布局完成");
		$("#layout").removeClass("btn-danger");
		$("#layout").addClass("btn-primary");
		if(temp>0){
			$(".chess input.btn-warning").addClass("btn-danger");
		}else{
			$(".chess input.btn-warning").addClass("btn-success");
		}
		$(".chess input.btn-warning").removeClass("btn-warning");
	}
}
function useInitial(){
	self.parent.frames["leftFrame"].layoutClick();
	self.parent.frames["leftFrame"].useInitial();
	$("#tip").val("布局完成");
}
function useNoneInitial(){
	self.parent.frames["leftFrame"].useNoneInitial();
	$("#tip").val("清空棋盘");
	self.parent.frames["leftFrame"].choseChessClick(0);
}
var temp = 0;
function choseChess(chessNum){
	
	if($("#layout").val()=="开始布局"){
		self.parent.frames["leftFrame"].layoutClick();
		$("#tip").val("布局中。。。");
		$("#layout").val("停止布局");
		$("#layout").removeClass("btn-primary");
		$("#layout").addClass("btn-danger");
	}
 	if(chessNum>0){
		$(".chess input.r"+chessNum).removeClass("btn-danger");
	}else{
		$(".chess input.r"+chessNum).removeClass("btn-success");
	}
 	if(temp>0){
		$(".chess input.btn-warning").addClass("btn-danger");
	}else if (temp<0){
		$(".chess input.btn-warning").addClass("btn-success");
	}else{
		self.parent.frames["leftFrame"].choseChessClick(0);
		$(".chess input.btn-warning").addClass("btn-success");
	}
	$(".chess input.btn-warning").removeClass("btn-warning");
	$(".chess input.r"+chessNum).addClass("btn-warning");
	temp = chessNum;
	self.parent.frames["leftFrame"].choseChessClick(chessNum);
}

function clear(){
	document.getElementById('showCC').innerHTML ="";
}
var websocket = null;
var userName = "<%=userName %>";
var msgBody = {};
msgBody.local = "<%=userId %>";
msgBody.localName = userName;
websocket = new WebSocket("ws://"+url+"/CCDecodeRoom");
//连接发生错误的回调方法
websocket.onerror = function() {
	alert("棋谱通讯建立失败！");
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
	console.log("接收到服务器消息: "+result);
	var json = JSON.parse(result);
	if(json.type =="95"){//打谱走棋消息
		result = json.msgText+ '<br/>';
		document.getElementById('showCC').innerHTML += result;
		var div = document.getElementById("showCC");
		div.scrollTop = div.scrollHeight;
	}
	
};
//连接关闭的回调方法
websocket.onclose = function() {000
	console.log("通讯已断开！");
};
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
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
function send(message){
	/* var message = document.getElementById('text').value; */
	websocket.send(message);
};

function saveGame(){
	var msgBody = self.parent.frames["leftFrame"].getMsgBody(); 
	msgBody.CCEncode = document.getElementById("showCC").innerText;
	msgBody.gameMark = 	$("#gameMark").val();
	console.log(msgBody);
	var json = sendPostSyn(msgBody,"game/saveGame");
	if(json.status == "0"){
		alert("保存成功！");
		qryGame(1);
	}else{
		alert("保存失败！");
	}
}
var pageCount = "";
var pageSize = 5;
var maxPage_ = 0;
var gotoPage = 1;
var currentPage = 1;
countPage = function(qryStr){
	var jsonParam = {};
	jsonParam.UserName = qryStr;
	var json = sendPostSyn(JSON.stringify(jsonParam),"game/countGame");
	if(json.status == "0"){
		var cnt= json.count;
		maxPage_ =  parseInt(cnt%pageSize == 0 ? cnt/pageSize : (cnt/pageSize + 1),10);
		initPage(maxPage_,cnt);
	}else{
		alert("操做失败！"+json.desc);
	}
}
initPage = function(maxPage,cnt){
	var $ctx = $(".page-ctx");
	var pageNo = parseInt($ctx.find(".on").text(),10);
	var pageList = $ctx.find(".page");
	var beginPage = 1, endPage = 5;
	if(maxPage <= 5){
		endPage = maxPage;
	}
	$ctx.find(".page").text("   当前第："+currentPage+"  页  ");
	$ctx.find(".max-page").text(maxPage);
	$ctx.find(".total-cnt").text(cnt);
},
firstPage = function(){
	var pageNo = 1;
	qryGame(pageNo);
},
lastPage = function(){
	var $ctx = $(".page-ctx");
	$ctx.find(".page").removeClass("on").hide();
	var pageList = $ctx.find(".page");
	var pageNo = parseInt(maxPage_,10);
	var beginPage = 1, endPage = 5;
	if(maxPage_ <= 5){
		endPage = maxPage_;
	}else{
		if(pageNo > 3){
			beginPage = pageNo - 2;
			endPage = maxPage_ - 2;
		}
		if(endPage > maxPage_)
			endPage = maxPage_;
	}
	for(var i=0; i < 5 ; i++){
		if(beginPage + i > maxPage_)
			break;
		var $a = $ctx.find(".page").eq(i);
		if(pageNo == (i+beginPage))
				$a.addClass("on");
		$a.text(i+beginPage).show();
	}			
	qryGame(pageNo);
},
prevPage = function(){
	if(currentPage>1){
		qryGame( parseInt(currentPage,10)-1);
	}
},
nextPage = function(){
	if(currentPage<maxPage_){
		qryGame(parseInt(currentPage,10)+1);
	}
},
gotoPage = function($this){
	var $ctx = $(".page-ctx");
	$ctx.find(".page").removeClass("on").hide();
	var pageList = $ctx.find(".page");
	var pageNo = parseInt($this.text(),10);
	var beginPage = 1, endPage = 5;
	if(maxPage_ <= 5){
		endPage = maxPage_;
	}else{
		if(pageNo > 3){
			beginPage = pageNo - 2;
			endPage = maxPage_ - 2;
		}
		if(endPage > maxPage_)
			endPage = maxPage_;
	}
	for(var i=0; i < 5 ; i++){
		if(beginPage + i > maxPage_)
			break;
		var $a = $ctx.find(".page").eq(i);
		if(pageNo == (i+beginPage))
				$a.addClass("on");
		$a.text(i+beginPage).show();
	}
	qryGame(pageNo);
} 
function qryGame(page){
	var $body = $("#data_body");
	var jsonParam ={};
	jsonParam.page = page+"";
	var qryStr = $("#qryStr").val();
	jsonParam.qryStr = qryStr;
	var json = sendPostSyn(JSON.stringify(jsonParam),"game/qryGame");
	if(json.status == "0"){
		var spinfoList = json.gameList;
		renderToData($body,spinfoList);
		currentPage = page;
		countPage(qryStr);
	}else{
		alert("操做失败！"+json.desc);
	} 
}
function editClick(gameId){
	alert(gameId+"开发中");
	
}
function reGameClick(gameMark,gameId){
	alert(gameMark);
	self.parent.parent.frames["rightFrame"].location.href = "reGame/reGameFram.jsp?gameId="+gameId;
}
function importClick(){
	self.parent.parent.frames["rightFrame"].location.href = "import.jsp";
}
renderToData = function($body,spconfigList){
	$body.html("");
	$.each(spconfigList,function(i,n){
		var _tr = "<tr id=\"tr_"+n.game_id+"\">";
	/* 	_tr += "<td title='"+(i+1)+"'><font class='table-query-text'>"+(i+1)+"</font></td>"; */
		_tr += "<td ><a  onClick = \"editClick('"+str(n.game_id)+"')\"></a> <a href='#' class='tablelink' onClick = \"reGameClick('"+n.game_mark+"','"+str(n.game_id)+"')\">复盘</a></td>";
		_tr += "<td title='"+str(n.game_type)+"'><font class='table-query-text'>"+str(n.game_type)+"</font></td>";
		var gameMark = str(n.game_mark);
		if(gameMark!=null && gameMark.length>10){
			gameMark = gameMark.substring(1,9)+"...";
		}
		_tr += "<td title='"+str(gameMark)+"'><font class='table-query-text'>"+str(gameMark)+"</font></td>";
		_tr += "<td title='"+str(n.insert_time)+"'><font class='table-query-text'>"+str(n.insert_time)+"</font></td>";
		_tr += "</tr>";
		$body.append(_tr);
	});
}
</script>
<link rel="stylesheet" href="../dist/css/bootstrap.min.css" />
<body>
					
		
				<div class="panel-body"
						style="width: 100%; height:40%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						<div >
						<input id = "layout" type="button" class="btn btn-primary" value="开始布局"
						onclick="layoutClick();" />
						<input id = "layout" type="button" class="btn btn-primary" value="使用初始棋盘"
						onclick="useInitial();" />
						<input id = "layout" type="button" class="btn btn-primary" value="清空棋盘"
						onclick="useNoneInitial();" />
						<input id = "layout" type="button" class="btn btn-primary" value="导入棋谱"
						onclick="importClick();" />
						</div>
						<input style="color:red; border:none;" id="tip" type="text" class=""  readonly="readonly"/>
						
				<div class="panel-body chess"
						style="width: 100%;; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						红旗
						<div>
						<input  type="button"class="btn-lg btn-danger r1" style="border-radius:25px;"  value="兵"
						onclick="choseChess(1);" />
						<input  type="button"class="btn-lg btn-danger r2" style="border-radius:50px;"  value="炮"
						onclick="choseChess(2);" />
						<input  type="button"class="btn-lg btn-danger r4" style="border-radius:50px;"  value="马"
						onclick="choseChess(4);" />
						<input  type="button"class="btn-lg btn-danger r3" style="border-radius:50px;"  value="車"
						onclick="choseChess(3);" />
						<input  type="button"class="btn-lg btn-danger r6" style="border-radius:50px;"  value="士"
						onclick="choseChess(6);" />
						<input  type="button"class="btn-lg btn-danger r5" style="border-radius:50px;"  value="相"
						onclick="choseChess(5);" />
						<input  type="button"class="btn-lg btn-danger r7" style="border-radius:50px;"  value="帅"
						onclick="choseChess(7);" />
						</div>
						黑棋
						<div>
						<input  type="button"class="btn-lg  btn-success r-1" style="border-radius:50px;"  value="卒"
						onclick="choseChess(-1);" />
						<input  type="button"class="btn-lg  btn-success r-2" style="border-radius:50px;"  value="炮"
						onclick="choseChess(-2);" />
						<input  type="button"class="btn-lg  btn-success r-4" style="border-radius:50px;"  value="马"
						onclick="choseChess(-4);" />
						<input  type="button"class="btn-lg  btn-success r-3" style="border-radius:50px;"  value="車"
						onclick="choseChess(-3);" />
						<input  type="button"class="btn-lg  btn-success r-6" style="border-radius:50px;"  value="士"
						onclick="choseChess(-6);" />
						<input  type="button"class="btn-lg  btn-success r-5" style="border-radius:50px;"  value="象"
						onclick="choseChess(-5);" />
						<input  type="button"class="btn-lg  btn-success r-7" style="border-radius:50px;"  value="将"
						onclick="choseChess(-7);" />
						<input  type="button"class="btn-lg  btn-success r0" style="border-radius:50px;"  value="空"
						onclick="choseChess(0);" />
						</div>
				</div>
	
</div>

				
				
			<div class="panel-body"
						style="width: 95%; height:65%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
				
				<div class="panel-body"
						style="width: 75%; height:400px; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						<form id="form1" method="post"  class="form-group form-inline">
	<%-- 	<input type="text" name="suserName" placeholder="按备注搜索!"
			class="form-control" value="${param.suserName }" /> <input type="button" value="搜 索"
			class="btn btn-warning" /> --%>
	</form>
				<table class="table" style="text-align:center;">
				<!-- 对局列表 -->
					<thead>
						<tr>
							<!-- <th><input name="" type="checkbox" value="" checked="unchecked"/></th>  -->
							<!-- <th>序号<i class="sort"><img src="../images/px.gif" /></i></th> -->
							<th>操作</th>
							<th>类型</th>
							<th>备注</th>
							<th>结束时间</th>
						</tr>
					</thead>
					</thead>
			<tbody id="data_body">
			</tbody>
			
		</table>
		<div class="page-ctx">
			<a onclick="firstPage();">首页</a> <a onclick="prevPage();" class="">上一页</a>

			<a class="page on">1</a>

			<a onclick="nextPage();" class="">下一页</a> <a onclick="lastPage();">尾页</a>
			<span class="">共<font class="max-page">0</font>页，<font
				class="total-cnt">0</font>条
			</span>

		</div>
				</div>
				<div class="panel-body"
						style="width: 25%; height:360px; overflow-y: auto; float:left ;border: 1px solid #333;"
						>
					<div style="width: 55%; ; float:right ;">
					<label for="">添加备注信息</label>
					<textarea id = "gameMark" class="form-control" style="width:100%;height:250px;">
					</textarea>	
					<input  type="button"class="btn-lg  btn-info" 
					style="border-radius:50px; margin-top: 6px;"  value="保存棋谱"
							onclick="saveGame();" />
					</div>
					<div id="showCC" style="width: 45%; overflow-y: auto; float:left ;">
					</div>
				</div> 
				</div> 
</body>

</html>