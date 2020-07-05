package com.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 缓存模拟
 * 暂替缓存机制
 * @author 天理
 *
 */
public class SimulateCache{

	private static Logger logger = Logger.getLogger("SimulateCache");
	/**
	 * 暂替缓存数据结构
	 */
	private static Map<String,Object>simulateCacheMap = new HashMap<String,Object>();

	public static void put(String key,Object obj){
		simulateCacheMap.put(key, obj);
	}
	@SuppressWarnings("unchecked")
	public static <V>V get(String key){
		return (V)simulateCacheMap.get(key);
	}
	
	public static boolean containsKey(String key){
		return simulateCacheMap.containsKey(key);
	}
	public static void remove(String key) {
		simulateCacheMap.remove(key);
	}
	/**
	 * 移除key下的list的最后一个value
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> int removeListLastIndex(String key){
		try{
			List<V> list = (List<V>) simulateCacheMap.get(key);
		if(list==null){
			return -1;
		}else{
			((List<V>) simulateCacheMap.get(key)).remove(list.size()-1);
			return 1;
		}
		}catch (Exception e){
			logger.info("无法获取List！！");
			return -1;
		}
	}
	/**
	 * 获取key下的list的最后一个value
	 * @param key
	 * @return
	 */
	public static <V> V getListLastIndex(String key){
		return getListLastIndexOnOffset(key,0);
	}
	/**
	 * 获取key下的list的最后一个-offset的value
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <V> V getListLastIndexOnOffset(String key,int offset){
		try{
			List<V> list = (List<V>) simulateCacheMap.get(key);
		if(list==null){
			return null;
		}else{
			return list.get(list.size()-1-offset);
		}
		}catch (Exception e){
			logger.info("无法获取List！！");
			return null;
		}
	}
}
