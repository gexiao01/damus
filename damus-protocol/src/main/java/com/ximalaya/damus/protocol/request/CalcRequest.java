package com.ximalaya.damus.protocol.request;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.ximalaya.damus.common.util.IdUtils;
import com.ximalaya.damus.protocol.config.DimType;

/**
 * entity of a calculation request
 *
 * @author xiao.ge
 * @date 20151130
 */
public class CalcRequest implements Serializable {

    private static final long serialVersionUID = 2600973787632403694L;

    // here we make the convention that
    // !targets.containsKey(DimType)==true means ALL-Targeting in this dimension
    private final EnumMap<DimType, Collection<Long>> targets = new EnumMap<DimType, Collection<Long>>(
            DimType.class);
    private final EnumMap<DimType, Collection<Long>> excludes = new EnumMap<DimType, Collection<Long>>(
            DimType.class);

    private final long id;

    public CalcRequest() {
        id = IdUtils.generateId();
    }

    /**
     * for test usage only
     */
    public CalcRequest(long id) {
        this.id = id;
    }

    public EnumMap<DimType, Collection<Long>> getTargets() {
        return this.targets;
    }

    public EnumMap<DimType, Collection<Long>> getExcludes() {
        return excludes;
    }

    public void put(DimType type, Collection<Long> target) {
        targets.put(type, target);
    }

    public void putExclude(DimType type, Collection<Long> exclude) {
        excludes.put(type, exclude);
    }

    public void add(DimType type, Collection<Long> targetValues) {
        if (CollectionUtils.isEmpty(targetValues)) {
            return;
        }
        Collection<Long> values = getValues(targets, type);
        values.addAll(targetValues);
    }

    public void exclude(DimType type, Collection<Long> targetValues) {
        if (CollectionUtils.isEmpty(targetValues)) {
            return;
        }
        Collection<Long> values = getValues(excludes, type);
        values.addAll(targetValues);
    }

    public void add(DimType type, long... targetValues) {
        if (ArrayUtils.isEmpty(targetValues)) {
            return;
        }
        Collection<Long> target = getValues(targets, type);
        for (long targetValue : targetValues) {
            target.add(targetValue);
        }
    }

    public void exclude(DimType type, long... targetValues) {
        if (ArrayUtils.isEmpty(targetValues)) {
            return;
        }
        Collection<Long> exclude = getValues(excludes, type);
        for (long targetValue : targetValues) {
            exclude.add(targetValue);
        }
    }

    private static Collection<Long> getValues(Map<DimType, Collection<Long>> map, DimType dimType) {
        Collection<Long> values = map.get(dimType);
        if (values == null) {
            values = new HashSet<Long>();
            map.put(dimType, values);
        }
        return values;
    }

    public boolean hit(Map<DimType, Long> valuesMap) {
        Set<DimType> includeDimTypes = targets.keySet();
        Set<DimType> excludeDimTypes = excludes.keySet();

        for (DimType type : DimType.values()) {
            // might be null
            Long value = valuesMap.get(type);
            Collection<Long> includeValues = targets.get(type);
            Collection<Long> excludeValues = excludes.get(type);
            if (!((includeValues == null || includeValues.contains(value)) && (excludeValues == null || !excludeValues
                    .contains(value)))) {
                return false;
            }
        }

        for (DimType type : excludeDimTypes) { // excludes first
            Long value = valuesMap.get(type);
            Collection<Long> excludeValues = excludes.get(type);
            if (excludeValues != null && excludeValues.contains(value)) {
                return false;
            }
        }

        // don't iterate DimType.values since some may not be contained in targets.keySet()
        for (DimType type : includeDimTypes) {
            Long value = valuesMap.get(type);
            Collection<Long> values = targets.get(type); // won't be null

            if (values == null || !values.contains(value)) {
                return false;
            }
        }

        return true;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CalcRequest{id=" + id + " targets=" + targets + "}";
    }
}
