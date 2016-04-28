package com.ximalaya.damus.protocol.resource;

import org.junit.Assert;
import org.junit.Test;

import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.dist.OneDDist;

public class FileResouceTest {

	@Test
	public void testFileResource() throws Exception {
		FileResource<OneDDist> resource = new FileResource<OneDDist>("./data/oned.dist", OneDDist.class);
		resource.load();
		OneDDist dist = resource.get();

		Assert.assertNotNull(dist);
		Assert.assertEquals(40L, dist.get(DimType.POSITION, 1L));
	}

}
