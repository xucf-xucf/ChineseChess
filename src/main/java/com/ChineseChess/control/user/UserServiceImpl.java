package com.ChineseChess.control.user;

import java.util.List;
import java.util.Map;

import com.dbSource.base.NameSpace;
import com.dbSource.base.service.BaseService;

public class UserServiceImpl extends BaseService implements UserService {

	
	
	@Override
	public int addUser(Map<String, String> paramMap) {
		return baseDao.insert(NameSpace.T_USER_DICTMapper, "addUser", paramMap);
	}

	@Override
	public int editUser(Map<String, String> paramMap) {
		return baseDao.update(NameSpace.T_USER_DICTMapper, "editUser", paramMap);
	}

	@Override
	public int delUser(Map<String, String> paramMap) {
		return	baseDao.delete(NameSpace.T_USER_DICTMapper, "delUser", paramMap);	
	}

	@Override
	public List<Map<String, String>> selectUserList(Map<String, Object> paramMap) {
		return baseDao.selectList(NameSpace.T_USER_DICTMapper, "findUserGame", paramMap);
	}

	@Override
	public Map<String, String> selectUserMap(Map<String, String> paramMap) {
		return baseDao.selectOne(NameSpace.T_USER_DICTMapper, "findOneUser", paramMap);
	}
	@Override
	public String selectUserCount(Map<String, Object> paramMap) {
		return baseDao.selectOne(NameSpace.T_USER_DICTMapper, "findPageWithCount", paramMap);
	}

	@Override
	public void changeUser(Map<String, Object> paramMap) {
		baseDao.update(NameSpace.T_USER_DICTMapper, "editUser", paramMap);
	}

}
