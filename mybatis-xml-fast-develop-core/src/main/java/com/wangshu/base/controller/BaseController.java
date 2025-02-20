package com.wangshu.base.controller;

import com.wangshu.tool.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.UUID;

/**
 * @author GSF
 * <p>BaseController</p>
 */
public interface BaseController {

    default Map<String, Object> getRequestParams(HttpServletRequest request) {
        return RequestUtil.getRequestParams(request);
    }

    default String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
