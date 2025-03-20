package com.wangshu.base.controller;

import com.wangshu.base.controller.delete.DeleteResultBody;
import com.wangshu.base.controller.list.ListTableResultTableBody;
import com.wangshu.base.controller.nestlist.NestListResultTableBody;
import com.wangshu.base.controller.save.SaveResultBody;
import com.wangshu.base.controller.select.SelectResultBody;
import com.wangshu.base.controller.update.UpdateResultBody;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

/**
 * @author wangshu-g
 * <p>BaseControllerImpl</p>
 */
public abstract class AbstractBaseDataControllerResultTableBody<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> implements SaveResultBody<S, T>, DeleteResultBody<S, T>, UpdateResultBody<S, T>, SelectResultBody<S, T>, ListTableResultTableBody<S, T>, NestListResultTableBody<S, T> {

}
