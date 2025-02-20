package com.wangshu.base.controller.save;

import com.wangshu.base.controller.BaseDataController;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.result.ResultBody;
import com.wangshu.base.service.BaseDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface Save<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {


    /**
     * <p>保存</p>
     **/
    @PostMapping("/save")
    @ResponseBody
    public default String save(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        int line = this.getService().save(this.getRequestParams(request));
        return line > 0 ? ResultBody.success(line).toJson() : ResultBody.error("保存失败").toJson();
    }

}
