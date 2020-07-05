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
    <script type="text/javascript" src="../js/jquery.js"></script>

<link rel="stylesheet" href="../dist/css/bootstrap.min.css" />
<script type="text/javascript">
$(document).ready(function(){
	qryGameById(<%=gameId%>);
	
});
function qryGameById(gameID){
	alert(gameID);
}
function next(){
	self.parent.frames["leftFrame"].test();
}
</script>
<body>
<div  class="panel-body">
<div class="panel-body"
						style="width: 49%; height:45%; overflow-y: auto; float:left ;border: 1px solid #333;"
						id="show">
						<div style="text-align:center;">
						<input type="button" class="btn btn-warning" value="下一步"
						onclick="next();" />
						</div>
				</div>
				<div class="panel-body"
						style="width: 49%; height:45%; overflow-y: auto; float:right ;border: 1px solid #333;"
						id="show">
						ppppppp</br>
						ppppppp</br>
						ppppppp</br>
						ppppppp</br>
						ppppppp</br>
						ppppppp</br>
						ppppppp</br>
</div>
</div>
</body>
</html>