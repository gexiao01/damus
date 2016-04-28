package com.ximalaya.damus.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class CollectionUtils {

    public static <T> List<T> sublist(List<T> input, int fromInclude, int toExclude) {
        Preconditions.checkArgument(fromInclude >= 0);
        Preconditions.checkArgument(toExclude >= fromInclude);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(input)) {
            return Collections.emptyList();
        }
        int inputSize = input.size();
        if (fromInclude >= inputSize) {
            return Collections.emptyList();
        }
        if (toExclude > inputSize) {
            toExclude = inputSize;
        }
        return input.subList(fromInclude, toExclude);
    }

    public static <T extends HasId<ID>, ID> Map<ID, T> collectionToMap(Collection<T> coll) {
        if (coll == null) {
            return null;
        }

        Builder<ID, T> builder = ImmutableMap.builder();
        for (T t : coll) {
            builder.put(t.getId(), t);
        }
        return builder.build();
    }

    public static <T extends HasId<ID>, ID> Collection<ID> getIds(Collection<T> coll) {
        if (coll == null) {
            return null;
        }

        Collection<ID> ids = new HashSet<ID>();
        for (T t : coll) {
            ids.add(t.getId());
        }
        return ids;
    }

    public static <T> List<T> getDatasByPage(List<T> datas, int page, int count) {
        int size = datas.size();

        if (page <= 0) {
            return Collections.emptyList();
        }

        if (count <= 0) {
            return Collections.emptyList();
        }

        int fromIndex = (page - 1) * count;
        if (fromIndex >= size) {
            return Collections.emptyList();
        }

        int toIndex = page * count;

        if (size < toIndex) {
            toIndex = size;
        }

        return datas.subList(fromIndex, toIndex);
    }

    public static Map<String, String> toStringMap(Map<?, ?> inputMap, boolean ignoreNull) {
        if (MapUtils.isEmpty(inputMap)) {
            return Collections.emptyMap();
        }
        Builder<String, String> builder = ImmutableMap.builder();
        for (Entry<?, ?> entry : inputMap.entrySet()) {
            if (ignoreNull && (entry.getKey() == null || entry.getValue() == null)) {
                continue;
            }
            builder.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return builder.build();
    }

    public static <K, V> V getOrCreateIfAbsent(ConcurrentHashMap<K, V> map, K key, V newValue) {
        V value = map.get(key);
        if (value != null) {
            return value;
        }
        value = map.putIfAbsent(key, newValue);
        if (value != null) {
            return value;
        }
        return map.get(key);
    }

    public static <T> List<T> nullToEmpty(List<T> input) {
        if (input == null) {
            return Collections.emptyList();
        } else {
            return input;
        }
    }
}
