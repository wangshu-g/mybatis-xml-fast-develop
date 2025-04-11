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
