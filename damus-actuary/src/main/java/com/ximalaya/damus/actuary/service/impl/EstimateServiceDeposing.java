package com.ximalaya.damus.actuary.service.impl;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.ximalaya.damus.actuary.service.EstimateService;
import com.ximalaya.damus.common.exception.DamusException;
import com.ximalaya.damus.protocol.config.DimType;
import com.ximalaya.damus.protocol.dist.OneDDist;
import com.ximalaya.damus.protocol.request.CalcRequest;
import com.ximalaya.damus.protocol.resource.Resource;

/**
 * impl of CalcService using dimensional decomposing method
 * 
 * @author xiao.ge
 * @date 20151203
 */
public class EstimateServiceDeposing implements EstimateService, InitializingBean {

    private BigDecimal[] totalFlowPower;

    // one-d distribution from damux-offline output, will automatically load & refresh respecting to
    // the impl injected
    @Autowired
    @Qualifier("oneDDistResource")
    private Resource<OneDDist> dist;

    @Value("${damus.offline.sample}")
    private double sampleRatio;

    @Override
    public void afterPropertiesSet() throws Exception {

        // if (dist.get() == null) {
        // dist.load(); // load first time by hand
        // }
        totalFlowPower = new BigDecimal[DimType.values().length + 1];
        totalFlowPower[0] = new BigDecimal(1);
        BigDecimal total = new BigDecimal(dist.get().getTotal());
        // minus 1 for flow magnitude
        for (int i = 1; i <= DimType.values().length; i++) {
            totalFlowPower[i] = totalFlowPower[i - 1].multiply(total);
        }
    }

    @Override
    public long handleRequest(CalcRequest request) throws DamusException {
        if (MapUtils.isEmpty(request.getTargets())) {
            return 0L;
        }
        BigDecimal numerator = new BigDecimal(1);
        for (DimType type : DimType.values()) {
            // might be null
            Collection<Long> includeValues = request.getTargets().get(type);
            Collection<Long> excludeValues = request.getExcludes().get(type);

            BigDecimal hits;

            // includes
            if (includeValues == null) {
                hits = new BigDecimal(dist.get().getTotal());
            } else {
                hits = new BigDecimal(0);
                for (long id : includeValues) {
                    hits = hits.add(new BigDecimal(dist.get().get(type, id)));
                }
            }

            // excludes
            if (excludeValues != null) {
                for (long id : excludeValues) {
                    if (includeValues == null || includeValues.contains(id)) {
                        hits = hits.subtract(new BigDecimal(dist.get().get(type, id)));
                    }
                }
            }
            numerator = numerator.multiply(hits);
        }

        // for (DimType type : request.getTargets().keySet()) {
        // BigDecimal hits = new BigDecimal(0);
        // for (long id : request.getTargets().get(type)) {
        // hits = hits.add(new BigDecimal(dist.get().get(type, id)));
        // }
        // numerator = numerator.multiply(hits);
        // }

        // divide in the end to preserve precision
        BigDecimal result = numerator.divide(totalFlowPower[DimType.values().length - 1], 0,
                BigDecimal.ROUND_HALF_UP);

        long unsampledHits = (long) (result.longValue() / sampleRatio);
        return unsampledHits;
    }
}
