package com.ximalaya.damus.actuary.rest;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.dp.simplerest.annotation.Rest;
import com.dp.simplerest.binding.BindingMeta;

@Component
@Rest
public class IndexController implements ApplicationContextAware {

	@Autowired
	private Config config;

	private BindingMeta bindingMeta;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		bindingMeta = applicationContext.getBean(BindingMeta.class);
	}

	@Rest(path = "/")
	public String index() {
		return "Damus-Actuary v0.0.1 www.ximalaya.com";
	}

	@Rest(path = "/help")
	public Map<String, Method> apiHelp() {
		return bindingMeta.getApiMap();
	}

	@Rest(path = "/config")
	public Config listConfig() {
		return config;
	}

}
