package com.ximalaya.damus.offline;

import org.springframework.beans.factory.annotation.Autowired;

import com.ximalaya.damus.offline.service.CalcService;
import com.ximalaya.damus.offline.tools.ReduceFlowToLiteral;

public class DamusOfflineMainTest extends BaseContextTest {

	@Autowired
	private CalcService calcService;

	@Override
	public void pass() throws Exception {
		calcService.calcReduceflow();
		calcService.destroy();

		String rootPath = "file:/Users/xiaoge/Documents/damus/damus-offline/";
		String reducedFlowPath = "./data/output/reducedflow";
		String dictPath = "./data/damus.meta";
		ReduceFlowToLiteral.readReducedFlow(dictPath, rootPath + reducedFlowPath + "/part-*", rootPath
				+ reducedFlowPath + "/literal");
	}
}
