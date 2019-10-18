package com.android.library.util;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
*@author chenjunwei
*@desc 带缓存的反射工具类
*@date 2019-09-25
*/
public final class ReflectUtil {

    /**
     * class缓存
     */
    public static Map<String, Class> clazzMap = new HashMap<String, Class>();
    /**
     * 构造器缓存
     */
    public static Map<String, Object> objMap = new HashMap<String, Object>();
    /**
     * 方法缓存
     */
    public static Map<String, Method> metMap = new HashMap<String, Method>();
    /**
     * 变量缓存
     */
    public static Map<String, Field> fieldMap = new HashMap<String, Field>();

    /**
     * 获取一个类
     *
     * @param name 类名
     * @return
     */
    public static Class<?> getClass(String name) {
        try {
            Class<?> clazz = clazzMap.get(name);
            if (clazz == null) {
                clazz = Class.forName(name);
                clazzMap.put(name, clazz);//缓存class对象
            }
            return clazz;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    /**
     * 获取一个无参构造函数的实例对象
     *
     * @param name 类名
     * @return
     */
    public static Object getObject(String name) {
        try {
            Object object = objMap.get(name + "_0");
            if (object == null) {
                Class<?> clazz = clazzMap.get(name);
                if (clazz == null) {
                    clazz = Class.forName(name);
                    clazzMap.put(name, clazz);//缓存class对象
                }
                object = clazz.newInstance();
                objMap.put(name + "_0", object);//缓存对象的实例
            }
            return object;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    /**
     * 获取一个参数为字符串类型的构造函数的实例对象
     *
     * @param name 类名
     * @param arg  构造参数
     * @return
     */
    public static Object getObject(String name, String arg) {
        try {
            Object object = objMap.get(name + "_String");
            if (object == null) {
                Class<?> clazz = clazzMap.get(name);
                if (clazz == null) {
                    clazz = Class.forName(name);
                    clazzMap.put(name, clazz);//缓存class对象
                }
                Constructor<?> constructor = clazz.getConstructor(String.class);
                object = constructor.newInstance(arg);
                objMap.put(name + "_String", object);//缓存对象的实例
            }
            return object;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    /**
     * 获取一个无参构造函数的Fragment实例
     *
     * @param name 类名
     * @return
     */
    public static Fragment getFragment(String name) {
        Fragment fragment = null;
        try {
            Class<?> clazz = clazzMap.get(name);
            if (clazz == null) {
                clazz = Class.forName(name);
                clazzMap.put(name, clazz);//缓存class对象
            }
            Object object = clazz.newInstance();
            fragment = (Fragment) object;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return fragment;
    }

    /**
     * 获取一个参数为字符串类型的构造函数的Fragment实例
     *
     * @param name 名称
     * @param arg  构造参数
     * @return
     */
    public static Fragment getFragment(String name, String arg) {
        Fragment fragment = null;
        try {
            Class<?> clazz = clazzMap.get(name);
            if (clazz == null) {
                clazz = Class.forName(name);
                clazzMap.put(name, clazz);//缓存class对象
            }
            Constructor<?> constructor = clazz.getConstructor(String.class);
            Object object = constructor.newInstance(arg);
            fragment = (Fragment) object;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return fragment;
    }

    /**
     * 获取一个参数为字符串类型的构造函数的Fragment实例
     *
     * @param name 名称
     * @param arg  构造参数
     * @return
     */
    public static Fragment getFragment(String name, Bundle arg) {
        Fragment fragment = null;
        try {
            Class<?> clazz = clazzMap.get(name);
            if (clazz == null) {
                clazz = Class.forName(name);
                clazzMap.put(name, clazz);//缓存class对象
            }
            Constructor<?> constructor = clazz.getConstructor(Bundle.class);
            Object object = constructor.newInstance(arg);
            fragment = (Fragment) object;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return fragment;
    }

    /**
     * 调用某个类的某个方法并且传入对应参数
     *
     * @param name       类名
     * @param methodName 方法名
     * @param types      类型
     * @param args       参数
     */
    public static Object invoke(String name, String methodName,
                                Class<?>[] types, Object... args) {
        int methodArgSize = 0, consArgSize = 0;
        if (types != null) {
            methodArgSize = types.length;
        }
        try {
            Method method = metMap.get(name + "_" + methodName + "_" + methodArgSize);//区分重载的方法
            Object object = objMap.get(name + "_" + consArgSize);
            if (method == null || object == null) {
                Class<?> clazz = clazzMap.get(name);
                if (clazz == null) {
                    clazz = Class.forName(name);
                    clazzMap.put(name, clazz);//缓存class对象
                }
                if (object == null) {
                    object = clazz.newInstance();
                    objMap.put(name + "_" + consArgSize, object);//缓存对象的实例
                }
                if (method == null) {
                    method = clazz.getMethod(methodName, types);
                    metMap.put(name + "_" + methodName + "_" + methodArgSize, method);//缓存Method对象
                }
            }
            return method.invoke(object, args);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }

    /**
     * @param name
     * @param constructorTypes
     * @param methodName
     * @param types
     * @param args
     * @return
     */
    public static Object invokeByConstructor(String name,
                                             Class<?>[] constructorTypes, Object[] constructorArgs,
                                             String methodName, Class<?>[] types, Object[] args) {
        int methodArgSize = 0, consArgSize = 0;
        if (types != null) {
            methodArgSize = types.length;
        }
        if (constructorTypes != null) {
            consArgSize = constructorTypes.length;
        }
        try {
            Method method = metMap.get(name + "_" + methodName + "_" + methodArgSize);//区分重载的方法
            Object object = objMap.get(name + "_" + consArgSize);
            if (method == null || object == null) {
                Class<?> clazz = clazzMap.get(name);
                if (clazz == null) {
                    clazz = Class.forName(name);
                    clazzMap.put(name, clazz);//缓存class对象
                }

                if (object == null) {
                    Constructor<?> constructor = clazz.getConstructor(constructorTypes);
                    object = constructor.newInstance(constructorArgs);
                    objMap.put(name + "_" + consArgSize, object);//缓存对象的实例
                }
                if (method == null) {
                    method = clazz.getMethod(methodName, types);
                    metMap.put(name + "_" + methodName + "_" + methodArgSize, method);//缓存Method对象
                }
            }

            return method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 静态方法反射
     *
     * @param name
     * @param methodName
     * @param types
     * @param args
     * @return
     */
    public static Object invokeByStaticMethod(String name,
                                              String methodName, Class<?>[] types, Object[] args) {
        int methodArgSize = 0;
        if (types != null) {
            methodArgSize = types.length;
        }
        try {
            Method method = metMap.get(name + "_" + methodName + "_" + methodArgSize);//区分重载的方法
            if (method == null) {
                Class<?> clazz = clazzMap.get(name);
                if (clazz == null) {
                    clazz = Class.forName(name);
                    clazzMap.put(name, clazz);//缓存class对象
                }
                method = clazz.getMethod(methodName, types);
                metMap.put(name + "_" + methodName + "_" + methodArgSize, method);//缓存Method对象
            }
            return method.invoke(null, args);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return null;
    }


    /**
     * 给静态变量赋值
     *
     * @param className
     * @param variableName
     * @param value
     */
    public static void set(String className, String variableName, String value) {
        try {
            Field field = fieldMap.get(className + "_" + variableName);
            if (field == null) {
                Class<?> clazz = clazzMap.get(className);
                if (clazz == null) {
                    clazz = Class.forName(className);
                    clazzMap.put(className, clazz);//缓存class对象
                }
                field = clazz.getDeclaredField(variableName);
                fieldMap.put(className + "_" + variableName, field);
            }
            field.set(null, value);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }

    /**
     * @param className
     * @param variableName
     * @return
     */
    public static String getString(String className, String variableName) {
        String s = "";
        try {
            Field field = fieldMap.get(className + "_" + variableName);
            if (field == null) {
                Class<?> clazz = clazzMap.get(className);
                if (clazz == null) {
                    clazz = Class.forName(className);
                    clazzMap.put(className, clazz);//缓存class对象
                }
                field = clazz.getDeclaredField(variableName);
                fieldMap.put(className + "_" + variableName, field);
            }
            s = field.get(null).toString();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return s;
    }

    /**
     * @param className
     * @param variableName
     * @return
     */
    public static int getInt(String className, String variableName) {
        int arg = -138;
        try {
            Field field = fieldMap.get(className + "_" + variableName);
            if (field == null) {
                Class<?> clazz = clazzMap.get(className);
                if (clazz == null) {
                    clazz = Class.forName(className);
                    clazzMap.put(className, clazz);//缓存class对象
                }
                field = clazz.getDeclaredField(variableName);
                fieldMap.put(className + "_" + variableName, field);
            }
            arg = field.getInt(null);
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return arg;
    }

    /**
     * 获取对象的私有变量
     */
    public static Object getField(Object instance, String name)
            throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Object object = null;
        if (instance != null && !TextUtils.isEmpty(name)) {
            Field field = instance.getClass().getDeclaredField(name);
            if (field != null) {
                field.setAccessible(true);
                object = field.get(instance);
            }
        }
        return object;
    }

    // ============================================================================================
    // 反射获取资源文件
    // ============================================================================================

    /**
     * 通过layout名字，获得layout的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout",
                paramContext.getPackageName());
    }

    /**
     * 通过String名字，获得String的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getStringId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "string",
                paramContext.getPackageName());
    }

    /**
     * 通过Drawable名字，获得Drawable的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,
                "drawable", paramContext.getPackageName());
    }

    /**
     * 通过Style名字，获得Style的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "style",
                paramContext.getPackageName());
    }

    /**
     * 通过Id名字，获得Id的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "id",
                paramContext.getPackageName());
    }

    /**
     * 通过Color名字，获得Color的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */

    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "color",
                paramContext.getPackageName());
    }

    /**
     * 通过Array名字，获得Array的id
     *
     * @param paramContext
     * @param paramString
     * @return
     */
    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "array",
                paramContext.getPackageName());
    }
}
