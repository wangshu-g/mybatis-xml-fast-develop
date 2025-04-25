package com.wangshu.base.controller.struct;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.result.ResultBody;
import com.wangshu.base.service.BaseDataService;
import com.wangshu.tool.CacheTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * <p>用于返回实体类结构信息</p>
 **/
public interface StructResultBody<S extends BaseDataService<?, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>实体结构信息</p>
     **/
    @PostMapping("/struct")
    @ResponseBody
    public default ResultBody<?> struct(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        return ResultBody.success(CacheTool.getControllerModelGenericColumnMetadata(this.getClass()));
    }

}
