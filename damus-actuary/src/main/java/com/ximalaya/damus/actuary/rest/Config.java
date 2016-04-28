package com.ximalaya.damus.actuary.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Value("damus.actuary.reducedfile.path")
    private String reducedfilePath;
    @Value("damus.actuary.dist.path")
    private String distPath;

    public Config() {
    }

    public String getDistPath() {
        return distPath;
    }

    public void setDistPath(String distPath) {
        this.distPath = distPath;
    }

    public String getReducedfilePath() {
        return reducedfilePath;
    }

    public void setReducedfilePath(String reducedfilePath) {
        this.reducedfilePath = reducedfilePath;
    }

}
