package com.wangshu.base.controller.select;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.result.ResultBody;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

public interface SelectResultBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>查询一条</p>
     **/
    @RequestMapping("/select")
    @ResponseBody
    public default ResultBody<T> select(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        return ResultBody.success(this.getService().select(this.getRequestParams(request)));
    }


}
