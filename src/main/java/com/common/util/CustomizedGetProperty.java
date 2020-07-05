package com.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
//自动获取spring.xml中装载的proprties配置文件
public class CustomizedGetProperty extends PropertyPlaceholderConfigurer{
	private static Map<String, Object> ctxPropertiesMap;
	

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);//关键的一行
		ctxPropertiesMap = new HashMap<String, Object>();
		
		System.out.println("------------------------------------------");
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			ctxPropertiesMap.put(keyStr, value);
			System.out.println("读取"+keyStr+"="+value);
		}
	}

	public static Object getContextProperty(String name) {
		return ctxPropertiesMap.get(name);
	}
	
	
	
	
}
