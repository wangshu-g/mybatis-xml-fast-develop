package com.wangshu.base.controller.nestlist;

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
import java.util.List;

public interface NestListResultBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>查询列表</p>
     **/
    @RequestMapping("/getNestList")
    @ResponseBody
    public default ResultBody<List<T>> getNestList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        return ResultBody.success(this.getService().getNestList(this.getRequestParams(request)));
    }

}
