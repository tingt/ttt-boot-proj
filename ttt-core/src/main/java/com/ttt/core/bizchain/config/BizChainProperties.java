package com.ttt.core.bizchain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author : tutingting
 * @description:
 * @date : 2020/4/22 下午4:14
 */
@ConfigurationProperties(prefix = "bizchain")
public class BizChainProperties {
    private List<ChainConfig> chains;

    public List<ChainConfig> getChains() {
        return chains;
    }

    public void setChains(List<ChainConfig> chains) {
        this.chains = chains;
    }
}
