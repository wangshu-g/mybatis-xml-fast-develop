package com.wangshu.base.controller;

import com.wangshu.base.controller.daoru.ImportExcel;
import com.wangshu.base.controller.delete.DeleteService;
import com.wangshu.base.controller.export.ExportExcel;
import com.wangshu.base.controller.list.ListService;
import com.wangshu.base.controller.nestlist.NestListService;
import com.wangshu.base.controller.save.SaveService;
import com.wangshu.base.controller.select.SelectService;
import com.wangshu.base.controller.update.UpdateService;
import com.wangshu.base.mapper.BaseDataMapper;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.AbstractBaseDataService;
import com.wangshu.base.service.BaseDataService;

/**
 * @author GSF
 * <p>基础控制器,不经过任何包装,直接响应Service的结果</p>
 */
public abstract class AbstractBaseDataControllerService<S extends BaseDataService<?, ? extends BaseDataMapper<T>, T>, T extends BaseModel> extends AbstractBaseDataController<S, T> implements SaveService<S, T>, DeleteService<S, T>, UpdateService<S, T>, SelectService<S, T>, ListService<S, T>, NestListService<S, T>, ExportExcel<S, T>, ImportExcel<S, T> {

}
