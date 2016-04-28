package com.ximalaya.damus.protocol.reduced;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.meta.DimDict;
import com.ximalaya.damus.protocol.util.SerializeUtil;

public class ReducedFlowTest {

	@Test
	public void testSerialization() throws Exception {
		DimDict dict = SerializeUtil.deserializeFromStream(new FileInputStream(new File("./data/damus.meta")),
				DimDict.class);

		ReducedFlow flow = new ReducedFlow();
		flow.put(DimType.POSITION, 1L, dict);
		flow.put(DimType.RESOLUTION, 2L, dict);
		flow.put(DimType.PACKAGE, 999999L, dict);

		System.out.println(flow.toPrettyFormat(dict));

		File tempFile = new File("./reduced-flow.tmp");
		flow.serialize(new FileOutputStream(tempFile));

		ReducedFlow retFlow = SerializeUtil.deserializeFromStream(new FileInputStream(tempFile), ReducedFlow.class);
		FileUtils.forceDelete(tempFile);
		System.out.println(retFlow.toPrettyFormat(dict));

		Assert.assertEquals(1L, retFlow.get(DimType.POSITION));
		Assert.assertEquals(2L, retFlow.get(DimType.RESOLUTION));
		Assert.assertEquals(Constant.UNKNOWN_ID, retFlow.get(DimType.PACKAGE));
		Assert.assertEquals(Constant.UNKNOWN_ID, retFlow.get(DimType.OS));
	}
}
