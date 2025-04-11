package com.wangshu.base.query;

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

import com.wangshu.base.entity.EntityTool;
import com.wangshu.base.model.BaseModel;
import com.wangshu.tool.CacheTool;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author wangshu-g
 *
 * <p>查询参数，参数合法性和规范性问题？hhc</p>
 **/
public class Query<T extends BaseModel> extends EntityTool {

    @Override
    public List<Field> modelFields() {
        return CacheTool.getQueryFields(this.getClass());
    }

    @SuppressWarnings("unchecked")
    public T toModel() {
        return (T) this.toEntity(CacheTool.getQueryModelGeneric(this.getClass()));
    }

}