package com.ChineseChess.control.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbSource.base.service.BaseService;
import com.webSocket.CommonWebSocket;

import static com.common.ThreadLocalHelp.*;



@Controller 
public class UserControl extends BaseService{
	
	@Autowired(required=true)
	UserService userService;
	
	
	@RequestMapping(value = "qryUser",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String qryUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,Object> paramMap = getParamMap();
			String page = (String)(paramMap.get("page")==null?"1":paramMap.get("page"));
			int start = (Integer.parseInt(page)-1)*10;
			int pageSize = 10;
			paramMap.put("start", start);
			paramMap.put("pageSize", pageSize);
			String ids = "";
			for(String key :CommonWebSocket.getSocketMap().keySet()){
				ids += "'"+key+"',";
			}
			ids+="''";
			paramMap.put("ids",ids);
			res.put("userList",userService.selectUserList(paramMap));
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "qryUserById",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String qryClassifyById(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,String> paramMap = getParamMap();
			String qryMy = paramMap.get("qryMy");
			if(StringUtils.isNotBlank(qryMy)){
				paramMap.put("userId", userT.get().getUserId());
			}
			res.put("user",userService.selectUserMap(paramMap));
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "editUser",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String editUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,String> paramMap = getParamMap();
			userService.editUser(paramMap);
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "addUser",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String addUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,String> paramMap = getParamMap();
			paramMap.put("addUserId", generateID());
			userService.addUser(paramMap);
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "delUser",produces = "text/plain;charset=UTF-8")
	public @ResponseBody String delUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,String> paramMap = getParamMap();
			int resNum = userService.delUser(paramMap);
			if(resNum>0){
				status = FAILED_CODE;
				desc = "";
			}
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "countUser",/*produces="application/json",*/produces = "text/plain;charset=UTF-8")
	public @ResponseBody String countUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			String ids = "";
			for(String key :CommonWebSocket.getSocketMap().keySet()){
				ids += "'"+key+"',";
			}
			ids+="''";
			Map<String,Object> paramMap = getParamMap();
			paramMap.put("ids",ids);
			res.put("count",userService.selectUserCount(paramMap));
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "changeUser",/*produces="application/json",*/produces = "text/plain;charset=UTF-8")
	public @ResponseBody String changeUser(){
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String ,Object >res=new HashMap<String,Object>();
		try {
			Map<String,Object> paramMap = getParamMap();
			if(userT.get().getUserId().equals(paramMap.get("userId"))){
				userService.changeUser(paramMap);
			}else{
				status = FAILED_CODE;
				desc = "无权操作";
			}
		} catch (Exception e) {
			status = FAILED_CODE;
			desc = e.getMessage();
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
}
