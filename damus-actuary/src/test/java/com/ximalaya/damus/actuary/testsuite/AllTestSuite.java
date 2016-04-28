package com.ximalaya.damus.actuary.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ximalaya.damus.actuary.service.ActuaryServiceTest;
import com.ximalaya.damus.actuary.service.RequestParserTest;
import com.ximalaya.damus.actuary.service.EstimateServiceDeposingTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ EstimateServiceDeposingTest.class, ActuaryServiceTest.class, RequestParserTest.class })
public class AllTestSuite {

}
