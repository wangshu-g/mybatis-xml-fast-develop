package com.wangshu.base.mapper;

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

import com.wangshu.base.model.BaseModel;

import java.util.List;
import java.util.Map;

/**
 * @author wangshu-g
 * <p>BaseMapper,常用方法</p>
 */
public interface BaseDataMapper<T extends BaseModel> extends BaseMapper {

    /**
     * <p>保存</p>
     *
     * @param model 保存参数,继承 {@link BaseModel}
     * @return int 影响行数
     **/
    int _save(T model);

    /**
     * <p>批量保存</p>
     *
     * @param modelList 保存参数,继承 {@link BaseModel}
     * @return int 影响行数
     **/
    int _batchSave(List<T> modelList);

    /**
     * <p>删除</p>
     *
     * @param map 删除条件
     * @return int 影响行数
     **/
    int _delete(Map<String, Object> map);

    /**
     * <p>更新</p>
     *
     * @param map 更新参数
     * @return int 影响行数
     **/
    int _update(Map<String, Object> map);

    /**
     * <p>查询一条</p>
     *
     * @param map 查询条件
     * @return T extends {@link BaseModel}
     **/
    T _select(Map<String, Object> map);

    /**
     * <p>查询列表</p>
     *
     * @param map 查询条件
     * @return List Map String Object
     **/
    List<Map<String, Object>> _getList(Map<String, Object> map);

    /**
     * <p>查询列表</p>
     *
     * @param map 查询条件
     * @return List T extends BaseModel
     **/
    List<T> _getNestList(Map<String, Object> map);

    /**
     * <p>数据条数</p>
     *
     * @param map 查询参数
     * @return int 数据条数
     **/
    int _getTotal(Map<String, Object> map);

}
