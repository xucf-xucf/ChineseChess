<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/includeJsp/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../dist/css/bootstrap.min.css" />
</head>

    <script type="text/javascript">
        function UpladFile() {
            var fileObj = document.getElementById("file").files[0]; // 获取文件对象
            var FileController = "<%=path%>/file/upload";                    // 接收上传文件的后台地址 
            // FormData 对象
            var form = new FormData();
            form.append("author", "hooyes");                        // 可以增加表单数据
            form.append("file", fileObj);                           // 文件对象
            // XMLHttpRequest 对象
            var xhr = new XMLHttpRequest();
            xhr.open("post", FileController, true);
            xhr.onload = function () {
            	document.getElementById('show').innerHTML = "";
            	document.getElementById('show').innerHTML += xhr.response;
				var div = document.getElementById("show");
				div.scrollTop = div.scrollHeight;
                
            };
            xhr.send(form);
        }
        function importSure(){
        		alert("保存");
        	var msgBody = {};
        	msgBody.local = "<%=userId%>";
        	msgBody.CCEncode = document.getElementById("show").innerText;
        	msgBody.gameMark = 	$("#gameMark").val();
        	var json = sendPostSyn(msgBody,"game/saveGame");
        	if(json.status == "0"){
        		alert("保存成功！自动反回上一页。");
        		location.href = "gameUtilFram.jsp";
        	}else{
        	}
        }
</script>


<body>
<section class="content">
		<dl style="width:80%; height: 95%; overflow-y: auto; margin:0px auto;border: 1px solid #333;">
			<dt>导入棋谱</dt>
			<dd>
			<input type="file" id="file" name="file" 
			style="width: 200px; height: 30px; overflow-y: auto; 
					border: 1px solid #333; display:inline;"/>
			<input type="button" onclick="UpladFile()" value="导入校验" class="btn btn-primary"
			style="overflow-y: auto ;border: 1px solid #333;"/>
				</dd>
			<dt>添加备注：</dt>
			<dd>
<textarea name="a" id = "gameMark" name="gameMark" style="width:70%;height:50px;" class="form-control">	</textarea>
		<input type="button"  onclick="importSure();" value="确定导入" class="btn btn-primary" 
				style="overflow-y: auto ;border: 1px solid #333;display:inline;"/> 
			</dd>
			<dd>
					<div class="panel-body"
					style="width: 40%; height: 95%; overflow-y: auto; 
					float:left ;
					/* margin:0px auto; */border: 1px solid #333;"
					id="show">
				</div>
			</dd>
			<dd>
					<div class="panel-body"
					style="width: 60%; height: 95%; overflow-y: auto; 
					float:left ;
					/* margin:0px auto; */border: 1px solid #333;"
					id="">
					<span style="color:red;">备注：导入的棋谱请有一定的规律。导入的棋谱从初始棋局开始 。文件内容为文字</span>
					</br>
							<span style="color:red;">例1：</span>	</br>
						红方：玩家1	黑方：玩家2</br>
						炮二平五		马8进7</br>
						马二进三		车9平8</br>
						车一平二		炮8进2</br>
						炮八平七		马2进3</br>
						炮七进4		卒7进1</br>
						车二进4		车1平2</br>
						<span style="color:red;">例2：</span>	</br>
						红方：玩家1，黑方：玩家2</br>
						炮二平五，马8进7</br>
						马二进三	，车9平8</br>
						车一平二	，炮8进2</br>
						炮八平七	，马2进3</br>
						炮七进4	，卒7进1</br>
						车二进4	，车1平2</br>
						<span style="color:red;">例3：</span>	</br>
						红方：玩家1	
						</br>黑方：玩家2
						</br>炮二平五
						</br>马8进7
						</br>马二进三
						</br>车9平8
						</br>车一平二
						</br>炮8进2
						</br>炮八平七
						</br>马2进3
						</br>炮七进4 
						</br>卒7进1
						</br>车二进4 
						</br>车1平2
						 <p>
						 <span style="color:red;">说明：</span></br>
					中国象棋的记谱方法一般由四个字组成
 					第1字是棋子的名称。如“马”或“車”。 </br>
					第2字是棋子所在纵线的数码。 </br>
					第3字表示棋子移动的方向：横走用“平”，向前走用“进”或“上”，向后走用“退”或“下”。 </br>
 					第4字是棋子进退的格数，或者到达纵线的数码。
					 <p><span style="color:red;">例：</span>	</br>
					“炮二平五”，表示红炮从纵线二平移到纵线五。 </br>
					“马8进7”，表示黑马从纵线8向前走到纵线7。</br>
					“車2退3”，表示黑車沿纵线2向后移动3格</br>
					当一方有2个以上名称相同的棋子位于同一纵线时，需要用“前”或“后”来加以区别。例如，“前马退六”（表示前面的红马退到直线六）、
					“后炮平4”（表示后面的黑炮平移到直线4）</br>
					<p>兵卒在特殊局面下的记谱方法(主要创作排局时遇到的多，实战也可遇到)</br>
					当兵卒在同一纵线达到3个，用前中后来区分，达到4个，用前二三四区分，达到5个，用前二三四五区分．</br>
					当兵卒在两个纵线都达到两个以上时，按照旧的记谱方式举例：前兵九平八，此时可省略兵（卒），记做前九平八，
					以达到都用4个汉字记谱的要求，此表示方式已在中国象棋DhtmlXQ动态棋盘上实现，是对中文记谱方法的一个重要完善．</br>
				</div>
			</dd>
		</dl>
	</section>
</body>
</html>