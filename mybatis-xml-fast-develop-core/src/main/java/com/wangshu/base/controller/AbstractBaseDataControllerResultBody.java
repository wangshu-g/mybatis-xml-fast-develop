package com.wangshu.base.controller;

import com.wangshu.base.controller.delete.DeleteResultBody;
import com.wangshu.base.controller.list.ListResultBody;
import com.wangshu.base.controller.nestlist.NestListResultBody;
import com.wangshu.base.controller.save.SaveResultBody;
import com.wangshu.base.controller.select.SelectResultBody;
import com.wangshu.base.controller.update.UpdateResultBody;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

/**
 * @author wangshu-g
 * <p>基础控制器,所有方法响应数据{@link com.wangshu.base.result.ResultBody}包装后的数据</p>
 */
public abstract class AbstractBaseDataControllerResultBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends AbstractBaseDataController<S, T> implements SaveResultBody<S, T>, DeleteResultBody<S, T>, UpdateResultBody<S, T>, SelectResultBody<S, T>, ListResultBody<S, T>, NestListResultBody<S, T> {

}
