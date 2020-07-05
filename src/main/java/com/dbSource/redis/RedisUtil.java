package com.dbSource.redis;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisUtil {
	private Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	private RedisTemplate redisTemplate;
	
	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	/**
	 * 在key的集合内放置field和value的对应关系，Hash集合
	 * @param key key
	 * @param field 枚举key里的key
	 * @param value
	 * @return
	 * 
	 */
	public   boolean setHashKeyObject(final String key,
			final String field, final Object value) {
		@SuppressWarnings("unchecked")
		boolean result = (Boolean) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection con)
							throws DataAccessException {
						StringRedisSerializer ser = new StringRedisSerializer();
						boolean res = false;
						con.hSet(ser.serialize(key), ser.serialize(field),
								serialize(value));
						res = true;
						return res;
					}
				});
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public   Map<String, Object> getHashFiedlsByKey(final String key) {
		Map<String, Object> result = (Map<String, Object>) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection con)
							throws DataAccessException {
						StringRedisSerializer ser = new StringRedisSerializer();
						Object res = deserializeBytesMap(con.hGetAll(ser
								.serialize(key)));
						return res;
					}
				});
		return result;
	}

	// 序列化
	public byte[] serialize(Object obj) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] byt = baos.toByteArray();

			oos.flush();
			oos.close();
			baos.close();
			return byt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 反序列化
	public   Object deserialize(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(bais));
			obj = ois.readObject();
			ois.close();
			bais.close();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * 反序列化 注意redis中存储的数据 尤其是object类型的 写入和读取使用的序列化方式必须 要保持一致 一般key
	 * 和field都是string的 直接getBytes或者使用StringRedisSerializer的serialize方法即可
	 * 
	 * @param object
	 * @return
	 * 
	 */

	public   Map<String, Object> deserializeBytesMap(
			Map<byte[], byte[]> object) {

		Map<String, Object> question = new HashMap<String, Object>();
		StringRedisSerializer ser = new StringRedisSerializer();

		try {
			Set<Entry<byte[], byte[]>> entries = object.entrySet();
			for (Entry<byte[], byte[]> entry : entries) {

				ByteArrayInputStream bais = new ByteArrayInputStream(
						entry.getValue());
				ObjectInputStream ois = new ObjectInputStream(
						new BufferedInputStream(bais));
				String key = ser.deserialize(entry.getKey());
				// System.out.println(key);
				// System.out.println(key+":"+ois.readObject());
				question.put(key, ois.readObject());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return question;
	}
	
	/**
     * 删除缓存
     *
     * @param key
     */
    public    void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public   boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    
    /**
	 * 写入list列表缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public   boolean setListMemberValue(final String listkey,Object listMemberValue) {
		boolean result = false;
		try {
			 ListOperations<String, Object> listOperation = redisTemplate.opsForList();
			 listOperation.rightPush(listkey, listMemberValue);
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 读出list列表的第一个值
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public   Object getListPopMemberValue(final String listkey) {
		Object result = null;
		try {
			 ListOperations<String, Object> listOperation = redisTemplate.opsForList();
			 result=listOperation.leftPop(listkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 读出list列表
	 * 
	 * @param 
	 * @param 
	 * @return
	 */
	public   List<Object> getList(final String listkey) {
		List<Object> result = null;
		try {
			 ListOperations<String, Object> listOperation = redisTemplate.opsForList();
			 Long listSize=listOperation.size(listkey);
			 result = listOperation.range(listkey, 0, listSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * redisLock 
	 * @param key
	 * @param values
	 * @param timeout   /s
	 * @return
	 */
	public  boolean tryLock(final String key,final Object value,final long timeout) {
		@SuppressWarnings("unchecked")
		boolean result = (Boolean) redisTemplate
				.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection con)
							throws DataAccessException {
						StringRedisSerializer ser = new StringRedisSerializer();
						boolean res = false;
						con.setNX(ser.serialize(key),serialize(value));
						con.expire(ser.serialize(key), timeout);
						res = true;
						return res;
					}
				});
		return result;
	}
	
	
}