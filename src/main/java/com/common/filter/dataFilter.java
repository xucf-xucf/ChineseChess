package com.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class dataFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("初始化过滤器。。。。。");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("过滤中。。。。。");
		chain.doFilter(request, response);
		
	}
//
	public void destroy() {
		System.out.println("》》销毁。。。。");
	}

}
