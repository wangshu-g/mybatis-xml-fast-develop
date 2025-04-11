package com.wangshu.base.controller;

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
public abstract class AbstractBaseDataControllerString<S extends BaseDataService<?,  T>, T extends BaseModel> extends AbstractBaseDataController<S, T> implements Save<S, T>, Delete<S, T>, Update<S, T>, Select<S, T>, List<S, T>, NestList<S, T> {

}
