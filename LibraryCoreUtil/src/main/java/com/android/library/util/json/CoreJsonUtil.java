package com.android.library.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.library.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lixurong on 2017/9/11.
 */

public class CoreJsonUtil {

    public static String parseJsonBykey(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.optString(key);
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public static int parseIntJsonBykey(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.optInt(key);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * json获取boolean值
     *
     * @param obj
     * @param key
     * @return
     */
    public static boolean parseJsonBykeyBool(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getBoolean(key);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }
    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String || obj instanceof Integer
                || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double
                || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        } else if (obj instanceof Object[]) {
            json.append(array2json((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(list2json((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(map2json((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(set2json((Set<?>) obj));
        } else {
            // json.append(bean2json(obj));
        }
        return json.toString();
    }

    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String string2json(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, l = s.length(); i < l; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= '\u0000' && ch <= '\u001F') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0, size = 4 - ss.length(); k < size; k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }
    // --------------------------fastjson相关方法-------------------------------
    /**
     * 快速转换json-bean,数据转换类型可以无限递归
     *
     * @param jsonString
     * @param cls
     * @return 注意：强转的bean需要序列化
     */
    public static <T> T getJsonBean(String jsonString, Class<T> cls) {
        T bean = null;
        try {
            bean = (T) JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return bean;
    }

    /**
     * 快速转换json-ArrayList,数据转换类型可以无限递归
     *
     * @param jsonString
     * @param cls
     * @return 注意：强转的bean需要序列化
     */
    public static <T> ArrayList<T> getJsonList(String jsonString, Class<T> cls) {
        ArrayList<T> list = new ArrayList<>();
        try {
            ArrayList<T> data = (ArrayList<T>) JSON.parseArray(jsonString, cls);
            if(data != null) {
                list.addAll(data);
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return list;
    }

    /**
     * 快速转换json-bean,用于含有泛型的Json解析，详情请看违章模块的如何使用的
     *
     * @param jsonString
     * @return 注意：强转的bean需要序列化
     */
    public static <T> T getJsonByTypeReference(String jsonString,
                                               TypeReference<T> mTypeReference) {
        T bean = null;
        try {
            bean = (T) JSON.parseObject(jsonString, mTypeReference);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return bean;
    }

    /**
     * 将JavaBean序列化为JSON文本
     *
     * @param object
     * @return
     */
    public static String getJsonFromObject(Object object) {
        return com.alibaba.fastjson.JSONArray.toJSONString(object, true);
    }

    /**
     * 获取josn数据终端data字段
     *
     * @param response
     * @return
     */
    public static String getResponseDate(String response) {
        String data = null;
        try {
            JSONObject obj = new JSONObject(response);
            data = parseJsonBykey(obj, "data");
        } catch (Exception e) {
        }
        return data;
    }

    /**
     * 可将对象转为Json字符串
     *
     * @param clazz
     * @return
     */
    public static String getJsonString(Object clazz) {
        Gson gson = new Gson();
        return gson.toJson(clazz);
    }


    /**
     * 将JSON数据拷贝到已有的对象
     *
     * @param object
     *            目标对象
     * @param jsonObject
     */
    public static void parseBeanFromJson(Object object, JSONObject jsonObject) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean isAccess = field.isAccessible();
            if (!isAccess) {
                field.setAccessible(true);
            }
            try {
                field.set(object, jsonObject.get(field.getName()));
            } catch (Exception e) {
//				e.printStackTrace();
            } finally {
                if (!isAccess) {
                    field.setAccessible(false);
                }
            }
        }
    };

    /**
     * 将JSON数据解析到已有的数据类型
     *
     * @param clazzTargetType
     *            需包含空的构造函数,#*内部类禁止使用*#
     * @param jsonObject
     * @return
     */
    public static <T> T parseBeanFromJson(Class<T> clazzTargetType, JSONObject jsonObject) {
        try {
            T t = clazzTargetType.newInstance();
            parseBeanFromJson(t, jsonObject);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
