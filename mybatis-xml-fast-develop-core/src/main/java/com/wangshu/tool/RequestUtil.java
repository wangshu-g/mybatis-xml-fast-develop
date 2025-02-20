package com.wangshu.tool;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.wangshu.enu.CommonErrorInfo;
import com.wangshu.exception.IException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestUtil {

    public static Map<String, Object> getRequestParams(@NotNull HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (StrUtil.equals(request.getMethod(), RequestMethod.POST.name())) {
            try {
                String requestBodyStr = IoUtil.readUtf8(request.getInputStream());
                if (StringUtil.isNotEmpty(requestBodyStr)) {
                    map = JSON.parseObject(requestBodyStr);
                }
            } catch (IORuntimeException | IOException e) {
                throw new IException(CommonErrorInfo.BODY_NOT_MATCH);
            }
        } else if (StrUtil.equals(request.getMethod(), RequestMethod.GET.name())) {
            String queryString = request.getQueryString();
            if (StringUtil.isNotEmpty(queryString)) {
                queryString = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);
                String[] params = queryString.split("&");
                for (String param : params) {
                    if (StringUtil.isNotEmpty(param)) {
                        String[] keyVal = param.split("=");
                        if (keyVal.length == 2) {
                            map.put(keyVal[0], keyVal[1]);
                        }
                    }
                }
            }
        }
        return map;
    }

}
