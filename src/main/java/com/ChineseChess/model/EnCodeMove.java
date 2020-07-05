package com.ChineseChess.model;

import java.io.Serializable;

public class EnCodeMove implements Serializable{

	private String prefix;//前缀
	private String action;//动作
	private String displacement;//y位移
	private String beforeX;//原始位置
	private String chessName;//棋子名字
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getBeforeX() {
		return beforeX;
	}
	public void setBeforeX(String beforeX) {
		this.beforeX = beforeX;
	}
	public String getChessName() {
		return chessName;
	}
	public void setChessName(String chessName) {
		this.chessName = chessName;
	}
	@Override
	public String toString(){
		return prefix+chessName+beforeX+action+displacement;
	}
}
