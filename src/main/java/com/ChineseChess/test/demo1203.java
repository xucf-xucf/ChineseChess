package com.ChineseChess.test;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;

public class demo1203 extends HttpServlet{
	
	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("123");
		set.add("456");
		set.add("123");
		System.out.println(set.size());
	}
}

