<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/includeJsp/common.jsp"%>
<%
/* 	String title1 = "官方公告";
	String title1Path = "notice.jsp";
	String title2 = "会员排行";
	String title2Path = "user/userList.jsp";
	String title3 = "个人信息";
	String title3Path = "user/userDetails.jsp";
	String title4 = "图书管理";
	String title4Path = "bookList.jsp";
	 */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/jquery.js"></script>
<script type="text/javascript">
$(function(){	
	//顶部导航切换
	$(".nav li a").click(function(){
		$(".nav li a.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
})	
</script>


</head>

<!-- <body style="background:url(images/logo.png) repeat-x;">
 --><body >


    <div class="topleft">
    <a href="main.jsp" target="_parent"><img src="images/logo.png" title="系统首页" /></a>
    </div>
        
    <ul class="nav">
   
    <!-- <li><a href="userDetail.jsp" target="rightFrame" class="selected"><img src="images/icon01.png" title="个人信息" /><h2>个人信息</h2></a></li>
    --> <li><a href="onLineFram.jsp" target="rightFrame"><img src="images/icon02.png" title="在线列表" /><h2>在线列表</h2></a></li><!-- 
    <li><a href="gameFram.jsp"  target="rightFrame" class = "game"><img src="images/icon03.png" title="对局" /><h2>对局</h2></a></li> -->
    <li><a href="gameUtil/gameUtilFram.jsp"  target="rightFrame" class="selected"><img src="images/icon04.png" title="打谱复盘" /><h2>打谱复盘</h2></a></li>
    <!--
    <li><a href="computer.html" target="rightFrame"><img src="images/icon05.png" title="文件管理" /><h2>快速定位</h2></a></li>
    <li><a href="tab.html"  target="rightFrame"><img src="images/icon06.png" title="系统设置" /><h2>系统设置</h2></a></li>-->
    </ul>
            
    <%-- <div class="topright">    
    <ul>
    <li><span><img src="images/help.png" title="帮助"  class="helpimg"/></span><a href="#">帮助</a></li>
    <li><a href="#">关于</a></li>
    <li><a href="<%=path %>/loginOut" target="_parent">退出</a></li>
    </ul> 
     
    <div class="user">
    <span></span>
    <!-- <i>消息</i>
    <b>5</b> -->
    </div>    
    
    </div>
    --%>
</body>
</html>
