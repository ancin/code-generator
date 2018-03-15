package io.code.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * @author songkejun
 * @create 2018-02-07 11:13
 **/
public class JsonUtils {

    public static Object toBean(String json,Class<?> clz){
        if (StringUtils.isEmpty(json)){
            return null;
        }
        Object jsonObject=JSONObject.parseObject(JSON.parse(json).toString(),clz);
        return jsonObject;
    }
}
