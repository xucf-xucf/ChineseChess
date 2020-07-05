package com.ChineseChess.model;

import java.io.Serializable;
import net.sf.json.JSONArray;

/**
 * 每一步的信息
 * @author 天理
 */
public class OnMove implements Serializable{
	private String color;//持字颜色
	private String chess;//棋子名字
	private String beforeY;//原来的y
	private String beforeX;//原来的x
	private String afterY;//之后的y
	private String afterX;//之后的x
	private String localSessionId;//本家id
	private String rivalSessionId;//对家id
	private String encodeMsg;//编码为中文的称呼
	private String requestReMove;//请求悔棋。1为请求
	private String responseReMove;//请求悔棋的回应。1为同意，0为不同意
	private String eat;//是否发生吃子事件
	private String target;//移动位置的棋子信息
	private JSONArray map;//局面
	private JSONArray beginMap;//局面
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getRivalSessionId() {
		return rivalSessionId;
	}
	public void setRivalSessionId(String rivalSessionId) {
		this.rivalSessionId = rivalSessionId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getChess() {
		return chess;
	}
	public void setChess(String chess) {
		this.chess = chess;
	}
	public String getBeforeY() {
		return beforeY;
	}
	public void setBeforeY(String beforeY) {
		this.beforeY = beforeY;
	}
	public String getBeforeX() {
		return beforeX;
	}
	public void setBeforeX(String beforeX) {
		this.beforeX = beforeX;
	}
	public String getAfterY() {
		return afterY;
	}
	public void setAfterY(String afterY) {
		this.afterY = afterY;
	}
	public String getAfterX() {
		return afterX;
	}
	public void setAfterX(String afterX) {
		this.afterX = afterX;
	}
	public String getLocalSessionId() {
		return localSessionId;
	}
	public void setLocalSessionId(String localSessionId) {
		this.localSessionId = localSessionId;
	}
	public String getEncodeMsg() {
		return encodeMsg;
	}
	public void setEncodeMsg(String encodeMsg) {
		this.encodeMsg = encodeMsg;
	}
	public String getRequestReMove() {
		return requestReMove;
	}
	public void setRequestReMove(String requestReMove) {
		this.requestReMove = requestReMove;
	}
	public String getResponseReMove() {
		return responseReMove;
	}
	public void setResponseReMove(String responseReMove) {
		this.responseReMove = responseReMove;
	}
	
	public String getEat() {
		return eat;
	}
	public void setEat(String eat) {
		this.eat = eat;
	}
	public JSONArray getMap() {
		return map;
	}
	public void setMap(JSONArray jsonMap) {
		this.map = jsonMap;
	}
	public JSONArray getBeginMap() {
		return beginMap;
	}
	public void setBeginMap(JSONArray jsonMap) {
		this.beginMap = jsonMap;
	}
	@Override
	public String toString() {
		return "OnMove [color=" + color + ", chess=" + chess + ", beforeY=" + beforeY + ", beforeX=" + beforeX
				+ ", afterY=" + afterY + ", afterX=" + afterX + ", localSessionId=" + localSessionId
				+ ", rivalSessionId=" + rivalSessionId + ", encodeMsg=" + encodeMsg + ", requestReMove=" + requestReMove
				+ ", responseReMove=" + responseReMove + ", eat=" + eat + ", map=" + map + "]";
	}

	
	
	
	
}
