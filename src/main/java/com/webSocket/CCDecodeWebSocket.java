package com.webSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.ChineseChess.model.User;
import com.dbSource.base.service.BaseService;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.webSocket.socketUtil.*;
import static com.common.ThreadLocalHelp.*;

import com.ChineseChess.model.OnMove;
//import com.fasterxml.jackson.databind.util.JSONPObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
/**
 * 编码后的棋谱推送
 */
@ServerEndpoint("/CCDecodeRoom")
public class CCDecodeWebSocket extends BaseService{
	
	private static int onlineCount = 0;
	int i=0;
	int count=0;
	private static Map<String,CCDecodeWebSocket> webSocketSet = new HashMap<String,CCDecodeWebSocket>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	/**
	 * 连接建立成功调用的方法
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketSet.put(session.getId(), this);
	}
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(){
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
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("来自客户端的消息:" + message);
		Map<String,String> msgMap = getMessage(message);
		//向指定人的人发送消息，此处需要包装消息体
		String msgType = msgMap.get("type");//类型
		String rival = msgMap.get("rival");//对家
		String local = msgMap.get("local");
		Map<String,String>resMap = new HashMap<String,String>();
		if(msgType.equals(MSG_TYPE_CONNECT)){//第一次 连接
			webSocketSet.put(local, this);
			if(!local.equals(session.getId())){
				webSocketSet.remove(session.getId());
			}
		}else if(msgType.equals(MSG_TYPE_TEXT)){
			String msgText = msgMap.get("msgText");//消息体
			resMap.put("msgText", msgText);
			resMap.put("sendName", msgMap.get("sendName"));
			resMap.put("type", MSG_TYPE_TEXT);
			if(webSocketSet.get(rival)!=null){
				webSocketSet.get(rival).sendMessage(resToJson(resMap));
			}
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

	/**
	 * 这里只提供get，外部的不允许put，保证线程安全
	 * @param webSocketKey
	 * @return
	 */
	public static CCDecodeWebSocket getWebSocket(String webSocketKey){
		return webSocketSet.get(webSocketKey);
	}
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
	}
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}
	public static synchronized void addOnlineCount() {
		CCDecodeWebSocket.onlineCount++;
	}
	public static synchronized void subOnlineCount() {
		CCDecodeWebSocket.onlineCount--;
	}
	public void saveToList(OnMove om){
		addGoingList(om);
	}
	
}