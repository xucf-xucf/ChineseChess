<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>

<link rel="stylesheet" href="dist/css/bootstrap.min.css" />
<script type="text/javascript">
var pageCount = "";
$(document).ready(function(){
	qryUser("1");
	
  $(".addClick").click(function(){
  $(".addTip").fadeIn(200);
  addClick();
  });
  $(".editClick").click(function(){
//  $(".editTip").fadeIn(200);
  });
  $(".query").click(function(){
	  var qryStr = $("#qryStr").val();
	  qryUserByStr(qryStr);
  });

  $(".tiptop a").click(function(){
  $(".editTip").fadeOut(200);
  $(".addTip").fadeOut(200);
});
});

function qryUserByStr(qryStr){
	var $body = $("#data_body");
	var jsonParam = {};
	jsonParam.UserName = qryStr;
	jsonParam.page = "1";
	var json = sendPostSyn(JSON.stringify(jsonParam),"qryUser");
	if(json.status == "0"){
		var spinfoList = json.userList;
		renderToData($body,spinfoList);
		countPage(qryStr);
	}else{
		alert("操做失败！"+json.desc);
	}
}
function qryUser(page){
	var $body = $("#data_body");
	var jsonParam ={};
	jsonParam.page = page+"";
	var qryStr = $("#qryStr").val();
	jsonParam.UserName = qryStr;
	var json = sendPostSyn(JSON.stringify(jsonParam),"qryUser");
	if(json.status == "0"){
		var spinfoList = json.userList;
		renderToData($body,spinfoList);
		currentPage = page;
		countPage(qryStr);
	}else{
		alert("操做失败！"+json.desc);
	} 
}
renderToData = function($body,spconfigList){
	$body.html("");
	$.each(spconfigList,function(i,n){
		var _tr = "<tr id=\"tr_"+n.user_id+"\">";
		_tr += "<td title='"+(i+1)+"'><font class='table-query-text'>"+(i+1)+"</font></td>";
		_tr += "<td title='"+str(n.user_name)+"'><font class='table-query-text'>"+str(n.user_name)+"</font></td>";
		_tr += "<td title='"+str(n.user_desc)+"'><font class='table-query-text'>"+str(n.user_desc)+"</font></td>";
		_tr += "<td title='"+str(n.user_game_count)+"'><font class='table-query-text'>"+str(n.user_game_count)+"</font></td>";
		_tr += "<td title='"+str(n.user_win_rate)+"'><font class='table-query-text'>"+str(n.user_win_rate)+"</font></td>";
		_tr += "<td title='"+str(n.user_div_game_count)+"'><font class='table-query-text'>"+str(n.user_div_game_count)+"</font></td>";
		_tr += "<td ><a  onClick = \"requestClick('"+str(n.user_id)+"')\">发起挑战</a> </td>";
		_tr += "</tr>";
		$body.append(_tr);
	});
}
requestClick = function(userId){
	var jsonParam ={};
	jsonParam.rival = userId;
	if(<%=userId%> == userId){
		alert("和自己玩，请移步打谱复盘！");
		return;
	}
	var json = sendPostSyn(JSON.stringify(jsonParam),"game/requestGame");
	if(json.status == "1"){
		alert(json.desc);
	}else{
		
	}
}
addClick = function(){
	var jsonParam ={};
	var json = sendPostSyn(JSON.stringify(jsonParam),"qryRole");
	var spconfigList = json.roleList;
	var $body = $("#addRoleId");
	$body.html("");
	$.each(spconfigList,function(i,n){
		var opt = " <option value='"+n.role_id+"'>"+n.role_name+"</option>";
		$body.append(opt);
	});
}
var pageSize = 10;
var maxPage_ = 0;
var gotoPage = 1;
var currentPage = 1;
countPage = function(qryStr){
	var jsonParam = {};
	jsonParam.UserName = qryStr;
	var json = sendPostSyn(JSON.stringify(jsonParam),"countUser");
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
	qryUser(pageNo);
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
	qryUser(pageNo);
},
prevPage = function(){
	if(currentPage>1){
		qryUser( parseInt(currentPage,10)-1);
	}
},
nextPage = function(){
	if(currentPage<maxPage_){
		qryUser(parseInt(currentPage,10)+1);
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
	qryUser(pageNo);
} 
function editClick(id){
	var jsonParam ={};
	jsonParam.userId = id;
	var json = sendPostSyn(JSON.stringify(jsonParam),"qryUserById");
	if(json.status == "0"){
		$("#userId").val(json.user.user_id);
		$("#userName").val(json.user.user_name);
		$("#userAccount").val(json.user.user_account);
		$("#userPassword").val(json.user.user_password);
		$("#userDesc").val(json.user.user_desc);
		$("#userRole").val(json.user.user_role);
		var userRole = json.user.user_role ==null?"":json.user.user_role;
		var jsonParam ={};
		var jsons = sendPostSyn(JSON.stringify(jsonParam),"qryRole");
		var spconfigList = jsons.roleList;
		var $body = $("#roleId");
		$body.html("");
		$.each(spconfigList,function(i,n){
			var flag = n.role_name==userRole?" selected ":"";
			var opt = " <option value='"+n.role_id+"'"+flag+">"+n.role_name+"</option>";
			$body.append(opt);
		});
		
		$(".editTip").fadeIn(200);
	}else{
		alert("操做失败！"+json.desc);
	}
}

function delClick(id){
		var jsonParam ={};
		jsonParam.userId = id;
		var json = sendPostSyn(JSON.stringify(jsonParam),"delUser");
		if(json.status == "0"){
			alert("删除成功！");
			 qryUser("1");
		}else{
			alert("操做失败！"+json.desc);
		}
}
function editSure(){
	var jsonParam = setParam("roleId","userName","userDesc","userPassword","userId");
	var json = sendPostSyn(jsonParam,"editUser");
	if(json.status == "0"){
		alert("修改成功！");
		 $(".editTip").fadeOut(200);
		 qryUser("1");
	}else{
		alert("操做失败！"+json.desc);
	}
}
function adddSure(){
	var jsonParam = setParam("addRoleId","addUserName","addUserDesc","addUserPassword","addUseAccount");
	var json = sendPostSyn(jsonParam,"addUser");
	if(json.status == "0"){
		alert("添加成功！");
		 $(".addTip").fadeOut(200);
		 qryUser("1");
	}else{
		alert("操做失败！"+json.desc);
	}
}
</script>


</head>


<body>

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="main.html">首页</a></li>
			<li><a href="#">在线列表</a></li>
		</ul>
	</div>

	<div class="rightinfo">

		<div class="tools">

			<!-- <ul class="toolqrybar">
				<li><label>关键字查询:</label></li>
				<li><input id="qryStr" type="text" class="qryinput" /></li>

				<li class="query"><span>
				</span>查询</li>
			</ul>
 -->

			<!-- <ul class="toolbar1">
				<li class="addClick"><span><img src="images/t01.png" /></span>新增</li>
			</ul>
 -->
		</div>


		<table class="table table-striped">
			<thead>
				<tr>
					<!-- <th><input name="" type="checkbox" value="" checked="unchecked"/></th>  -->
					<th>序号<i class="sort"><img src="images/px.gif" /></i></th>
					<th>姓名</th>
					<th>签名</th>
					<th>对局数</th>
					<th>胜</th>
					<th>打谱数</th>
					<th>操作</th>
				</tr>
			</thead>
			</thead>
			<tbody id="data_body">
			</tbody>
			<tbody>
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

		<div class="addTip">
			<div class="tiptop">
				<span>添加数据</span><a></a>
			</div>
			<div class="row clearfix">
				<div class="col-md-12 column">
					邀请成功
				</div>
			</div>
		</div>
		





	</div>

	<script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>
</body>
</html>
