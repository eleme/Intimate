package me.ele.intimate.plugin.process

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod;

/**
 * Created by lizhaoxuan on 2017/12/28.
 */

class GenerateUtils {

    static String generateCreateRefImplCode(implMap) {
        StringBuilder builder = new StringBuilder()
        implMap.each { key, value ->
            builder.append("if(\$2.equals(\"").append(key).append("\")){ ")
                    .append("return new ").append(value).append("(\$1);} \n")
        }
        return builder.toString()
    }

    static String generateImplFieldDes(filedConfig) {
        StringBuilder des = new StringBuilder(filedConfig.methodName)
        if (filedConfig.parameterType != null) {
            des.append("-").append(filedConfig.parameterType.fullName)
        }
        return des.toString()
    }

    static String generateImplMethodDes(methodConfig) {
        StringBuilder des = new StringBuilder(methodConfig.name)
        for (def param : methodConfig.parameterTypes) {
            des.append("-").append(param.fullName)
        }
        return des.toString()
    }


    static String generateImplMethodDes(CtMethod method) {
        StringBuilder des = new StringBuilder(method.name)
        for (CtClass param : method.getParameterTypes()) {
            des.append("-").append(param.name)
        }
        return des.toString()
    }

    static String generateFieldDes(fieldConfig) {
        return "[" + fieldConfig.type.fullName + " : " + fieldConfig.name + "]"
    }

    static String generateMethodDes(methodConfig) {
        StringBuilder me = new StringBuilder(methodConfig.name).append("(")
        for (def parameterTypes : methodConfig.parameterTypes) {
            me.append(parameterTypes.fullName).append(",")
        }
        if (me.lastIndexOf(",") == me.length() - 1) {
            me.deleteCharAt(me.length() - 1)
        }
        me.append(")")
        return me.toString()
    }

    static String generateFieldDes(CtField field) {
        return "[" + field.getType().name + " : " + field.name + "]"
    }

    static String generateMethodDes(CtMethod method) {
        StringBuilder me = new StringBuilder(method.name).append("(")
        for (CtClass parameterType : method.getParameterTypes()) {
            me.append(parameterType.name).append(",")
        }
        if (me.lastIndexOf(",") == me.length() - 1) {
            me.deleteCharAt(me.length() - 1)
        }
        me.append(")")
        return me.toString()

    }

    static String generateNotFoundFieldError(def intimateFieldList) {
        StringBuilder msg = new StringBuilder()
        for (String str : intimateFieldList) {
            msg.append(str).append(" \n")
        }
        return msg.toString()
    }

    static String generateNotFoundMethodError(def intimateMethod) {
        StringBuilder msg = new StringBuilder()
        for (String str : intimateMethod) {
            msg.append(str).append(" \n")
        }
        return msg.toString()
    }

    static String generateNotFoundClassError(def todoList) {
        StringBuilder msg = new StringBuilder()
        for (String str : todoList) {
            msg.append(str).append("  ")
        }
        return msg.toString()
    }
}
