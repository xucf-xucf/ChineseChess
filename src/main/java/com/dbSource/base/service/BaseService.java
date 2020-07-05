package com.dbSource.base.service;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;


import com.common.LoggerHelp;
import com.dbSource.base.NameSpace;
import com.dbSource.base.baseDao;
import com.dbSource.redis.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseService extends LoggerHelp{
	
	/**
	 * 成功信息
	 */
	public static final String SUCCESS_CODE = "0";
	/**
	 * 失败信息
	 */
	public static final String FAILED_CODE = "1";
	/**
	 * 消息
	 */
	public static final String MSG_TYPE_CLOSE = "0";
	/**
	 * 消息
	 */
	public static final String MSG_TYPE_TEXT = "1";
	/**
	 * 悔棋
	 */
	public static final String MSG_TYPE_REMOVE = "3";
	/**
	 * 同意悔棋
	 */
	public static final String MSG_TYPE_SURE_REMOVE = "33";
	/**
	 * 拒绝悔棋
	 */
	public static final String MSG_TYPE_DIS_REMOVE = "34";
	/**
	 * 同意和棋
	 */
	public static final String MSG_TYPE_SURE_PEACE = "44";
	/**
	 * 拒绝和棋
	 */
	public static final String MSG_TYPE_DIS_PEACE = "45";
	/**
	 * 认输
	 */
	public static final String MSG_TYPE_GIVEUP = "2";
	/**
	 * 自家发送给自己认输
	 */
	public static final String MSG_TYPE_ME_GIVEUP = "32";
	/**
	 * 求和
	 */
	public static final String MSG_TYPE_PEACE = "4";
	/**
	 * 走棋
	 */
	public static final String MSG_TYPE_CHESSMOVE = "5";
	/**
	 *重新编码
	 */
	public static final String MSG_TYPE_RECHESSMOVE = "6";
	/**
	 * 设置对家
	 */
	public static final String MSG_TYPE_SETRIVAL = "11";
	/**
	 * 请求对战
	 */
	public static final String MSG_TYPE_REQUESTGAME = "12";
	/**
	 * 同意对战
	 */
	public static final String MSG_TYPE_CONSENTGAME = "13";
	/**
	 * 拒绝对战
	 */
	public static final String MSG_TYPE_REFUSEGAME = "14";
	/**
	 * 第一次建立连接
	 */
	public static final String MSG_TYPE_CONNECT = "20";
	
	/**
	 * 建立私聊连接
	 */
	public static final String MSG_TYPE_PRIVATECHAT = "21";
	/**
	 * 赢棋消息
	 */
	public static final String MSG_TYPE_WIN = "99";
	/**
	 * 打谱走棋
	 */
	public static final String MSG_TYPE_SETCHESS_MOVE = "95";


	public static final String CHESSINFO_SPLIT_STR = "%%";
	
	public ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 数据库连接必备
	 */
	@Resource(name = "baseDao")
	protected baseDao baseDao;
	
	protected NameSpace ns;
//	/**
//	 * redis工具类
//	 */
//	@Resource
//	protected RedisUtil redisUtil;
//	
	/**
	 * 返回Map做json处理
	 * @param resMap
	 * @return
	 * @throws Exception
	 */
	public <V>String resToJson(Map<String,V>resMap){
		try {
			StringWriter swRes = new StringWriter();
			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;  
			objectMapper.writeValue(swRes, resMap);
			String strResult = swRes.toString();
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	/**
	 * 校验入参
	 * @param map
	 * @param param
	 */
	public void checkParam(Map<String ,String> map,String ...param){
		if(map.get(param)==null){
			try {
				throw new Exception(param+"不能为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 检查某Key,返回其value
	 * @param map
	 * @param param
	 * @return
	 */
	public String checkKey(Map<String ,String> map,String param){
		if(map.get(param)==null){
			try {
				throw new Exception(param+"不能为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map.get(param);
	}
	
	/**
	 * 生成唯一ID<p>毫秒级 X 1/10概率重复
	 * @param table
	 * @return
	 */
	public String generateID(){
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   
		String year = c.get(Calendar.YEAR)+"";  
		String month = c.get(Calendar.MONTH)+"";   
		String date = c.get(Calendar.DATE)+"";    
		String hour = c.get(Calendar.HOUR_OF_DAY)+"";   
		String minute = c.get(Calendar.MINUTE)+"";   
		String second = c.get(Calendar.SECOND)+"";
		String ms = c.get(Calendar.MILLISECOND)+"";
		String number = new Random().nextInt(10)+""; 
		return year+month+date+hour+minute+second+ms+number;
	}
	

	/**
	 * websocket入参json解析 为Map
	 * @param message
	 * @return msgMap
	 */
	public Map<String,String> getMessage(String message){
		objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;  
		Map<String, String> msgMap = null;
		try {
			msgMap = objectMapper.readValue(message, HashMap.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgMap;
	}
	
}
