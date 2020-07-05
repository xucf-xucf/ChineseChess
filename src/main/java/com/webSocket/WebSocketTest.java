package com.webSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


import static com.webSocket.socketUtil.*;

import static com.common.ThreadLocalHelp.*;

import com.ChineseChess.control.gamebase.GameBase;
import com.ChineseChess.model.OnMove;
import com.common.SimulateCache;

import static com.common.SimulateCache.*;
import com.dbSource.base.NameSpace;
//import com.fasterxml.jackson.databind.util.JSONPObject;
import com.dbSource.base.service.BaseService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocketTest extends BaseService{
	
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	int i=0;
	int count=0;
	int[][] testMap;
	List<OnMove> list = new ArrayList<OnMove>();
 	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	//	private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();
	private static Map<String,WebSocketTest> webSocketSet = new HashMap<String,WebSocketTest>();

	/**
	 * 使用一个map集合暂替缓存机制
	 */
//	public static Map<String,Object>simulateCacheMap = new HashMap<String,Object>();
	private Session session;
	
	private String rival;
	private String color;
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setRival(String rival){
		this.rival = rival;
	}
	public String getRival(){
		return this.rival;
	}
	
	/**
	 * 连接建立成功调用的方法
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketSet.put(session.getId(),this); //加入set中
	}
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		//		webSocketSet.remove(this); //从set中删除

		webSocketSet.remove(session.getId());
		subOnlineCount(); //在线数减
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("来自客户端的消息:" + message);
		Map<String,String> msgMap = getMessage(message);
		String msgType = msgMap.get("type");//类型
		Map<String,String>resMap = new HashMap<String,String>();
		String local = msgMap.get("local");
//		if(webSocketSet.get(local)!=null){
//			String rival = webSocketSet.get(local).getRival();
//		}
		if(msgType.equals(MSG_TYPE_CONNECT)){//第一次 连接
			webSocketSet.put(local, this);
			if(!local.equals(session.getId())){
				webSocketSet.remove(session.getId());
			}
			
//			resMap.put("type", MSG_TYPE_CHESSMOVE);
//			CCUtil cUtil=new CCUtil();
//			OnMove om = new OnMove();
//			om = cUtil.deCodeCC("炮二平五", "R", CCUtil.CHESS_INITIAL_JSONARRAY);
//			resMap.put("beforeY", om.getBeforeY());
//			resMap.put("beforeX", om.getBeforeX());
//			resMap.put("afterX", om.getAfterX());
//			resMap.put("afterY", om.getAfterY());
//			String res = resToJson(resMap);
//			this.sendMessage(res);
//			om = cUtil.deCodeCC("炮8进4", "B", om.getMap());
//			resMap.put("beforeY", om.getBeforeY());
//			resMap.put("beforeX", om.getBeforeX());
//			resMap.put("afterX", om.getAfterX());
//			resMap.put("afterY", om.getAfterY());
//			this.sendMessage(resToJson(resMap));
//			om = cUtil.deCodeCC("炮五进4", "R", om.getMap());
//			resMap.put("beforeY", om.getBeforeY());
//			resMap.put("beforeX", om.getBeforeX());
//			resMap.put("afterX", om.getAfterX());
//			resMap.put("afterY", om.getAfterY());
//			this.sendMessage(resToJson(resMap));
//			om = cUtil.deCodeCC("象7进5", "B", om.getMap());
//			resMap.put("beforeY", om.getBeforeY());
//			resMap.put("beforeX", om.getBeforeX());
//			resMap.put("afterX", om.getAfterX());
//			resMap.put("afterY", om.getAfterY());
//			this.sendMessage(resToJson(resMap));
			
		}else if(msgType.equals(MSG_TYPE_CHESSMOVE)){//走棋消息
			//发送编码信息
			JSONObject json = JSONObject.fromObject(message);
			OnMove om = (OnMove)JSONObject.toBean(json,OnMove.class);
			CCUtil cUtil=new CCUtil();
			String enCodeName = cUtil.enCodeCC(om);
			resMap.put("msgText", enCodeName);
			resMap.put("color", om.getColor());
			resMap.put("type", MSG_TYPE_CHESSMOVE);
			String res = resToJson(resMap);
			CCDecodeWebSocket.getWebSocket(local).sendMessage(res);;
			CCDecodeWebSocket.getWebSocket(rival).sendMessage(res);
			//编码信息写到模拟缓存中
			String key = judgeMapMixtureKey(local,rival);
			if(key!=null){
				((ArrayList<String>)SimulateCache.get(key)).add(enCodeName);
				((ArrayList<OnMove>)SimulateCache.get(key+"_om")).add(om);
			}else{
				System.out.println("棋谱缓存失败！！！");
			}
			
			//给对家发送走棋信息
			resMap.put("beforeY", om.getBeforeY());
			resMap.put("beforeX", om.getBeforeX());
			resMap.put("afterX", om.getAfterX());
			resMap.put("afterY", om.getAfterY());
			res = resToJson(resMap);
			webSocketSet.get(rival).sendMessage(res);
//			//list.add(om);
//			JSONArray jsonMap = JSONArray.fromObject(om.getMap());
//			System.out.println(jsonMap);
//			//转为int[][]之后，在数组里面是x和y是互换的，所以后面用的全是翻转的
//			int[][] array = JSONArrayToIntArray(jsonMap);
			String target = om.getTarget();
			if(target!=null){//吃子
				if("7-7".contains(target)){//某方的王被吃了//来自发消息方的消息
					//String winName = CommonWebSocket.getWebsocket(local).getWebName();
					String winColor = msgMap.get("color");
					String msg = "对局结束，"+(winColor.equals("R")?"红方":"黑方")+"方获胜。";
					resMap.put("msgText", msg);
					resMap.put("sendName", "系统");
					resMap.put("type", MSG_TYPE_WIN);
					res = resToJson(resMap);
					webSocketSet.get(rival).sendMessage(res);
					webSocketSet.get(local).sendMessage(res);
					resMap.put("type", MSG_TYPE_TEXT);
					res = resToJson(resMap);
					PrivateWebSocket.getWebsocket(local).sendMessage(res);
					PrivateWebSocket.getWebsocket(rival).sendMessage(res);
				}
			}

		}else if(MSG_TYPE_WIN.equals(msgType)){//对局胜利
		}else if(msgType.equals(MSG_TYPE_SETCHESS_MOVE)){//打谱的走棋
			//发送编码信息
			JSONObject json = JSONObject.fromObject(message);
			OnMove om = (OnMove)JSONObject.toBean(json,OnMove.class);
			CCUtil cUtil=new CCUtil();
			String enCodeName = cUtil.enCodeCC(om);
			resMap.put("msgText", enCodeName);
			resMap.put("type", MSG_TYPE_SETCHESS_MOVE);
			String res = resToJson(resMap);
			CCDecodeWebSocket.getWebSocket(local).sendMessage(res);
		}else if(msgType.equals(MSG_TYPE_SURE_REMOVE)){//同意悔棋，被请求者
			resMap.put("type", MSG_TYPE_SURE_REMOVE);
			String color = msgMap.get("chessColor");
			String key = judgeMapMixtureKey(local,rival);
			OnMove om = new OnMove();
			if(key!=null){
				SimulateCache.removeListLastIndex(key);
				om = SimulateCache.getListLastIndex(key+"_om");
				SimulateCache.removeListLastIndex(key+"_om");
			}else{
				System.out.println("棋谱缓存失败！！！");
			}
			resMap.put("beforeY", om.getBeforeY());
			resMap.put("beforeX", om.getBeforeX());
			resMap.put("afterX", om.getAfterX());
			resMap.put("afterY", om.getAfterY());
			resMap.put("color", om.getColor());
			resMap.put("map", ((OnMove)SimulateCache.getListLastIndex(key+"_om")).getMap().toString());
			if(webSocketSet.get(rival)!=null){//悔棋动作数据发送
				String res = resToJson(resMap);
				webSocketSet.get(rival).sendMessage(res);
				this.sendMessage(res);
			}
			//棋谱编码发送
			ArrayList<String> gameList = SimulateCache.get(key);
			StringBuilder stringBuilder = new StringBuilder();
			for(String str : gameList){
				stringBuilder.append(str+"<br>");
			}
			resMap.put("msgText", stringBuilder.toString());
			resMap.put("type", MSG_TYPE_RECHESSMOVE);
			String res = resToJson(resMap);
			CCDecodeWebSocket.getWebSocket(local).sendMessage(res);
			CCDecodeWebSocket.getWebSocket(rival).sendMessage(res);
			int reCount = om.getColor().equals(color)?2:1;
			if(reCount ==2){
				SimulateCache.removeListLastIndex(key);
				om = SimulateCache.getListLastIndex(key+"_om");
				SimulateCache.removeListLastIndex(key+"_om");
				resMap.put("type", MSG_TYPE_SURE_REMOVE);
				resMap.put("beforeY", om.getBeforeY());
				resMap.put("beforeX", om.getBeforeX());
				resMap.put("afterX", om.getAfterX());
				resMap.put("afterY", om.getAfterY());
				resMap.put("color", om.getColor());
				resMap.put("map", ((OnMove)SimulateCache.getListLastIndex(key+"_om")).getMap().toString());
				if(webSocketSet.get(rival)!=null){//悔棋动作数据发送
					res = resToJson(resMap);
					webSocketSet.get(rival).sendMessage(res);
					this.sendMessage(res);
				}
				//棋谱编码发送
				gameList = SimulateCache.get(key);
				stringBuilder = new StringBuilder();
				for(String str : gameList){
					stringBuilder.append(str+"<br>");
				}
				resMap.put("msgText", stringBuilder.toString());
				resMap.put("type", MSG_TYPE_RECHESSMOVE);
				res = resToJson(resMap);
				CCDecodeWebSocket.getWebSocket(local).sendMessage(res);
				CCDecodeWebSocket.getWebSocket(rival).sendMessage(res);
			}
		}else if(msgType.equals(MSG_TYPE_DIS_REMOVE)){//拒绝悔棋
			resMap.put("type", MSG_TYPE_DIS_REMOVE);
			if(webSocketSet.get(rival)!=null){
				webSocketSet.get(rival).sendMessage(resToJson(resMap));
			}
		}else if(msgType.equals(MSG_TYPE_SURE_PEACE)){//同意和棋
			resMap.put("type", MSG_TYPE_SURE_PEACE);
			if(webSocketSet.get(rival)!=null){
				webSocketSet.get(rival).sendMessage(resToJson(resMap));
			}
		}else if(msgType.equals(MSG_TYPE_DIS_PEACE)){//拒绝和棋
			resMap.put("type", MSG_TYPE_DIS_PEACE);
			if(webSocketSet.get(rival)!=null){
				webSocketSet.get(rival).sendMessage(resToJson(resMap));
			}
		}
		
		
		
		
		
		
		
		
		
		/*JSONObject json = JSONObject.fromObject(message);
		OnMove om = (OnMove)JSONObject.toBean(json,OnMove.class);
		list.add(om);
		JSONArray jsonMap = JSONArray.fromObject(om.getMap());
		//转为int[][]之后，在数组里面是x和y是互换的，所以后面用的全是翻转的
		int[][] array = JSONArrayToIntArray(jsonMap);
		CCUtil cUtil=new CCUtil();
		System.out.println(cUtil.enCodeCC(om));
		*/
//		String array =  jsonMap.get(0).toString();
//        String l[] = array.split(",");
//        for(String k:l){
//        	System.out.println(k);
//        }
//		System.out.println(array);
//		System.out.println((jsonMap.get(0))[0]);
		count++;
		i++;
		
//		if(i==3){
//			System.out.println("======222222222========");
//			OnMove om1 = list.get(list.size()-2);
//			jsonMap = JSONArray.fromObject(om1.getMap());
//			System.out.println(jsonMap);
//			System.out.println(jsonMap.toString());
//			OnMove test = new OnMove();
//			om.setMap(jsonMap);
//			om.setResponseReMove("1");
//			sendMsg((String)session.getId(),JSONObject.fromObject(om).toString());
//			i = i-3;
//		}
//		System.out.println("==============");
//		System.out.println(json);
////		String moveDo = CCUtil.enCodeCC(om);
//		System.out.println(om);
//		if(message.contains("move")){//客户端的移动操作//红黑都移动后保存记录
//			String moveDo = CCUtil.enCodeCC(message);//转化为中国象棋的叫法
//			String msg[] = message.split("Local");
//			String localId = msg[1];
//			String goMsg = msg[0];
//			String Msg[] = goMsg.split(",");
//			String y = Msg[2];//原y
//			String x = Msg[3];
//			String j = Msg[4];//移动后的y
//			String i = Msg[5];
//			sendMsg(rivalSessionId.get(),message);
//		}else if(message.contains("rivalSessionId")){//收到客户端的设置对家消息
//			setRival(message.split("=")[1]);
//		}else if(message.contains("reMove")){//收到客户端的悔棋消息
//			if(message.contains("agreereMove")){//对家同意悔棋消息
//				Map<String,String> reMoveMsg = getGoingList(moveCount.get()-1);
//				sendMsg(rivalSessionId.get(),reMoveMsg.get(""));
//			}else if(message.contains("disAgreereMove")){//不同意 悔棋
//				
//			}else{//发送请求同意悔棋
//				sendMsg(rivalSessionId.get(),"requestreMove");
//			}
//			
//		}
//				for(WebSocketTest item: webSocketSet.values()){
//					try {
//						item.sendMessage(message);
//					} catch (IOException e) {
//						e.printStackTrace();
//						continue;
//					}
//				}
//				if(webSocketSet.get(rivalSessionId)!=null){
//					try {
//						webSocketSet.get(rivalSessionId).sendMessage("3,6,4,6,move");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
	}
	
	
	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		System.out.println("发生错误");
		error.printStackTrace();
	}
	
	/**
	 * 向指定客户端发送消息
	 * @param session
	 * @param msg
	 */
	private void sendMsg(String session ,String msg){
		try {
			webSocketSet.get(session).sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static WebSocketTest getWebSocket(String key){
		return webSocketSet.get(key);
	}
	
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
		//this.session.getAsyncRemote().sendText(message);
	}
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}
	public static synchronized void addOnlineCount() {
		WebSocketTest.onlineCount++;
	}
	public static synchronized void subOnlineCount() {
		WebSocketTest.onlineCount--;
	}
	public void saveToList(OnMove om){
//		goingMap.put("rival", value);
		addGoingList(om);
	}

	/**
	 * 将棋局转换为二维int数组
	 * @param jsonArray
	 * @return int[][]
	 */
	private int[][] JSONArrayToIntArray(JSONArray jsonArray){
		int[][] array = new int[10][9];
		for(int i=0;i<jsonArray.size();i++){
			String arrayString = jsonArray.getString(i).toString();
			String[] stringArray = arrayString.split(",");
			int len = stringArray.length;
			for(int j=0;j<len;j++){
				String str = stringArray[j];
				str = str.replace("[", "").replace("]", "");
				array[i][j] = Integer.parseInt(str);
			}
		}
		return array;
	}
	
	/**
	 * int二维数组转化JSONArray
	 * @param array
	 * @return
	 */
	private JSONArray IntArrayToJSONArray(int[][] array){
		JSONArray jsonMap2 = new JSONArray();
		jsonMap2.add(array);
		return (JSONArray)jsonMap2.get(0);
	}
	
	/**
	 * 我需要混合对战双方id混合起来的key可以互相顺逆识别
	 * @param key1
	 * @param key2
	 * @return real key
	 */
	public static String judgeMapMixtureKey(String key1,String key2){
		if(SimulateCache.containsKey(key1+"_"+key2)){
			return key1+"_"+key2;
		}else if(SimulateCache.containsKey(key2+"_"+key1)){
			return key2+"_"+key1;
		}else{
			return null;
		}
	}
	public static void main(String[] args) {
		String a = "hello ";
		link(a);
		System.out.println(a);
		a+="world";
		System.out.println(a);
		System.gc();
	}
	private static void link(String a){
		a+="world";
	}
}