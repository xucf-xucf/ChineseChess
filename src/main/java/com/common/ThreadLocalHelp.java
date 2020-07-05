package com.common;

import static com.common.ThreadLocalHelp.userT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ChineseChess.model.User;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ThreadLocalHelp {
	static ObjectMapper objectMapper = new ObjectMapper();
	 private static final ThreadLocal<String> _param = new ThreadLocal<String>();
	 public static final ThreadLocal<String>uuids=new InheritableThreadLocal<String>(){};
	 public static final ThreadLocal<Long>startTime=new InheritableThreadLocal<Long>();
	 public static final ThreadLocal<String> ipAddr=new ThreadLocal<String>();
	 public static final ThreadLocal<String> macAddr=new ThreadLocal<String>();
	 public static final ThreadLocal<User> userT=new ThreadLocal<User>();
	 
	 public static final ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();
	 private static void set_param(String param) {
		 _param.set(param);
	    }  
	      
	    private static String get_param() {  
	        return _param.get();
	    }
	    
	    public static void clear_param() {
	    	_param.remove();
	    }
	    /**
		 * 获取入参
		 * @return json String
		 * @throws Exception
		 */
		public static String getParamStr() {
			return get_param();
		}
		/**
		 * 获取入参,为空则返回空集合
		 * @return Map<K,V>
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public static <K,V>Map<K,V> getParamMap() throws Exception {
			if(StringUtils.isBlank(getParamStr())){
				return new HashMap<K,V>();
			}
			objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;  
			Map<K,V> paramMap = objectMapper.readValue(getParamStr(), HashMap.class);
			return paramMap;
		}
		public static boolean setParam(String param) {
			set_param(param);
			return true;
		}
		
	
		
		
}
