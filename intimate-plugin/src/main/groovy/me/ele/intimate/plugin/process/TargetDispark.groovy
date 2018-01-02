package me.ele.intimate.plugin.process

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.bytecode.AccessFlag
import me.ele.intimate.plugin.DataSource
import me.ele.intimate.plugin.ThrowExecutionError

/**
 * Created by lizhaoxuan on 2017/12/28.
 */

class TargetDispark {

    static void processClass(CtClass c) {
        c.setModifiers(AccessFlag.setPublic(c.getModifiers()))
        def intimateFieldList = []
        def intimateObjectFieldList = []
        def intimateMethodList = []

        DataSource.intimateConfig.each { key, value ->
            if (key == c.name || key == c.name.replace("\$", ".")) {
                for (def fieldConfig : value.fieldList) {
                    if (fieldConfig.type.fullName == "java.lang.Object") {
                        intimateObjectFieldList.add(fieldConfig.name)
                    } else {
                        intimateFieldList.add(GenerateUtils.generateFieldDes(fieldConfig))
                    }
                }
                for (def methodConfig : value.methodList) {
                    intimateMethodList.add(GenerateUtils.generateMethodDes(methodConfig))
                }
            }
        }
        processTargetField(c, intimateFieldList, intimateObjectFieldList)
        processTargetMethod(c, intimateMethodList)
    }

    private static void processTargetField(CtClass c, intimateFieldList, intimateObjectFieldList) {
        def tempIntimateField = []
        def tempIntimateObjectFieldList = []
        for (CtField field : c.getDeclaredFields()) {
            String fieldStr = GenerateUtils.generateFieldDes(field)
            if (intimateFieldList.contains(fieldStr)) {
                field.setModifiers(AccessFlag.setPublic(field.getModifiers()))
                tempIntimateField.add(fieldStr)
            } else if (intimateObjectFieldList.contains(field.name)) {
                field.setModifiers(AccessFlag.setPublic(field.getModifiers()))
                tempIntimateObjectFieldList.add(field.name)
            }
        }

        intimateFieldList.removeAll(tempIntimateField)
        intimateObjectFieldList.removeAll(tempIntimateObjectFieldList)
        if (intimateFieldList.size() != 0) {
            ThrowExecutionError.throwError(c.name + " not found field:" + GenerateUtils.generateNotFoundFieldError(intimateFieldList))
        }
        if (intimateObjectFieldList.size() != 0) {
            ThrowExecutionError.throwError(c.name + " not found field:" + GenerateUtils.generateNotFoundFieldError(intimateObjectFieldList))
        }
    }

    private static void processTargetMethod(CtClass c, intimateMethodList) {
        def tempIntimateMethod = []

        for (CtMethod method : c.getDeclaredMethods()) {
            String methodInfo = GenerateUtils.generateMethodDes(method)
            if (intimateMethodList.contains(methodInfo)) {
                method.setModifiers(AccessFlag.PUBLIC)
                tempIntimateMethod.add(methodInfo)
            }
        }
        intimateMethodList.removeAll(tempIntimateMethod)
        if (intimateMethodList.size() != 0) {
            ThrowExecutionError.throwError(c.name + " not found method:  " + GenerateUtils.generateNotFoundMethodError(intimateMethodList))
        }
    }

}
