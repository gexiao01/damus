package com.ximalaya.damus.example.ad;

import java.util.Arrays;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dp.simplerest.NettyHttpServer;
import com.ximalaya.damus.common.util.ConfUtils;

/**
 * main() for damus-auctuary
 */
public class DamusActuaryMain {

	static Logger logger = Logger.getLogger(DamusActuaryMain.class);

	public static void main(String[] args) {

		ConfUtils.configureLog4j();

		String[] activeProfiles = ConfUtils.loadActiveProfiles("damus.actuary.profiles");
		logger.info("Start Damus-Actuary Main with profiles: " + Arrays.asList(activeProfiles));

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();

		Runtime.getRuntime().addShutdownHook(new ShutdownHook(ctx));

		ctx.getEnvironment().setActiveProfiles(activeProfiles);
		ctx.setConfigLocation("classpath:damus-actuary-example.xml");
		// ctx.setConfigLocation("classpath:ads-dbconf.xml");
		ctx.refresh();

		// TODO
		/**
		 * start rest server
		 */
		NettyHttpServer restServer = ctx.getBean(NettyHttpServer.class);
		try {
			restServer.start();
		} catch (Exception e) {
			logger.error("fail to start REST Server at " + restServer.getHost());
			System.exit(1);
		}
	}
}
