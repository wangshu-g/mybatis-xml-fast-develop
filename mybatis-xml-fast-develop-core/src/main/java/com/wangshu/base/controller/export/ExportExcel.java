package com.wangshu.base.controller.export;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

public interface ExportExcel<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    @RequestMapping("/exportExcel")
    @ResponseBody
    public default void exportExcel(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Map<String, Object> requestParams = this.getRequestParams(request);
        this.getService().exportExcel(String.valueOf(requestParams.get("fileName")), this.getService().getNestList(requestParams).stream().map(BaseModel::toMap).toList(), response);
    }

}
