<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/includeJsp/common.jsp"%>
    <%
    String gameId = request.getParameter("gameId");  
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

 	<link rel="stylesheet" href="<%=path%>/role/css/index.css" type="text/css">
    <link rel="stylesheet" href="<%=path%>/role/css/Chess.css" type="text/css">
    <script type="text/javascript" src="<%=path%>/role/js/jquery.min.js"></script>
    <script type="text/javascript">
    
//    var url = "localhost/ChineseChess";
    var websocket = null;
    var gameId = <%=gameId%>; //对局id
    var rivalSessionId = 0;  //对家id
    var map=[];         //棋局状态
    var runNow=false;   //可以走
    var DeBug=true;   
    var onMove=false;   
    var OnChoseNow=false;
    var nowChoseC=[];
    var nowWho=0;//0红 1黑
    var moveList=[];
    var eatList=[];
    var sureReMove = 0; //0不给悔棋
    //初始化
$(document).ready(function(){
    LoadGround();
    putDef();
    showC();
	Log("ready");
    runNow=true;
    readyGame();
});
//log日志辅助
function Log(info){
    if(DeBug){
        console.log("DEBUG:"+info);
    }
};
//错误日志
function LogError(info){
    if(DeBug){
        console.log("ERROR:"+info);
    }
};

//0空
//兵1 炮2 车3 马4 相5 士6 将7 红
//卒-1 炮-2 车-3 马-4 象-5 士-6 帅-7 黑

//默认棋子安放
function putDef(){
    map[0][0]=-3; map[9][0]=3;
    map[0][1]=-4; map[9][1]=4;
    map[0][2]=-5; map[9][2]=5;
    map[0][3]=-6; map[9][3]=6;
    map[0][4]=-7; map[9][4]=7;
    map[0][5]=-6; map[9][5]=6;
    map[0][6]=-5; map[9][6]=5;
    map[0][7]=-4; map[9][7]=4;
    map[0][8]=-3; map[9][8]=3;

    map[2][1]=-2; map[7][1]=2;
    map[2][7]=-2; map[7][7]=2;
    map[3][0]=-1; map[6][0]=1;
    map[3][2]=-1; map[6][2]=1;
    map[3][4]=-1; map[6][4]=1;
    map[3][6]=-1; map[6][6]=1;
    map[3][8]=-1; map[6][8]=1;
    Log("完成放置默认棋子");
};
//返回相应位置的棋子
function WhatSpace(y,x){
    return map[y][x];
};
//判断是否能吃
function CanEat(y,x,c){
	
    var cc=0;
    if(c==0){
        cc=1;
    }else{
        cc=-1;
    }
    return map[y][x]*cc<0;
};

//0空
//兵1 炮2 车3 马4 相5 士6 帅7 红
//卒-1 炮-2 车-3 马-4 象-5 士-6 将-7 黑
//判断哪里可以走
function WhereCan(y,x,t){//0可以走 1可以吃
    var c=0;
    if(t<=0){
        c=1;
        t*=-1;
    }
    var tmap=[];
    switch (t){
        case 1:
            binMove(tmap,c,y,x);
            break;
        case 2:
            paoMove(tmap,c,y,x);
            break;
        case 3:
            juMove(tmap,c,y,x);
            break;
        case 4:
            maMove(tmap,c,y,x);
            break;
        case 5:
            xiangMove(tmap,c,y,x);
            break;
        case 6:
            shiMove(tmap,c,y,x);
            break;
        case 7:
            JSMove(tmap,c,y,x);
            break;
        default :
            break;
    }
    for(var l=0;l<tmap.length;l++){
        if(CanEat(tmap[l][0],tmap[l][1],c)){
            tmap[l][2]=1;
        }else{
            tmap[l][2]=0;
        }
    }
    return tmap;
};
function binMove(tmap,c,y,x){//0红 1黑
    var w;
    var h=0;
    if(c==0){
        w=y<5;
        h=-1;
    }else{
        w=y>4;
        h=1;
    }
    if(w){
        if(y+h>=0&&y+h<map.length){
            var t1=[];
            t1[0]=y+h;
            t1[1]=x;
            tmap.push(t1);
        }
        var t2=[];var t3=[];
        t2[0]=y;t3[0]=y;
        t2[1]=x-1;t3[1]=x+1;
        tmap.push(t2);tmap.push(t3);
    }else{
        var t=[];
        t[0]=y+h;
        t[1]=x;
        tmap.push(t);
    }
};
function paoMove(tmap,c,y,x){
    paoMove_(tmap,0,c,y,x);
    paoMove_(tmap,1,c,y,x);
    paoMove_(tmap,2,c,y,x);
    paoMove_(tmap,3,c,y,x);
};
function paoMove_(tmap,d,c,y,x){//0上1左2下3右
    var q= y,w= x,qi= 0,wi= 0,ci=0;//ci:0红 1黑
    if(c==0){
        ci=1;
    }else{
        ci=-1;
    }
    var cc;
    switch (d){
        case 0:
            cc=function(q){return q>=0;}
            qi=-1;
            break;
        case 1:
            cc=function(q,w){return w>=0;}
            wi=-1;
            break;
        case 2:
            cc=function(q){return q<map.length;}
            qi=1;
            break;
        case 3:
            cc=function(q,w){return w<map.length;}
            wi=1;
            break;
    }
    var ce=false;
    while(true){
        if(!cc(q,w))break;
        if(q==y&&w==x){
            q+=qi;w+=wi;
            continue;
        }
        if(map[q][w]==0){
            if(!ce){
                var t=[];
                t[0]=q;
                t[1]=w;
                tmap.push(t);
            }
        }else{
            if(ce){
                if(map[q][w]*ci<0){
                    var t=[];
                    t[0]=q;
                    t[1]=w;
                    tmap.push(t);
                    ce=false;
                    break;
                }
            }
            ce=true;
        }
        q+=qi;w+=wi;
    }
};
function juMove(tmap,c,y,x){
    for(var q=y;q>=0;q--){
        if(q==y)continue;
        if(!fastMove(tmap,c,q,x))break;
    }
    for(var q=x;q>=0;q--){
        if(q==x)continue;
        if(!fastMove(tmap,c,y,q))break;
    }
    for(var q=y;q<map.length;q++){
        if(q==y)continue;
        if(!fastMove(tmap,c,q,x))break;
    }
    for(var q=x;q<map.length;q++){
        if(q==x)continue;
        if(!fastMove(tmap,c,y,q))break;
    }
};
function fastMove(tmap,c,y,x){//c:0红 1黑
    var ci=0;
    if(c==0){
        ci=1;
    }else{
        ci=-1;
    }
    if(map[y][x]==0){
        var t=[];
        t[0]=y;
        t[1]=x;
        tmap.push(t);
        return true;
    }else{
        if(map[y][x]*ci<0){
            var t=[];
            t[0]=y;
            t[1]=x;
            tmap.push(t);
        }
        return false;
    }
};
function maMove(tmap,c,y,x){
    function fastMa(tmap,y,x,ys,xs,c){
        if(y+ys<map.length&&y+ys>=0&&x+xs<map.length&&x+xs>=0)
        if(map[y+ys][x+xs]==0){
            var yz= 0,xz=0;
            if(ys==0){
                yz=-1;
            }else{
                xz=-1;
            }
            if(y+ys+ys-yz<map.length&&y+ys+ys-yz>=0&&x+xs+xs-xz<map.length&&x+xs+xs-xz>=0)
            if(map[y+ys+ys-yz][x+xs+xs-xz]*c<=0){
                var t=[];
                t[0]=y+ys+ys-yz;
                t[1]=x+xs+xs-xz;
                tmap.push(t);
            }
            if(y+ys+ys+yz<map.length&&y+ys+ys+yz>=0&&x+xs+xs+xz<map.length&&x+xs+xs+xz>=0)
            if(map[y+ys+ys+yz][x+xs+xs+xz]*c<=0){
                var t1=[];
                t1[0]=y+ys+ys+yz;
                t1[1]=x+xs+xs+xz;
                tmap.push(t1);
            }
        }
    }
    var cc=0;
    if(c==0){
        cc=1;
    }else{
        cc=-1;
    }
    fastMa(tmap,y,x,-1,0,cc);
    fastMa(tmap,y,x,1,0,cc);
    fastMa(tmap,y,x,0,-1,cc);
    fastMa(tmap,y,x,0,1,cc);
};
function xiangMove(tmap,c,y,x){//c:0红 1黑
    function fastXiang(tmap,y,x,yy,xx,c,cy){
        if(((y+yy*2)<map.length)&&y+yy*2>=0&&((x+xx*2)<map.length)&&x+xx*2>=0){
            if(cy(y+yy*2))
            if(map[y+yy][x+xx]==0){
                if(map[y+yy*2][x+xx*2]*c<=0){
                    var t=[];
                    t[0]=y+yy*2;
                    t[1]=x+xx*2;
                    tmap.push(t);
                }
            }
        }
    }
    var cc=0;
    if(c==0){
        cc=1;
    }else{
        cc=-1;
    }
    var ch;
    if(c==0){
        ch=function(y){return y>4};
    }else{
        ch=function(y){return y<5};
    }
    fastXiang(tmap,y,x,1,1,cc,ch);
    fastXiang(tmap,y,x,1,-1,cc,ch);
    fastXiang(tmap,y,x,-1,1,cc,ch);
    fastXiang(tmap,y,x,-1,-1,cc,ch);
};
function shiMove(tmap,c,y,x){//c:0红 1黑
    function fastShi(tmap,y,x,yy,xx,c,cc){
        if(cc(y+yy)){
            if(x+xx>=3&&x+xx<=5){
                if(map[y+yy][x+xx]*c<=0){
                    var t=[];
                    t[0]=y+yy;
                    t[1]=x+xx;
                    tmap.push(t);
                }
            }
        }
    }
    var cf;
    var cc=0;
    if(c==0){
        cc=1;
        cf=function(y){return y>=7&&y<=9};
    }else{
        cf=function(y){return y>=0&&y<=2};
        cc=-1;
    }
    fastShi(tmap,y,x,1,1,cc,cf);
    fastShi(tmap,y,x,-1,1,cc,cf);
    fastShi(tmap,y,x,1,-1,cc,cf);
    fastShi(tmap,y,x,-1,-1,cc,cf);
};
function JSMove(tmap,c,y,x){
    function fastJS(tmap,y,x,yy,xx,c,cc){
        if(cc(y+yy)){
            if(x+xx>=3&&x+xx<=5){
                if(map[y+yy][x+xx]*c<=0){
                    var t=[];
                    t[0]=y+yy;
                    t[1]=x+xx;
                    tmap.push(t);
                }
            }
        }
    }
    var cf;
    var cc=0;
    if(c==0){
        cc=1;
        cf=function(y){return y>=7&&y<=9};
    }else{
        cf=function(y){return y>=0&&y<=2};
        cc=-1;
    }
    fastJS(tmap,y,x,1,0,cc,cf);
    fastJS(tmap,y,x,-1,0,cc,cf);
    fastJS(tmap,y,x,0,-1,cc,cf);
    fastJS(tmap,y,x,0,1,cc,cf);
    if(c==0){
        for(var q=y-1;q<map.length&&q>=0;q--){
            if(map[q][x]==0){
                continue;
            }
            if(map[q][x]==-7){
                var t=[];
                t[0]=q;
                t[1]=x;
                tmap.push(t);
            }else break;
        }
    }else{
        for(var q=y+1;q<map.length&&q>=0;q++){
            if(map[q][x]==0){
                continue;
            }
            if(map[q][x]==7){
                var t=[];
                t[0]=q;
                t[1]=x;
                tmap.push(t);
            }else break;
        }
    }
};
//被选中的时候
function onChose(j,i){
	Log("测试，这里切面二，用于控制玩家不能越权");
//    if(!runNow)return;
    if(onMove)return;
    //alert(j+""+i);
    var CC=WhatSpace(j,i);
    if(CC==0)
    {/* 选中空位置 */
        onChoseS(j,i);
    }else
    {
        Log("选择了"+j+"-"+i+"  "+CC);
        onChoseC(j,i,CC);
    }

};
//清除本次操作缓存
function cleanSt(){
    nowChoseC=[];
    cleanChose();
    moveList=[];
    eatList=[];
    OnChoseNow=false;
};
function trunH(){
    if(nowWho==0){
        nowWho=1;
    }else{
        nowWho=0;
    }
    cleanSt();
};
function showSt(j,i,t){
    nowChoseC=[];
    cleanChose();
    showChose(j,i,1);
    var tmap = WhereCan(j,i,t);
    if(tmap!=null && tmap.length>0)
        for(var q=0;q<tmap.length;q++){
            if(map[tmap[q][0]][tmap[q][1]]==0){
                moveList.push(tmap[q]);
            }else{
                eatList.push(tmap[q]);
            }
            showChose(tmap[q][0],tmap[q][1],tmap[q][2]+2);
        }
    nowChoseC[0]=j;
    nowChoseC[1]=i;
    nowChoseC[2]=t;
    OnChoseNow=true;
};



function onChoseC(j,i,t){
    if(!OnChoseNow){
        if(nowWho==0){
            if(t<0)return;
        }
        if(nowWho==1){
            if(t>0)return;
        }
    }
    if(nowChoseC[0]==j&&nowChoseC[1]==i){
        cleanSt();
        return;
    }
    if(OnChoseNow==true){
        for(var q=0;q<eatList.length;q++){
            if(eatList[q][0]==j&&eatList[q][1]==i){
                //eat && move
                eat(nowChoseC[0],nowChoseC[1],j,i);
                break;
            }
        }
        cleanSt();
    }
    if(nowWho==0){
        if(t<0){
            cleanSt();
            return;
        }
    }
    if(nowWho==1){
        if(t>0){
            cleanSt();
            return;
        }
    }
    showSt(j,i,t);
};
function onChoseS(j,i){
    if(OnChoseNow){
        for(var q=0;q<moveList.length;q++){
            if(moveList[q][0]==j&&moveList[q][1]==i){
                move(nowChoseC[0],nowChoseC[1],j,i);
                break;
            }
        }
    }
    cleanSt();
};
function LoadGround(){
    var g="";
    for(var j=0;j<10 ;j++){
        map[j]=[];
        for(var i=0;i<9 ;i++){
            map[j][i]=0;
            g+="<article class='CS' id='CS"+j+"-"+i+"' onclick='onChose("+j+","+i+")'></article>";
        }
    }
    $("#space").html(g);
    Log("完成创建场景LoadGround");
};

//0空
//兵1 炮2 车3 马4 相5 士6 帅7 红
//卒-1 炮-2 车-3 马-4 象-5 士-6 将-7 黑

function getCText(j,i){
    var T=[];
    switch (map[j][i])
     {
     case (0):
    	 console.log("case0");
        return null;
     break;
     case (1):
         T[0]="兵";
         T[1]="BR";
     break;
     case (2):
         T[0]="炮";
         T[1]="PR";
     break;
     case (3):
         T[0]="车";
         T[1]="JR";
     break;
     case (4):
         T[0]="马";
         T[1]="MR";
     break;
     case (5):
         T[0]="相";
         T[1]="XR";
     break;
     case (6):
         T[0]="士";
         T[1]="SR";
     break;
     case (7):
         T[0]="帅";
         T[1]="SR";
     break;
     case (-1):
         T[0]="卒";
         T[1]="BB";
     break;
     case (-2):
         T[0]="炮";
         T[1]="PB";
     break;
     case (-3):
         T[0]="车";
         T[1]="JB";
     break;
     case (-4):
         T[0]="马";
         T[1]="MB";
     break;
     case (-5):
         T[0]="象";
         T[1]="XB";
     break;
     case (-6):
         T[0]="士";
         T[1]="SB";
     break;
     case (-7):
         T[0]="将";
         T[1]="JB";
     break;
     default :
    	 console.log("case0Default");
         return null;
     break;
     }
    return T;
};

function showC()
{
    for(var j=0;j<10 ;j++) {
        for (var i = 0; i < 9; i++) {
            var cla="";
            var tex="";
            var isNone=false;
            var T=getCText(j,i);
            if(T == null){
                isNone=true;
            }else{
                cla=T[1];
                tex=T[0];
            }
            if(isNone){
                continue;
            }
            $("#CS"+j+"-"+i).html(
                    "<section class='C "+cla.substring(1)+"'>"+tex+"</section>"
            )
        }
    }
    Log("完成显示场景showC");
};

//0清除 1绿色 2黄色 3红色
function showChose(j,i,t){
    var o=$("#CS"+j+"-"+i);
    if(t==0){
        o.css({
            "box-shadow": "",
            "border": ""
        });
        return;
    }
    var c="";
    switch (t){
        case 1:
            c="6bc274";
            break;
        case 2:
            c="eeb948";
            break;
        case 3:
            c="c53f46";
            break;
        default :
            break;
    }
   o.css({
        "box-shadow": "0 0 25pt #"+c,
        "border": "3px solid #"+c
    })
};

function cleanChose(){
    $(".CS").css({
        "box-shadow": "",
        "border": ""
    })
};
function move(y,x,j,i,eat){
	Log("进入move方法！");
    onMove=true;
    if(eat==null)
        if(map[j][i]!=0){
            LogError("错误的位置");
            return;
        }
    var cla="";
    var tex="";
    var T=getCText(y,x);
    var temp = "";
    var calTemp ="" ;
    if(T == null){
        LogError("丢失棋子信息");
        return;
    }else{
        cla=T[1];
        tex=T[0];
    }
    if(eat==null){
	     Log(y+"-"+x+" "+tex+" 移动到"+j+"-"+i);
	}else{
        Log(y+"-"+x+" "+tex+" 吃"+j+"-"+i+" "+getCText(j,i)[0]);
        temp = getCText(j,i)[0];
        calTemp = getCText(j,i)[1];
	}
	var old = map[y][x];
	var _new = map[j][i];
    //map[j][i]=map[y][x];
    map[j][i] = old;
    map[y][x]=0;
    $("#CS"+j+"-"+i).html(
            "<section class='C "+cla.substring(1)+"' style='transform:translate("+(x-i)*45+"px,"+(y-j)*45+"px);'>"+tex+"</section>"
    )
    $("#CS"+y+"-"+x).html(
        ""
    )

    setTimeout(function(){
        $("#CS"+j+"-"+i+" section").css({
            transform:""
        })
    },10);
    setTimeout(function(){
        trunH();
        onMove=false;
    },700);
};

function eat(y,x,j,i){
    onMove=true;
    $("#CS"+j+"-"+i+" section").css({
        transform:"scale(0,0)"
    })
    setTimeout(function(){
        move(y,x,j,i,true);
    },700)
};
function next(){
	var json = JSON.pares(moveArray[currentCount]);
	move(json.beforeY,json.beforeX,json.afterY ,json.afterX ,eat);
	currentCount=currentCount+1;
}
var moveArray = [];
var currentCount = 0;
function readyGame(){
		var jsonParam ={};
		jsonParam.gameId = gameId;
		var json = sendPostSyn(JSON.stringify(jsonParam),"qryGameById");
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
		}
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
    <title>象1棋</title>
  
</head>
<body>

<section id="ground">
    <section id="board">
        <section id="line">
            <section id="rows">
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row"></article>
                <article class="row end"></article>
            </section>
            <section id="lines">
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line"></article>
                <article class="line end"></article>
            </section>
            <section id="river">
                <article>۞۞۞۞۞۞۞۞</article>
            </section>
            <section id="flower">
                <section id="F">
                    <section class="L2">
                        <article></article>
                        <article class="t2"></article>
                    </section>
                    <section class="L5">
                        <article></article>
                        <article></article>
                        <article></article>
                        <article></article>
                        <article></article>
                    </section>
                </section>
                <section id="L">
                    <section class="L5">
                        <article></article>
                        <article></article>
                        <article></article>
                        <article></article>
                        <article></article>
                    </section>
                    <section class="L2">
                        <article></article>
                        <article></article>
                    </section>
                </section>
            </section>
            <section id="cross">
                <section id="T">
                    <article></article>
                    <article></article>
                </section>
                <section id="B">
                    <article></article>
                    <article></article>
                </section>
            </section>
        </section>
        <section id="space"></section>
    </section>
</section>

</body>


</html>