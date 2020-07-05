package com.common;



import static com.common.ThreadLocalHelp.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class LoggerHelp {
	/**获得当前方法名
	 * @return
	 */
	protected Logger getLogger(){
		return LoggerFactory.getLogger(this.getClass().getName());
	}
	public String getMethodName(){
		return Thread.currentThread().getStackTrace()[4].getMethodName();
	}
	
	/**
	 * 开始日志记录
	 * @param param
	 */
	public void loggerStart(String param ){
		getLogger().info("UUID:"+uuids.get()+", Request MethodName:"+getMethodName()+
				", Request parameter:"+param);
	}
	public void loggerStart(String param ,String MethodName){
		getLogger().info("UUID:"+uuids.get()+", Request MethodName:"+MethodName+
				", Request parameter:"+param);
	} 
	public void loggerStart(String param ,String MethodName,String ipAddress){
		getLogger().info("UUID:"+uuids.get()+", Client IP Address: "+ipAddress+",Request MethodName:"+MethodName+
				", Request parameter:"+param);
	}
	public void loggerEnd(){
		getLogger().info("UUID:"+uuids.get()+
				", Elapsed time:"+(System.currentTimeMillis()-startTime.get())+"ms"
				);
	}
	public void loggerEnd(String Msg){
		getLogger().info("UUID:"+uuids.get()+", End Method:"+Msg+
				", Elapsed time:"+(System.currentTimeMillis()-startTime.get())+"ms"
				);
	}
	
	public void loggerMsg(String Msg ){
		getLogger().info("UUID:"+uuids.get()+", Msg:"+Msg);
	}
}
