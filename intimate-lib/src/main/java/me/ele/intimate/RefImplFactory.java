package me.ele.intimate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {
    private static Map<Class, Map<String, Field>> fieldMap;
    private static Map<Class, Map<String, Method>> methodMap;

    public static <T> T getRefImpl(Object object, Class clazz) {
        //Compile time generate code
        return null;
    }

    protected static Field getField(Class refName, String name) {
        if (fieldMap == null) {
            return null;
        }
        Map<String, Field> fields = fieldMap.get(refName);
        if (fields == null) {
            return null;
        }
        return fields.get(name);
    }

    protected static Method getMethod(Class refName, String name) {
        if (methodMap == null) {
            return null;
        }
        Map<String, Method> methods = methodMap.get(refName);
        if (methods == null) {
            return null;
        }
        return methods.get(name);
    }

    protected static void putField(Class refName, String name, Field field) {
        if (fieldMap == null) {
            fieldMap = new HashMap<>();
        }
        Map<String, Field> fields = fieldMap.get(refName);
        if (fields == null) {
            fields = new HashMap<>();
            fieldMap.put(refName, fields);
        }
        fields.put(name, field);
    }

    protected static void putMethod(Class refName, String name, Method method) {
        if (methodMap == null) {
            methodMap = new HashMap<>();
        }
        Map<String, Method> methods = methodMap.get(refName);
        if (methods == null) {
            methods = new HashMap<>();
            methodMap.put(refName, methods);
        }
        methods.put(name, method);
    }

    public static void clearAccess(Class refName) {
        if (fieldMap != null) {
            Map<String, Field> fields = fieldMap.get(refName);
            if (fields != null) {
                fields.clear();
            }
        }
        if (methodMap != null) {
            Map<String, Method> methods = methodMap.get(refName);
            if (methods != null) {
                methods.clear();
            }
        }
    }

    public static void clearAllAccess() {
        if (fieldMap != null) {
            fieldMap.clear();
        }
        if (methodMap != null) {
            methodMap.clear();
        }
    }
}
