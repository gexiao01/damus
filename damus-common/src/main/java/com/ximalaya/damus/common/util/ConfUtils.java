package com.ximalaya.damus.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;

public class ConfUtils {

	public static void configureLog4j() {
		InputStream is = null;
		try {
			is = ConfUtils.class.getResourceAsStream("/log4j.properties");
			Properties props = new Properties();
			props.load(is);
			PropertyConfigurator.configure(props);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static String[] loadActiveProfiles(String key) {
		InputStream is = null;
		try {
			is = ConfUtils.class.getResourceAsStream("/damus.properties");
			Properties props = new Properties();
			props.load(is);
			String profiles = props.getProperty(key);
			return profiles.split(",");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
}
