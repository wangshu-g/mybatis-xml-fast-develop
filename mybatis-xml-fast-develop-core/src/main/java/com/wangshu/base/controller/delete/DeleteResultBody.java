package com.wangshu.base.controller.delete;

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

import java.util.Map;

public interface DeleteResultBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends BaseDataController<S, T> {

    /**
     * <p>删除</p>
     **/
    @PostMapping("/delete")
    @ResponseBody
    public default ResultBody<Object> delete(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        Map<String, Object> map = this.getRequestParams(request);
        int line = this.getService().delete(map);
        return line > 0 ? ResultBody.success() : ResultBody.error("记录不存在");
    }

}
