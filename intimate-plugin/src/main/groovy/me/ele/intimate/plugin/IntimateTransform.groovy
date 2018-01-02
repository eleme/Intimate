package me.ele.intimate.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.json.JsonSlurper
import javassist.ClassPool
import javassist.CtClass
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

/**
 * Created by lizhaoxuan on 2017/12/20.
 */

class IntimateTransform extends Transform {

    static ClassPool pool
    static List<CtClass> jarClassList = new ArrayList<>()
    Project project

    IntimateTransform(Project project) {
        this.project = project
        pool = new ClassPool()
        pool.appendSystemPath()
    }

    @Override
    String getName() {
        return "Intimate"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()


        readIntimateConfig(inputs)
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        inputs.each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                pool.appendClassPath(jarInput.file.absolutePath)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                pool.appendClassPath(directoryInput.file.absolutePath)
            }
        }

        inputs.each { TransformInput input ->
            jarClassList = new ArrayList<>()
            input.jarInputs.each { JarInput jarInput ->
                processJar(jarInput, outputProvider)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                processClassFile(directoryInput, outputProvider)
            }
        }

        for (CtClass ctClass : jarClassList) {
            if (ctClass != null) {
                ctClass.detach()
            }
        }
        jarClassList.clear()
    }

    private static void readIntimateConfig(Collection<TransformInput> inputs) {
        DataSource.clear()
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File configFile = new File(directoryInput.file.absolutePath + "/me/ele/intimate/intimate.json")
                if (configFile.exists()) {
                    def content = new StringBuilder()
                    configFile.eachLine("UTF-8") {
                        content.append(it)
                    }
                    def data = new JsonSlurper().parseText(content.toString())

                    if (data.refFactoryShellName != null && data.refFactoryShellName != "") {
                        DataSource.refFactoryShellName = data.refFactoryShellName
                        DataSource.todoList.add(data.refFactoryShellName)
                    }

                    data.targetModelMap.each { key, value ->
                        DataSource.implMap.put(value.interfaceName.fullName, value.implFullName)
                        if (value.optimizationRef) {
                            DataSource.intimateConfig.put(value.targetName.fullName, value)
                            if (!DataSource.todoList.contains(value.implFullName)) {
                                DataSource.todoList.add(value.implFullName)
                            }
                            if (!DataSource.todoList.contains(value.targetName.fullName)) {
                                DataSource.todoList.add(value.targetName.fullName)
                            }
                        }
                    }
                    println("DataSource.todoList:" + DataSource.todoList)
                }
            }
        }
    }

    private
    static void processClassFile(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        int packageIndex = directoryInput.file.absolutePath.toString().length() + 1
        ClassInject.injectDir(directoryInput.file.absolutePath, packageIndex)

        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes, directoryInput.scopes,
                Format.DIRECTORY)

        FileUtils.copyDirectory(directoryInput.file, dest)
    }

    private static void processJar(JarInput jarInput, TransformOutputProvider outputProvider) {
        String jarPath = jarInput.file.absolutePath
        File jar = JarInject.injectJar(jarPath)

        // 重命名输出文件（同目录copyFile会冲突）
        def jarName = jarInput.name
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        def dest = outputProvider.getContentLocation(jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        if (jar == null) {
            FileUtils.copyFile(jarInput.file, dest)
        } else {
            FileUtils.copyFile(jar, dest)
        }
    }
}
