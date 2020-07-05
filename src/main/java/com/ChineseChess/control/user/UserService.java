package com.ChineseChess.control.user;

import java.util.List;
import java.util.Map;

public interface UserService {
	
	/**
	 * 添加数据
	 * @param map
	 * @return
	 */
	public int addUser(Map<String,String>map);
	
	/**
	 * 编辑数据
	 * @param map
	 * @return
	 */
	public int editUser(Map<String,String>map);
	
	/**
	 * 删除数据
	 * @param map
	 * @return
	 */
	public int delUser(Map<String,String>map);

	/**
	 * 查询多条数据
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> selectUserList(Map<String,Object>map);
	
	/**
	 * 查询单条数据
	 * @param map
	 * @return
	 */
	public Map<String,String> selectUserMap(Map<String,String>map);

	public String selectUserCount(Map<String, Object> paramMap);

	public void changeUser(Map<String, Object> paramMap);
	
}
