package com.wangshu.base.controller.list;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.result.ResultBody;
import com.wangshu.base.result.ResultTableBody;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.tool.CacheTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ListTableResultTableBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>查询列表</p>
     **/
    @RequestMapping("/getList")
    @ResponseBody
    public default ResultBody<List<Map<String, Object>>> getList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        Map<String, Object> params = this.getRequestParams(request);
        return ResultTableBody.success(this.getService().getList(params), this.getService().getTotal(params), CacheTool.getControllerModelGenericColumnType(this.getClass()));
    }

}
