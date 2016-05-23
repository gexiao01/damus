package com.ximalaya.damus.relativity;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ximalaya.damus.common.util.ConfUtils;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.relativity.service.RelativityService;

public class DamusRelativityMain {

	private static Logger logger;

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		DimType dim1, dim2;
		try {
			dim1 = DimType.valueOf(args[0]);
			dim2 = DimType.valueOf(args[1]);
		} catch (Exception e) {
			logger.error("Fail to Parse Args", e);
			return;
		}

		ConfUtils.configureLog4j();

		logger = Logger.getLogger(DamusRelativityMain.class);

		final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.setConfigLocation("classpath:damus-relativity.xml");
		ctx.refresh();

		ctx.registerShutdownHook();

		RelativityService service = ctx.getBean(RelativityService.class);

		try {
			double r = service.calcRelativity(dim1, dim2);
			System.err.println("Relativity=" + r);
		} catch (Exception e) {
			logger.error("Damus Relativity unexpected error", e);
		}

	}
}
