package com.ttt.core.bizchain.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
/**
 * @author : tutingting
 * @description:
 * @date : 2020/4/22 下午4:15
 */
@Getter
@Setter
public class ChainConfig {
    private String id;
    private String clazz;
    private List<String> handles;
}
