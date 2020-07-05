package com.common.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.dbSource.base.NameSpace;
import com.dbSource.base.service.BaseService;
import com.dbSource.redis.RedisUtil;
@Controller
public class heartbeatTask extends BaseService{
//	@Resource
//	RedisUtil redisUtil;
	public void task(){
//		System.out.println("心跳。。。。");
//		System.out.println(baseDao.selectList(NameSpace.BaseInfo, "BaseInfo"));
//		redisUtil.setHashKeyObject("xcf", "XCF_", "1286237240"); 
//		System.out.println(redisUtil.getHashFiedlsByKey("xcf"));
		
	}
}
