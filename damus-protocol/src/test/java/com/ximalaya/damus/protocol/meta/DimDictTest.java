package com.ximalaya.damus.protocol.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ximalaya.damus.protocol.config.Constant;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.util.SerializeUtil;

public class DimDictTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testSerialization() throws Exception {
		InputStream is = new FileInputStream("./data/damus-demo.meta");
		DimDict dict = SerializeUtil.deserializeFromStream(is, DimDict.class);

		DimMeta posMeta = dict.getDimMeta(DimType.POSITION);
		Assert.assertEquals(DimType.POSITION, posMeta.getType());
		Assert.assertEquals(1L, posMeta.getId("loading"));
		Assert.assertEquals("find_banner", posMeta.getValue(2L));
		Assert.assertEquals(Constant.UNKNOWN_VALUE, posMeta.getValue(Constant.UNKNOWN_ID));
		Assert.assertEquals(3 + 1, posMeta.getIdMap().size()); // add 1 for
																// unknown

		Assert.assertEquals(0, dict.getDimMeta(DimType.HOUR).getIdMap().size()); // not
																					// literal

		File tempFile = new File("./damus.meta.tmp");
		dict.serialize(new FileOutputStream(tempFile));
		List<String> lines = FileUtils.readLines(tempFile);
		System.out.println(StringUtils.join(lines, "\n"));
		Assert.assertEquals(11, lines.size());
		FileUtils.forceDelete(tempFile);
	}

	@Test
	public void testMerge() throws Exception {
		DimDict dict = SerializeUtil.deserializeFromStream(new FileInputStream(new File("./data/damus.meta")),
				DimDict.class);
		DimDict deltaDict = SerializeUtil.deserializeFromStream(
				new FileInputStream(new File("./data/damus.meta.delta")), DimDict.class);
		dict.merge(deltaDict);

		DimMeta posMeta = dict.getDimMeta(DimType.RESOLUTION);
		Assert.assertEquals("999*999", posMeta.getValue(999L));
	}
}
