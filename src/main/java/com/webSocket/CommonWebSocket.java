package com.webSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.ChineseChess.model.User;
import com.common.SimulateCache;
import com.dbSource.base.service.BaseService;
import com.fasterxml.jackson.core.JsonParser.Feature;

import static com.webSocket.socketUtil.*;
import static com.common.ThreadLocalHelp.*;

import com.ChineseChess.model.OnMove;
//import com.fasterxml.jackson.databind.util.JSONPObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
/**
 * 公聊频道
 */
@ServerEndpoint("/commonRoom")
public class CommonWebSocket extends BaseService{
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	int i=0;
	int count=0;
	int[][] testMap;
	List<OnMove> list = new ArrayList<OnMove>();
	private String webName ;
	private String webId ;
	public void setWebName(String webName){
		this.webName = webName;
	}
	public String getWebName(){
		return this.webName;
	}
	public void setWebId(String webId){
		this.webId = webId;
	}
	public String getWebId(){
		return this.webId;
	}
 	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	//	private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>();
	private static Map<String,CommonWebSocket> webSocketSet = new HashMap<String,CommonWebSocket>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	/**
	 * 连接建立成功调用的方法
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		//		webSocketSet.add(this); //加入set中
		webSocketSet.put(session.getId(), this);
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount()+"\tID为"+session.getId());
	}
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
		webSocketSet.remove(this.getWebId());
		subOnlineCount(); //在线数减
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 * @throws IOException 
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("来自客户端的消息:" + message);
		Map<String,String>msgMap = getMessage(message);
		String msgType = msgMap.get("type");
		Map<String,String>resMap = new HashMap<String,String>();
		String local = msgMap.get("local");//消息来源的id
		if(msgType.equals(MSG_TYPE_CONNECT)){//第一次 连接
			webSocketSet.remove(local);
			webSocketSet.put(local, this);
			webSocketSet.get(local).setWebName(msgMap.get("localName"));
			webSocketSet.get(local).setWebId(local);
			if(!local.equals(session.getId())){
				webSocketSet.remove(session.getId());
			}
		}else if (msgType.equals(MSG_TYPE_TEXT)){ //消息
			//向所有人转发
			resMap.put("msgText", msgMap.get("msgText"));
			resMap.put("type", MSG_TYPE_TEXT);
			resMap.put("sendName", msgMap.get("localName"));
			String res = resToJson(resMap);
			for(String key : webSocketSet.keySet()){
				webSocketSet.get(key).sendMessage(res);
			}
		}else if (msgType.equals(MSG_TYPE_CONSENTGAME)){ //同意对战
			resMap.put("type", MSG_TYPE_CONSENTGAME);
			resMap.put("requester", msgMap.get("requester"));
			resMap.put("requesterName", msgMap.get("requesterName"));
			String res = resToJson(resMap);
			webSocketSet.get(msgMap.get("requester")).sendMessage(res);//向发起者通知
			this.sendMessage(res);//给本家通知
			
			
		}else if (msgType.equals(MSG_TYPE_REFUSEGAME)){ //拒绝对战
			resMap.put("msgText", msgMap.get("msgText"));
			resMap.put("type", MSG_TYPE_TEXT);
			resMap.put("sendName", msgMap.get("localName"));
			String res = resToJson(resMap);
			for(String key : webSocketSet.keySet()){
				webSocketSet.get(key).sendMessage(res);
			}
		}else if (msgType.equals(MSG_TYPE_PRIVATECHAT)){ //建立私聊消息
			String requester =  msgMap.get("requester");
			String requesterName = webSocketSet.get(requester).getWebName();
			String responser =  msgMap.get("responser");
			String responserName =  webSocketSet.get(responser).getWebName();
			resMap.put("requester", requester);
			resMap.put("responser", responser);
			resMap.put("responserName", responserName);
			resMap.put("requesterName", requesterName);
			resMap.put("msgText", "准备就绪！");
			resMap.put("type", MSG_TYPE_SETRIVAL);
			String res = resToJson(resMap);
			//私聊连接的建立和公聊走i到这一步，是异步的，所以让公聊等一下私聊的建立
			if(PrivateWebSocket.getWebsocket(requester)!=null){
				PrivateWebSocket.getWebsocket(requester).sendMessage(res);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(PrivateWebSocket.getWebsocket(requester)!=null){
					PrivateWebSocket.getWebsocket(requester).sendMessage(res);
				}
			}
			if(PrivateWebSocket.getWebsocket(responser)!=null){
				PrivateWebSocket.getWebsocket(responser).sendMessage(res);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(PrivateWebSocket.getWebsocket(responser)!=null){
					PrivateWebSocket.getWebsocket(responser).sendMessage(res);
				}
			}
			
			//同样方式的，建立私聊了，也要建立棋谱房间连接
			if(CCDecodeWebSocket.getWebSocket(requester)!=null){
				CCDecodeWebSocket.getWebSocket(requester).sendMessage(res);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(CCDecodeWebSocket.getWebSocket(requester)!=null){
					CCDecodeWebSocket.getWebSocket(requester).sendMessage(res);
				}
			}
			if(CCDecodeWebSocket.getWebSocket(responser)!=null){
				CCDecodeWebSocket.getWebSocket(responser).sendMessage(res);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(CCDecodeWebSocket.getWebSocket(responser)!=null){
					CCDecodeWebSocket.getWebSocket(responser).sendMessage(res);
				}
			}
			
			//同样方式的，建立棋谱房间连接,也要建立对局设置\设置内部的对家
			if(WebSocketTest.getWebSocket(requester)!=null){
				WebSocketTest.getWebSocket(requester).setRival(responser);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(WebSocketTest.getWebSocket(requester)!=null){
					WebSocketTest.getWebSocket(requester).setRival(responser);
				}
			}
			if(WebSocketTest.getWebSocket(responser)!=null){
				WebSocketTest.getWebSocket(responser).setRival(requester);
			}else{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(WebSocketTest.getWebSocket(responser)!=null){
					WebSocketTest.getWebSocket(responser).setRival(requester);
				}
			}
			//创建仿造缓存的数据结构
			SimulateCache.put(requester+"_"+responser, new ArrayList<String>());
			SimulateCache.put(requester+"_"+responser+"_om", new ArrayList<OnMove>());

			//向双方推送持子颜色/发起者为红旗
			resMap.put("type", MSG_TYPE_SETRIVAL);
			resMap.put("chessColor", "R");
			resMap.put("rival", responser);
			resMap.put("rivalName", responserName);
			WebSocketTest.getWebSocket(requester).sendMessage(resToJson(resMap));
			WebSocketTest.getWebSocket(requester).setColor("R");
			resMap.put("chessColor", "B");
			resMap.put("rival", requester);
			resMap.put("rivalName", requesterName);
			WebSocketTest.getWebSocket(this.getWebId()).sendMessage(resToJson(resMap));
			WebSocketTest.getWebSocket(this.getWebId()).setColor("B");
		}else if(MSG_TYPE_CLOSE.equals(msgType)){//关闭连接前发送的关闭请求
			webSocketSet.remove(local);
		}

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
	private void sendMsg(String userId ,String msg){
		try {
			webSocketSet.get(userId).sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
	}
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}
	public static synchronized void addOnlineCount() {
		CommonWebSocket.onlineCount++;
	}
	public static synchronized void subOnlineCount() {
		CommonWebSocket.onlineCount--;
	}
	public void saveToList(OnMove om){
		addGoingList(om);
	}
	public static CommonWebSocket getWebsocket(String key) {
		return webSocketSet.get(key);
	}
	public static Map<String,CommonWebSocket> getSocketMap(){
		return webSocketSet;
	}
}