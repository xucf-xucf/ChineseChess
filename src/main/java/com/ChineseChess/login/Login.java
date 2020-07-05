package com.ChineseChess.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ChineseChess.model.User;
import com.dbSource.base.service.BaseService;

import static com.common.ThreadLocalHelp.*;



@SessionAttributes({"user"})  
@Controller
public class Login extends BaseService{
	
	
	
	private static final String DEFAULE_ROLE_ID = "3";//默认权限等级
	@RequestMapping(value = "user/login", method = RequestMethod.POST ,produces="text/plain;charset=UTF-8")
	public  @ResponseBody String login(Model model) throws Exception{
		String status = SUCCESS_CODE;
		String desc = "";
		Map<String,String> paramMap = getParamMap();
		String account = checkKey(paramMap,"userAccount");
		String pwd = checkKey(paramMap,"userPwd");
		if((account==null||account.equals("")
				)||pwd==null||pwd.equals("")
				){
			status = FAILED_CODE;
			desc="账号密码不能为空";
		}else{
			Map<String ,String >map = new HashMap<String,String>();
			map.put("userAccount", account);
			map.put("userPwd", pwd);
			Map<String,String> userMap = baseDao.selectOne(ns.BaseInfo,"checkUser",map);
			if(userMap!=null){
				User user = new User();
				user.setUserAccount(account);
				user.setUserName(userMap.get("user_name"));
				user.setUserId(userMap.get("user_id"));
				user.setRoleCode(userMap.get("role_id"));
				model.addAttribute("user", user);
			}else{
				status = FAILED_CODE;
				desc = "Login failed！!!";
			}
		}
		paramMap.put("status", status);
		paramMap.put("desc", desc);
		return resToJson(paramMap);
	}
	
	
	@RequestMapping(value = "index")
	public String goTomain() throws Exception{
		
		return "main";
	}
	
	@RequestMapping(value = "visitor")
	public String visitor(Model model) throws Exception{
		User user = new User();
//		user.setRoleCode("10086");
//		user.setUserAccount("10086");
//		user.setUserName("游客");
		model.addAttribute("user", user);
		return "main";
	}
	@RequestMapping(value = "loginOut")
	public String loginOut(Model model) throws Exception{
		User user = new User();
//		user.setRoleCode("10086");
//		user.setUserAccount("10086");
//		user.setUserName("游客");
		model.addAttribute("user", user);
		return "main";
	}
	@RequestMapping(value = "regedit")
	public @ResponseBody String regedit() throws Exception{
		String status = SUCCESS_CODE;
		String desc = "success";
		Map<String,String> paramMap = getParamMap();
		String account = paramMap.get("regeditAccount");
		paramMap.put("userId", generateID());
		paramMap.put("roleId", DEFAULE_ROLE_ID);
		int i = baseDao.selectOne(ns.BaseInfo,"countAccount", account);
		if(i>0){
			status = FAILED_CODE;
			desc = "账号已存在！";
		}else{
			baseDao.insert(ns.BaseInfo,"addAccount", paramMap);
		}
		Map<String,String>res = new HashMap<String,String>();
		res.put("status", status);
		res.put("desc", desc);
		return resToJson(res);
	}
	@RequestMapping(value = "/test")

	public String test(Model model,@RequestParam Map<String,Object>param,HttpSession session) throws Exception{

//		List<Object> list = new ArrayList<Object>();
//		list.add("test");
//		redisUtil.setHashKeyObject("xcf", "XCF_", "1286237240"); 
//		System.out.println(redisUtil.getAllHashFiedlsByKey("xcf"));
//		redisUtil.setHashKeyObject("xcf", "XCF_2", list); 
//		System.out.println(redisUtil.getAllHashFiedlsByKey("xcf"));
////		list.add(om);
//		redisUtil.setHashKeyObject("xcf", "XCF_3", list); 
//		System.out.println(redisUtil.getAllHashFiedlsByKey("xcf"));
//		redisUtil.setHashKeyObject("xcf", "onMove", om);
//		System.out.println(redisUtil.getAllHashFiedlsByKey("xcf"));
//		list.add("ssss");
//		redisUtil.setListMemberValue("list", list);
//		System.out.println(redisUtil.getList("list"));
//		
//		redisUtil.remove("list");
//		System.out.println("============");
//		System.out.println(redisUtil.exists("test"));
//		System.out.println(redisUtil.getHashFiedlsByKey("xcf"));
		return "hr";
	}
	public static void main(String[] args) {
		String p = "sss{ddd}dd1";
		System.out.println(p.substring(p.indexOf("{"),p.indexOf("}")));
	}
}
