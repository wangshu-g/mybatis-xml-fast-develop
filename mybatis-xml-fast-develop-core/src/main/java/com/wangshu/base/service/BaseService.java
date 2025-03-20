package com.wangshu.base.service;

import java.util.UUID;

/**
 * @author wangshu-g
 * <p>BaseService</p>
 */
public interface BaseService {

    default String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
