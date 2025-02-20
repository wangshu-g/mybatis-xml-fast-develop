package com.wangshu.base.controller.update;

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

import java.io.IOException;

public interface UpdateResultBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>更新</p>
     **/
    @PostMapping("/update")
    @ResponseBody
    public default ResultBody<Object> update(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int line = this.getService().update(this.getRequestParams(request));
        return line > 0 ? ResultBody.success(line) : ResultBody.error("更新失败");
    }

}
