package com.xynu.util;

import com.xynu.model.JsonResponse;

/**
 *
 * @author xiaosuda
 * @date 2018/3/16
 */
public class ControllerUtil {

    public static JsonResponse getResponse(Integer x, String successMsg, String failMsg) {
        if (x == null) {
            return new JsonResponse(false, "未知错误");
        }
        if(x > 0){
            return new JsonResponse(true, successMsg);
        } else {
            return new JsonResponse(false, failMsg);
        }
    }
}
