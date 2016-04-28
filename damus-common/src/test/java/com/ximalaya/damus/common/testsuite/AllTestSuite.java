package com.ximalaya.damus.common.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ximalaya.damus.common.util.CollectionUtilTest;
import com.ximalaya.damus.common.util.JsonUtilsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ CollectionUtilTest.class, JsonUtilsTest.class })
public class AllTestSuite {

}
