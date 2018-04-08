package com.xynu.mapper;

import java.util.List;

/**
 * Created by xiaosuda on 2018/3/16.
 */
public interface BaseMapper<T> {

    /**
     *
     * @param id
     * @return
     */
    T findById(String id);


    /**
     * 获得所有
     * @return 对象集合
     */
    List<T> selectAll();

    /**
     *  添加
     * @param t 对象
     * @return  更新的条数
     */
    Integer insert(T t);

    /**
     * 根据id删除
     * @param id   id
     * @return  更新的条数
     */
    Integer deleteById(Integer id);

    /**
     * 根据id删除
     * @param id   id
     * @return  更新的条数
     */
    Integer deleteById(String id);

    /**
     *  根据id查找对象
     * @param id    id
     * @return  对象
     */
    T selectById(Integer id);

    /**
     *  根据id查找对象
     * @param id    id
     * @return  对象
     */
    T selectById(String id);

    /**
     * 根据t.id更新信息
     * @param t  对象
     * @return  更新的条数
     */
    Integer updateById(T t);
}
