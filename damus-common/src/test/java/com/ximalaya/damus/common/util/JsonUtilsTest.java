// Copyright 2011-2015 Ximalaya All right reserved.

package com.ximalaya.damus.common.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.ximalaya.damus.common.util.JsonUtils;

/**
 * @author rocky.wang@ximalaya.com
 * @date 2015年8月24日
 */
public class JsonUtilsTest {

    static class TestObject {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @Test
    public void testToFieldMap() {
        TestObject testObject = new TestObject();
        testObject.setId(123);
        testObject.setName("testName");
        Map<String, Object> fieldMap = JsonUtils.toFieldMap(testObject);
        Map<String, Object> expectedMap = ImmutableMap.
                <String, Object> of("id", 123, "name", "testName");
        assertEquals(expectedMap, fieldMap);
    }
}
