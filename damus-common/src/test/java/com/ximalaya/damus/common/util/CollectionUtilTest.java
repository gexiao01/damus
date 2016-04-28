// Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.common.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.ximalaya.damus.common.util.CollectionUtils;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年8月24日
 */
public class CollectionUtilTest {

    @Test
    public void testToStringMap() {
        Map<Object, Object> map = ImmutableMap.<Object, Object> of(1, "v1", "name", 2, false, 123L);
        Map<String, String> resultMap = CollectionUtils.toStringMap(map, true);
        Map<String, String> expectMap = ImmutableMap.of("1", "v1", "name", "2", "false", "123");
        assertEquals(expectMap, resultMap);
    }

}
