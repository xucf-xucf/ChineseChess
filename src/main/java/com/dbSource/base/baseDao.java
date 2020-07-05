package com.dbSource.base;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;

public interface baseDao {
	
	public <K,V>K selectOne(NameSpace Mapper,String sqlID,Map<String ,V> param);
	
	public <K,V>K selectOne(NameSpace Mapper,String sqlID,List<V> param);

	public <K>K selectOne(NameSpace Mapper,String sqlID,String param);
	
	public <K>K selectOne(NameSpace Mapper,String sqlID);

	public <K,V>List<K> selectList(NameSpace Mapper,String sqlID,Map<String ,V> param);
	
	public <K,V>List<K> selectList(NameSpace Mapper,String sqlID,List<V> param);

	public <K,V>List<K> selectList(NameSpace Mapper,String sqlID,String param);
	
	public <K>List<K> selectList(NameSpace Mapper,String sqlID);
	
	public <V>int delete(NameSpace Mapper,String sqlID,Map<String ,V> param);
	
	public <V>int delete(NameSpace Mapper,String sqlID,List<V> param); 
	
	public int delete(NameSpace Mapper,String sqlID,String param); 
	
	public <V>int insert(NameSpace Mapper,String sqlID,Map<String ,V> param);
	
	public <V>int insert(NameSpace Mapper,String sqlID,List<V> param);

	public int insert(NameSpace Mapper,String sqlID,String param);
	
	public <V>int update(NameSpace Mapper,String sqlID,Map<String ,V> param);
	
	public <V>int update(NameSpace Mapper,String sqlID,List<V> param);

	public int update(NameSpace Mapper,String sqlID,String param);
	
	Configuration getConfiguration();
	
	Connection getConnection();
	
	List<BatchResult> flushStatements();
	
	<T> T getMapper(Class<T> type);
	
}
