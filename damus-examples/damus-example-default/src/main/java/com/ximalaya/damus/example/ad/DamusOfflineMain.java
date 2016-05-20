// 文件名称: Main.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.example.ad;

import java.io.IOException;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.common.util.ConfUtils;
import com.ximalaya.damus.offline.service.CalcService;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月1日
 */
public class DamusOfflineMain {

	private static Logger logger;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ConfUtils.configureLog4j();

		logger = Logger.getLogger(DamusOfflineMain.class);

		String[] activeProfiles = ConfUtils.loadActiveProfiles("damus.offline.profiles");

		logger.info("Start Damus-Offline Main with profiles: " + Arrays.asList(activeProfiles));

		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.getEnvironment().setActiveProfiles(activeProfiles);
		ctx.setConfigLocation("classpath:damus-offline-example.xml");
		ctx.refresh();

		ctx.registerShutdownHook();

		CalcService calcService = ctx.getBean(CalcService.class);

		try {
			calcService.calcReduceflow();
		} catch (DamusException e) {
			logger.error("Damus Offline error", e);
		} catch (IOException e) {
			logger.error("Damus Offline unexpected error", e);
		}

	}
}
