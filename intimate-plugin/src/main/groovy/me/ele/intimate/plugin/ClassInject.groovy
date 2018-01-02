package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import me.ele.intimate.plugin.process.GenerateUtils
import me.ele.intimate.plugin.process.TargetDispark

/**
 * Created by lizhaoxuan on 2017/12/25.
 */

class ClassInject {

    static void injectDir(String path, packageIndex) {
        IntimateTransform.pool.appendClassPath(path)
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {
                    int end = filePath.length() - 6 // .class = 6
                    String className = filePath.substring(packageIndex, end)
                            .replace('/', '.').replace('/', '.')
                    // 判断是否是需要处理的类
                    if (DataSource.todoList.contains(className)) {
                        processClass(className, path)
                    }
                }
            }
        }
    }


    private static void processClass(String className, String path) {
        CtClass c = IntimateTransform.pool.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }

        if (className.contains("\$\$Intimate")) {
            println("processImpl:" + className)
            processImpl(c)
        } else {
            println("processTarget:" + className)
            TargetDispark.processClass(c)
        }

        c.writeFile(path)
        c.detach()
    }

    private static void processImpl(CtClass c) {
        def contentMap = [:]
        def methodList = []

        DataSource.intimateConfig.each { key, value ->
            if (value.implFullName == c.name) {
                if (value.needForName) {
                    processInnerClass(c, value)
                }
                for (def filedConfig : value.fieldList) {
                    String des = GenerateUtils.generateImplFieldDes(filedConfig)
                    methodList.add(des)
                    contentMap.put(des, filedConfig.methodContentCode)
                }

                for (def methodConfig : value.methodList) {
                    String des = GenerateUtils.generateImplMethodDes(methodConfig)
                    methodList.add(des)
                    contentMap.put(des, methodConfig.methodContentCode)
                }
            }
        }
        for (CtMethod method : c.getDeclaredMethods()) {
            String des = GenerateUtils.generateImplMethodDes(method)
            if (methodList.contains(des)) {
                String code = contentMap.get(des)
                method.setBody(code)
            }
        }
    }

    private static void processInnerClass(CtClass c, value) {
        CtField field = c.getDeclaredField("mData")
        CtClass targetClass = IntimateTransform.pool.getCtClass(value.targetName.fullName)
        field.setType(targetClass)

        for (CtConstructor constructors : c.getConstructors()) {
            constructors.insertAfter("mData = (" + value.targetName.fullName + ") mObject;")
        }
    }

}
