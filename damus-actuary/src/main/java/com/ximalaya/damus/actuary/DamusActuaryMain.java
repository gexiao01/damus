package com.ximalaya.damus.actuary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dp.simplerest.NettyHttpServer;

/**
 * main() for damus-auctuary
 */
public class DamusActuaryMain {

	static Logger logger = Logger.getLogger(DamusActuaryMain.class);

	private static void configureLog4j() {
		InputStream is = null;
		try {
			is = DamusActuaryMain.class.getResourceAsStream("/log4j.properties");
			Properties props = new Properties();
			props.load(is);

			// 文件级别分离实时/离线日志
			PropertyConfigurator.configure(props);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static String[] loadActiveProfiles() {
		InputStream is = null;
		try {
			is = DamusActuaryMain.class.getResourceAsStream("/damus.properties");
			Properties props = new Properties();
			props.load(is);
			String profiles = props.getProperty("damus.actuary.profiles");
			return profiles.split(",");
		} catch (Exception e) {
			logger.error("Fail to load Active Profile Config", e);
			System.exit(1);
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static void main(String[] args) {

		configureLog4j();

		String[] activeProfiles = loadActiveProfiles();
		logger.info("Start Damus-Actuary Main with profiles: " + Arrays.asList(activeProfiles));

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();

		Runtime.getRuntime().addShutdownHook(new ShutdownHook(ctx));

		// ctx.getEnvironment().setActiveProfiles("file", "local", "fifo");
		ctx.getEnvironment().setActiveProfiles(activeProfiles);
		ctx.setConfigLocation("classpath:application-context.xml");
		ctx.refresh();

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
