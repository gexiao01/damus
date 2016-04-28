// 文件名称: Main.java Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.offline;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.offline.service.CalcService;

/**
 * @author lester.zhou@ximalaya.com
 * @date 2015年12月1日
 */
public class DamusOfflineMain {

    private static Logger logger;

    private static void configureLog4j() {
        InputStream is = null;
        try {
            is = DamusOfflineMain.class.getResourceAsStream("/log4j.properties");
            Properties props = new Properties();
            props.load(is);
            PropertyConfigurator.configure(props);
            logger = Logger.getLogger(DamusOfflineMain.class);
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
            is = DamusOfflineMain.class.getResourceAsStream("/damus.properties");
            Properties props = new Properties();
            props.load(is);
            String profiles = props.getProperty("damus.offline.profiles");
            return profiles.split(",");
        } catch (Exception e) {
            logger.error("Fail to load Active Profile Config", e);
            System.exit(1);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        configureLog4j();

        String[] activeProfiles = loadActiveProfiles();
        logger.info("Start Damus-Offline Main with profiles: " + Arrays.asList(activeProfiles));
        // load spring context
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ctx.getEnvironment().setActiveProfiles(activeProfiles);
        ctx.setConfigLocation("classpath:application-context.xml");
        ctx.refresh();

        // register shutdown hook
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
