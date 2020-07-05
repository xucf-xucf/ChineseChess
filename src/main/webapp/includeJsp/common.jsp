<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.ChineseChess.model.User"%>
<% 
	User user = (User)session.getAttribute("user");
	String path = request.getContextPath();
	String userName = "无名";
	String userId = "0";
	String rival = "无";
	String local = "无";
	if(user !=null){
		userName = user.getUserName();
		userId = user.getUserId();
	}
%>
<script type="text/javascript" src="<%=path%>/role/js/jquery.min.js"></script>
<script type="text/javascript">
<%-- var url = "192.168.0.104/<%=path%>"; --%>
<%--  var url = "47.97.191.150:9016<%=path%>";  --%>
 var url = "localhost/<%=path%>";
//调用后台接口，返回json数据
function parseToJSON(data){
	return JSON.parse(JSON.stringify(data));
}

//封装body对应id的vaule到json数据的string对象
function setParam(){
	var obj={};
	var key = "";
	for(var i = 0; i < arguments.length; i++){
		key = arguments[i];
		obj[key]= $("#"+key).val();  
	}
	return JSON.stringify(obj);
}

//同步提交post请求，返回json对象
function sendPostSyn(jsonParam,url){
	var result = {};
	var type = typeof(jsonParam);
	if(type == "object"){
		jsonParam = JSON.stringify(jsonParam);
	}else{
		
	}
	var relUrl = "<%=path %>/"+url;
	$.ajax({
		  url: relUrl,
		  async:false,
		  data :  {param:jsonParam},
			type : "POST",
			dataType : "json",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
		  success: function(data){
			  result = parseToJSON(data);
		  }  
		});
	return result;
}

//异步提交post请求，返回json对象
function sendPostAsyn(jsonParam,url){
	var result = {};
	var relUrl = "<%=path %>/"+url;
	var type = typeof(jsonParam);
	if(type == "object"){
		jsonParam = JSON.stringify(jsonParam);
	}else{
		
	}
	$.ajax({
		  url: relUrl,
		  async:true,
		  data :  {param:jsonParam},
			type : "POST",
			dataType : "json",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
		  success: function(data){
			  result = parseToJSON(data);
		  }  
		});
	return result;
}
str = function(strVal){
	if(strVal == '' || strVal == null || strVal == undefined){
		return "";
	}
	return strVal;
};


</script>