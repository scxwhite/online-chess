package com.xyny.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc  json返回类型
 * @author xiaosuda
 * @date 2017/12/26
 */
@Data
public class JsonResponse<T> implements Serializable{
    /**
     * 标志   true：查询成功
     *          false: 查询失败
     */
    private boolean isSuccess;
    /**
     * 返回的信息
     */
    private String  message;
    /**
     * 要返回的结果
     */
    private T       result;

    public JsonResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public JsonResponse(boolean isSuccess, String message, T result) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.result = result;
    }
}
