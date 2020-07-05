package com.webSocket;

import java.util.List;
import java.util.Map;

import com.ChineseChess.model.OnMove;

public class socketUtil {
	static ThreadLocal<String> rivalSessionId = new ThreadLocal<String>();//唯一对家id
	static ThreadLocal<String> localSessionId = new ThreadLocal<String>();//唯一本家id
	static ThreadLocal<Integer> moveCount = new ThreadLocal<Integer>();//单局的第几步
	static ThreadLocal<List<OnMove>> goingList = new ThreadLocal<List<OnMove>>();//行动记录
	
	/**
	 * 设置当前手数
	 * @param count
	 */
	public static void setCount(int count){
		moveCount.set(count);
	}
	/**
	 * 设置对家sessionId
	 * @param sessionId
	 */
	public static void setRival(String sessionId){
		rivalSessionId.set(sessionId);
	}
	/**
	 * 设置本家sessionId
	 * @param sessionId
	 */
	public static void setLocal(String sessionId){
		localSessionId.set(sessionId);
	}
	/**
	 * 对绑定的线程list写数据
	 * @param moveMap 每一步的记录
	 */
	public static void addGoingList(OnMove om){
		goingList.get().add(om);
	}
	/**
	 * 获取绑定线程list的对应的值
	 * @param count 第几步
	 * @return
	 */
	public static OnMove getGoingList(int count){
		return goingList.get().get(count);
	}
	/**
	 * 清除对家sessionId
	 */
	public static void clearRival() {
		rivalSessionId.remove();
	}
	

}
