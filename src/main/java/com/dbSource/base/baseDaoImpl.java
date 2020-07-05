package com.dbSource.base;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;


public class baseDaoImpl extends SqlSessionDaoSupport implements baseDao{

	public <K, V> K selectOne(NameSpace Mapper, String sqlID, Map<String, V> param) {
		return super.getSqlSession().<K>selectOne(getStatement(Mapper, sqlID),param);
	}

	public <K, V> K selectOne(NameSpace Mapper, String sqlID, List<V> param) {
		return super.getSqlSession().<K>selectOne(getStatement(Mapper, sqlID),param);
	}

	public <K> K selectOne(NameSpace Mapper, String sqlID, String param) {
		return super.getSqlSession().<K>selectOne(getStatement(Mapper, sqlID),param);
	}

	public <K> K selectOne(NameSpace Mapper, String sqlID) {
		return super.getSqlSession().<K>selectOne(getStatement(Mapper, sqlID));
	}

	public <K, V> List<K> selectList(NameSpace Mapper, String sqlID, Map<String, V> param) {
		return super.getSqlSession().selectList(getStatement(Mapper, sqlID),param);
	}

	public <K, V> List<K> selectList(NameSpace Mapper, String sqlID, List<V> param) {
		return super.getSqlSession().selectList(getStatement(Mapper, sqlID),param);
	}

	public <K, V> List<K> selectList(NameSpace Mapper, String sqlID, String param) {
		return super.getSqlSession().selectList(getStatement(Mapper, sqlID),param);
	}

	public <K> List<K> selectList(NameSpace Mapper, String sqlID) {
		return super.getSqlSession().selectList(getStatement(Mapper, sqlID));
	}

	public <V> int delete(NameSpace Mapper, String sqlID, Map<String, V> param) {
		return super.getSqlSession().delete(getStatement(Mapper, sqlID),param);
	}

	public <V> int delete(NameSpace Mapper, String sqlID, List<V> param) {
		return super.getSqlSession().delete(getStatement(Mapper, sqlID),param);
	}

	public int delete(NameSpace Mapper, String sqlID, String param) {
		return super.getSqlSession().delete(getStatement(Mapper, sqlID),param);
	}

	public <V> int insert(NameSpace Mapper, String sqlID, Map<String, V> param) {
		return super.getSqlSession().insert(getStatement(Mapper, sqlID),param);
	}

	public <V> int insert(NameSpace Mapper, String sqlID, List<V> param) {
		return super.getSqlSession().insert(getStatement(Mapper, sqlID),param);
	}

	public int insert(NameSpace Mapper, String sqlID, String param) {
		return super.getSqlSession().insert(getStatement(Mapper, sqlID),param);
	}

	public <V> int update(NameSpace Mapper, String sqlID, Map<String, V> param) {
		return super.getSqlSession().update(getStatement(Mapper, sqlID),param);
	}

	public <V> int update(NameSpace Mapper, String sqlID, List<V> param) {
		return super.getSqlSession().update(getStatement(Mapper, sqlID),param);
	}

	public int update(NameSpace Mapper, String sqlID, String param) {
		return super.getSqlSession().update(getStatement(Mapper, sqlID),param);
	}

	private String getStatement(NameSpace nameSpace, String sqlId) {
		return nameSpace.getValue()+"."+sqlId;
	}

	public Configuration getConfiguration() {
		return super.getSqlSession().getConfiguration();
	}

	public Connection getConnection() {
		return super.getSqlSession().getConnection();
	}

	public List<BatchResult> flushStatements() {
		return super.getSqlSession().flushStatements();
	}

	public <T> T getMapper(Class<T> type) {
		return super.getSqlSession().getMapper(type);
	}
	
	
}
