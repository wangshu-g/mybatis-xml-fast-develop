package com.wangshu.base.controller;

// MIT License
//
// Copyright (c) 2025 2560334673@qq.com wangshu-g https://github.com/wangshu-g/mybatis-xml-fast-develop
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

import com.wangshu.base.controller.delete.DeleteService;
import com.wangshu.base.controller.list.ListService;
import com.wangshu.base.controller.nestlist.NestListService;
import com.wangshu.base.controller.save.SaveService;
import com.wangshu.base.controller.select.SelectService;
import com.wangshu.base.controller.update.UpdateService;
import com.wangshu.base.model.BaseModel;
import com.wangshu.base.service.BaseDataService;

import java.util.Map;

/**
 * @author wangshu-g
 * <p>基础控制器,不经过任何包装,直接响应Service的结果</p>
 * <p>注意这里默认的删除调用的是 {@link BaseDataService#_delete(Map)} 真删除</p>
 * <p>conteollrt 接口，我的建议是根据自己的业务，新建一个选择性继承或覆写一些方法，满足常用业务规则即可</p>
 */
public abstract class AbstractBaseDataControllerService<S extends BaseDataService<?, T>, T extends BaseModel> extends AbstractBaseDataController<S, T> implements SaveService<S, T>, DeleteService<S, T>, UpdateService<S, T>, SelectService<S, T>, ListService<S, T>, NestListService<S, T> {

}
