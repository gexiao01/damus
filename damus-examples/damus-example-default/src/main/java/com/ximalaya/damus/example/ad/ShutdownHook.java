package com.ximalaya.damus.example.ad;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dp.simplerest.NettyHttpServer;

/**
 * 关闭钩子，释放进程锁并关闭Spring
 * 
 * @author xiao.ge
 * @date 2015.7.30
 *
 */
public class ShutdownHook extends Thread {

	private ClassPathXmlApplicationContext context;

	private static Logger logger = Logger.getLogger(ShutdownHook.class);

	public ShutdownHook(ClassPathXmlApplicationContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			logger.info("Shutting Down Damus-Auctuary: ");

			NettyHttpServer restServer = context.getBean(NettyHttpServer.class);
			restServer.close();
			context.close();
			logger.info("Damus-Auctuary Shutted Down ");
		} catch (Exception e) {
			logger.error("Error when Shutting down Damus-Auctuary", e);
		}
	}
}
