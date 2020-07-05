package com.dbSource.base;

/**
 * 枚举类
 * @author 天理
 *
 */
public enum NameSpace {

	BaseInfo("com.ChineseChess.mapper.BaseInfo"),
	T_USER_DICTMapper("com.library.mapper.T_USER_DICTMapper");
	
	private final String value;

	private NameSpace(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
