package me.ele.intimate.plugin

import javassist.CtClass
import javassist.CtMethod
import me.ele.intimate.plugin.process.GenerateUtils
import me.ele.intimate.plugin.process.TargetDispark
import me.ele.intimate.plugin.util.JarUtils
import me.ele.intimate.plugin.util.Log
import org.apache.commons.io.FileUtils

class JarInject {

    /**
     * 这里需要将jar包先解压，注入代码后再重新生成jar包
     * @path jar包的绝对路径
     */
    static void injectJar(path) throws Exception {
        if (path.endsWith(".jar")) {
            File jarFile = new File(path)
            String jarZipDir = jarFile.getParent() + "/" + jarFile.getName().replace('.jar', '')
            File unJar = new File(jarZipDir)
            List classNameList = JarUtils.unJar(jarFile, unJar)

            try {
                boolean haveTarget = traverseClassList(classNameList, jarZipDir)
                if (haveTarget) {
                    jarFile.delete()
                    JarUtils.jar(jarFile, unJar)
                }
            } catch (Exception e) {
                throw e
            } finally {
                FileUtils.deleteDirectory(new File(jarZipDir))
            }
        }
    }

    private static boolean traverseClassList(List classNameList, String jarZipDir) {
        boolean haveTarget = false
        boolean hasAppend = false
        for (String className : classNameList) {
            if (className.endsWith(".class")
                    && !className.contains('R$')
                    && !className.contains('R.class')
                    && !className.contains("BuildConfig.class")) {
                className = className.substring(0, className.length() - 6)
                for (String todo : DataSource.todoList) {
                    if (className == todo || className.replace("\$", ".") == todo) {
                        if (!hasAppend) {
                            hasAppend = true
                            IntimateTransform.pool.appendClassPath(jarZipDir)
                        }
                        haveTarget = true
                        processClass(className, jarZipDir)
                    }
                }
            }
        }
        return haveTarget
    }

    private static void processClass(String className, String path) {
        CtClass c = IntimateTransform.pool.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }

        if (DataSource.refFactoryShellName == className) {
            Log.d_("processFactory:" + c.name + "ing...")
            processFactory(c)
        } else {
            Log.d_("processTarget:" + c.name + "ing...")
            TargetDispark.processClass(c)
        }

        c.writeFile(path)
        IntimateTransform.jarClassList.add(c)
    }

    private static void processFactory(CtClass c) {
        if (DataSource.implMap.size() == 0) {
            return
        }
        CtMethod ctMethods = c.getDeclaredMethod("getRefImpl")
        String code = GenerateUtils.generateCreateRefImplCode(DataSource.implMap)
        ctMethods.setBody(code)
        Log._d(" done")
    }
}