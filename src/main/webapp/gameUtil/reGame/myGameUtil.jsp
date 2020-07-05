<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <%
    String gameId = request.getParameter("gameId");  
    %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台010管理</title>
</head>
    <script type="text/javascript" src="../../js/jquery.js"></script>

<link rel="stylesheet" href="../../dist/css/bootstrap.min.css" />
<script type="text/javascript">
$(document).ready(function(){
	qryGameById(<%=gameId%>);
	
});
function qryGameById(gameID){
	/* var jsonParam ={};
	jsonParam.gameId = gameID;
	var json = sendPostSyn(JSON.stringify(jsonParam),"game/qryGameById");
	if(json.status == "0"){
		var moveJson = {};
		$.each(json.moveList,function(i,n){
			moveJson.beforeY = n.beforeY;
			moveJson.beforeX = n.beforeX;
			moveJson.afterY = n.afterY;
			moveJson.afterX = n.afterX;
			moveJson.currentCount = n.currentCount;
			moveArray[i] =JSON.stringify(moveJson); 
		});
	}else{
		$("#error3").val(json.desc);
	} */
}
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
var count = 0;
function next(){
	self.parent.frames["leftFrame"].next(count);
	$("#tip").val(self.parent.frames["leftFrame"].CCEncode[count]);
	document.getElementById('showCC').innerHTML += self.parent.frames["leftFrame"].CCEncode[count]+"</br>";
	count = count+1;
	var div = document.getElementById("showCC");
	div.scrollTop = div.scrollHeight;
}
</script>
<body>
<div  class="panel-body">
<div class="panel-body"
						style="width: 49%; height:95%; overflow-y: auto; float:left ;border: 1px solid #333;"
						id="show">
						<div style="text-align:center;">
						<input type="button" class="btn btn-warning" value="下一步"
						onclick="next();" />
						</div>
						<div style="text-align:center; margin-top: 6px;">
						当前：
						<input style="color:red; border:none;" id="tip" type="text" class=""  readonly="readonly"/>
						</div>
						<div style="text-align:center; margin-top: 60px;" >
						<input type="button" class="btn btn-warning" value="返回上一步"
						onclick="next();" />
						</div>
				</div>
				<div class="panel-body"
						style="width: 49%; height:95%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="showCC">
						
</div>
</div>
</body>
</html>