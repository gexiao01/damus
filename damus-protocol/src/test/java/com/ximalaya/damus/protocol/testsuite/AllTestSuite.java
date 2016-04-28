package com.ximalaya.damus.protocol.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ximalaya.damus.protocol.meta.DimDictTest;
import com.ximalaya.damus.protocol.reduced.ReducedFlowTest;
import com.ximalaya.damus.protocol.resource.FileResouceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ReducedFlowTest.class, DimDictTest.class, FileResouceTest.class })
public class AllTestSuite {

}
