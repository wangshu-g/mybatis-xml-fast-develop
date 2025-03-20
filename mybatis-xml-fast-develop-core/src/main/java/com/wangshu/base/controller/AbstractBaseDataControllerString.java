package com.wangshu.base.controller;

import com.wangshu.base.controller.delete.Delete;
import com.wangshu.base.controller.list.List;
import com.wangshu.base.controller.nestlist.NestList;
import com.wangshu.base.controller.save.Save;
import com.wangshu.base.controller.select.Select;
import com.wangshu.base.controller.update.Update;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

/**
 * @author wangshu-g
 * <p>基础控制器,所有方法响应数据{@link com.wangshu.base.result.ResultBody}包装后的JSON字符串数据</p>
 */
public abstract class AbstractBaseDataControllerString<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends AbstractBaseDataController<S, T> implements Save<S, T>, Delete<S, T>, Update<S, T>, Select<S, T>, List<S, T>, NestList<S, T> {

}
