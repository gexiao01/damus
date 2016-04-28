package com.ximalaya.damus.offline;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context.xml")
public class BaseContextTest {

	Logger logger = Logger.getLogger(getClass());

	@Test
	public void pass() throws Exception {
	}
}
