package com.common.interceptor;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ChineseChess.model.User;

import static com.common.ThreadLocalHelp.*;
public class userInterceptor implements HandlerInterceptor{
	
	//免检
	private List<String>unCheckUrl;
	public List<String> getUnCheckUrl() {
		return unCheckUrl;
	}

	public void setUnCheckUrl(List<String> unCheckUrl) {
		this.unCheckUrl = unCheckUrl;
	}

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		System.out.println("拦截后");
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		System.out.println("拦截中");
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
		String checkUrl = request.getRequestURI();
		System.out.println(checkUrl+"============");
		System.out.println(unCheckUrl);
//		if(unCheckUrl.contains(checkUrl)){
//			System.out.println("我不拦截=========");
//			return true;
//		}
		HttpSession Hsession = request.getSession();
		User user = (User) Hsession.getAttribute("user");
		if(user==null){
			user = new User();
			user.setUserName("游客");
			user.setUserId("10086");
			user.setRoleCode("10086");
			Hsession.setAttribute("user", user);
		}
		userT.set(user);
		System.out.println("拦截前"+request.getParameter("param"));
		setParam(request.getParameter("param"));
		return true;
	}

}
