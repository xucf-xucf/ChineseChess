<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/includeJsp/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录...</title>
<link href="<%=path%>/css/style.css" rel="stylesheet" type="text/css" />

<style>
body {
	font-family: "微软雅黑";
	font-size: 12px;
	background-color: #6495ED;
}

#loginer {
	position: relative;
	/* 相对定位 */
	width: 400px;
	border: solid 5px #87CEFF;
	margin: 200px auto 0px;
	/* 上   左右   下     的边界 */
	border-radius: 10px;
	/* 圆角 */
	background-color: #FFF;
}

#loginer dl dt {
	font-size: 14px;
	font-weight: bold;
	/* 加粗 */
	margin: 20px 0px;
	/* 上下  左右    的边界 */
	text-align: center;
	/* 水平居中 */
	letter-spacing: 3px;
	/* 文字间距 */
}

#loginer dl dd {
	text-align: center;
	margin: 15px 0px;
}

.input {
	width: 300px;
	height: 26px;
	text-indent: 24px;
	/* 缩进 */
}

.input-user {
	/* background-image: url("img/input_icons_24.png"); */
	background-repeat: no-repeat;
	background-position-y: -68px;
}

.input-password {
	/* background-image: url("img/input_icons_24.png"); */
	background-repeat: no-repeat;
	background-position-y: -146px;
}

.btn {
	width: 300px;
	height: 30px;
	background-color: #6495ED;
	border: none;
	/* 去除边框 */
	color: #FFF;
	/* 字体颜色 */
	cursor: pointer;
	/* 鼠标指针样式：小手型 */
}

.btn:hover {
	background-color: #5CACEE;
}
</style>
<script type="text/javascript">

function regeditDIV(){
	document.getElementById("hiddenDiv").style.display = "block"; 
}
</script>
</head>

<body>
	<div id="loginer" id = "unHiddenDIV" style="display: block;">
			<dl>
				<!-- 定义列表 -->
				<!--注释快捷键 ctrl+shift+/ -->
				<dt>用户帐号登录</dt>
				<!-- 标题 -->
				<dd>
					<input type="text" name="userAccount" id= "userAccount" class="input input-user"
						placeholder="账号" autofocus required value="${param.userName }" />
				</dd>
				<!-- 描述 -->
				<dd>
					<input type="password" name="password" id = "userPwd" class="input input-password"
						placeholder="密码" required value="${param.password }" />
				</dd>
				<dd><span style="color:red;">${error }</span></dd>
				<dd>
				<input style="color:red;" id="error3" type="lable" class="" />
					<input type="button"   onClick="login()" value="登 录" class="btn" />
					<input type="button" onClick="regeditDIV()" value="注册" class="btn" />
				</dd>
			</dl>
	</div>
	<div class="tip" id="hiddenDiv" style="display: none;">
    	<div class="tiptop"><span>注册</span><a></a></div>
        
      <div class="tipinfo">
        <div class="tipright">
        <p><h1>账号:<input id="regeditAccount" type="text" class="qryinput" /><input style="color:red;" id="error" type="lable" class="" /></h2></p>
        </div>
         <div class="tipright">
        <p><h1>称呼:<input id="name" type="text" class="qryinput" /></h2></p>
        	 </div>
          <div class="tipright">
        <p><h1>密码:<input id="regeditPwd" type="text" class="qryinput" /><input style="color:red;" id="error2" type="lable" class="" /></h2></p>
        	 </div>
       
        </div>
        
        <div class="tipright">
        <input name="" type="button" onClick="ok()" class="btn" value="注册" />&nbsp;
        <input name="" type="button" onClick="cancle()"  class="btn" value="取消" />
        </div>
    
    </div>
</body>
<script type="text/javascript">

function ok(){
	var acc = $("#regeditAccount").val();
	var pwd = $("#regeditPwd").val();
	console.log(acc+"---"+pwd);
	if(pwd==null||pwd==""){
		$("#error2").val("密码no为空！！");
		return;
	}
	if(acc==null||acc==""){
		$("#error").val("账号no为空！！");
		return;
	}
	var jsonParam = setParam("regeditAccount","name","regeditPwd");
	var json = sendPostSyn(jsonParam,"regedit");
	if(json.status == "0"){
		alert("register success!!!");
		document.getElementById("hiddenDiv").style.display = "none"; 
		$("#userAccount").val(acc);
		$("#userPwd").val(pwd);
	}else{
		$("#error").val(json.desc);
	}
}

function cancle(){
	document.getElementById("hiddenDiv").style.display = "none"; 
}
function login(){
	var jsonParam = setParam("userAccount","userPwd");
	var json = sendPostSyn(jsonParam,"user/login");
	if(json.status == "0"){
		document.location = "<%=path%>/index";
	}else{
		$("#error3").val(json.desc);
	}
}
</script>
</html>