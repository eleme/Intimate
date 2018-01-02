package me.ele.intimate.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import me.ele.intimate.compiler.model.CName;
import me.ele.intimate.compiler.model.RefFieldModel;
import me.ele.intimate.compiler.model.RefMethodModel;
import me.ele.intimate.compiler.model.RefTargetModel;

import static me.ele.intimate.compiler.TypeUtil.INTIMATE_PACKAGE;


/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class GenerateSystemCode {

    RefTargetModel model;

    public GenerateSystemCode(RefTargetModel model) {
        this.model = model;
    }

    public JavaFile generate() {
        MethodSpec.Builder construction = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addException(ClassNotFoundException.class)
                .addParameter(Object.class, "object");
        if (model.getTargetName().fullName.contains("$") || model.isNeedForName()) {
            construction.addStatement("this.mObject = object")
                    .addStatement("this.mClass = Class.forName($S)", model.getTargetName().fullName);
        } else {
            construction.addStatement("this.mObject = object")
                    .addStatement("this.mClass = $N.class", model.getTargetName().fullName);
        }

        TypeSpec.Builder implClass = TypeSpec.classBuilder(model.getImplClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(model.getInterfaceName().typeName)
                .addMethod(construction.build())
                .addField(Object.class, "mObject")
                .addField(Class.class, "mClass");

        generateFiled(implClass, model.getFieldList());
        generateMethod(implClass, model.getMethodList());

        return JavaFile.builder(INTIMATE_PACKAGE, implClass.build()).build();
    }

    private void generateFiled(TypeSpec.Builder implClass, List<RefFieldModel> fieldModelList) {
        Set<String> fieldList = new HashSet<>();
        for (RefFieldModel fieldModel : fieldModelList) {
            if (!fieldList.contains(fieldModel.getName())) {
                fieldList.add(fieldModel.getName());
                implClass.addField(Field.class, fieldModel.getName());
            }

            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(fieldModel.getMethodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            for (CName exception : fieldModel.getThrownTypes()) {
                methodSpec.addException(exception.typeName);
            }
            if (fieldModel.getParameterType() != null) {
                methodSpec.addParameter(fieldModel.getParameterType().typeName, "arg");
            }
            if (fieldModel.isSet()) {
                methodSpec.returns(fieldModel.getReturnType().typeName);
                methodSpec.beginControlFlow("try")
                        .beginControlFlow("if($N == null)", fieldModel.getName())
                        .addStatement("$N = mClass.getDeclaredField($S)", fieldModel.getName(), fieldModel.getName())
                        .addStatement("$N.setAccessible(true)", fieldModel.getName())
                        .endControlFlow()
                        .addStatement("$N.set(mObject, arg)", fieldModel.getName())
                        .endControlFlow()
                        .beginControlFlow("catch (Exception e)");
                for (CName exception : fieldModel.getThrownTypes()) {
                    methodSpec.addStatement("if(e instanceof $T) throw ($T)e", exception.typeName, exception.typeName);
                }
                methodSpec.addStatement("e.printStackTrace()");
                methodSpec.endControlFlow();
            } else {
                methodSpec.returns(fieldModel.getType().typeName);
                methodSpec.beginControlFlow("try")
                        .beginControlFlow("if($N == null)", fieldModel.getName())
                        .addStatement("$N = mClass.getDeclaredField($S)", fieldModel.getName(), fieldModel.getName())
                        .addStatement("$N.setAccessible(true)", fieldModel.getName())                        .endControlFlow()
                        .addStatement("return($T)$N.get(mObject)", fieldModel.getType().typeName, fieldModel.getName())
                        .endControlFlow()
                        .beginControlFlow("catch (Exception e)");
                for (CName exception : fieldModel.getThrownTypes()) {
                    methodSpec.addStatement("if(e instanceof $T) throw ($T)e", exception.typeName, exception.typeName);
                }
                methodSpec.addStatement("e.printStackTrace()");
                methodSpec.endControlFlow();
            }
            methodSpec.addCode(TypeUtil.typeDefaultReturnCode(fieldModel.getReturnType()));
            implClass.addMethod(methodSpec.build());
        }
    }

    private static void generateMethod(TypeSpec.Builder implClass, List<RefMethodModel> methodModels) {
        if (methodModels == null || methodModels.size() == 0) {
            return;
        }
        Set<String> fieldList = new HashSet<>();
        for (RefMethodModel methodModel : methodModels) {
            if (!fieldList.contains(methodModel.getName())) {
                implClass.addField(Method.class, methodModel.getName(), Modifier.STATIC);
                fieldList.add(methodModel.getName());
            }

            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodModel.getName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodModel.getReturnType().typeName);

            StringBuilder getMethodCode = new StringBuilder("$N =  mClass.getDeclaredMethod($S");
            StringBuilder invokeCode = new StringBuilder("$N.invoke(mObject");
            if (methodModel.getParameterTypes() != null && methodModel.getParameterTypes().size() > 0) {
                for (int i = 0; i < methodModel.getParameterTypes().size(); i++) {
                    methodSpec.addParameter(methodModel.getParameterTypes().get(i).typeName, "arg" + i);
                    getMethodCode.append(",").append(methodModel.getParameterTypes().get(i).fullName).append(".class");
                    invokeCode.append(",").append("arg").append(i);
                }
            }
            getMethodCode.append(")");
            invokeCode.append(")");

            for (CName exception : methodModel.getThrownTypes()) {
                methodSpec.addException(exception.typeName);
            }
            methodSpec.beginControlFlow("try")
                    .beginControlFlow("if($N == null)", methodModel.getName())
                    .addStatement(getMethodCode.toString(), methodModel.getName(), methodModel.getName())
                    .addStatement("$N.setAccessible(true)", methodModel.getName())
                    .endControlFlow();

            if (methodModel.isVoid()) {
                methodSpec.addStatement(invokeCode.toString(), methodModel.getName());
            } else {
                methodSpec.addStatement("return ($T)$N.invoke(mObject)", methodModel.getReturnType().typeName, methodModel.getName());
            }
            methodSpec.endControlFlow()
                    .beginControlFlow("catch (Exception e)");
            for (CName exception : methodModel.getThrownTypes()) {
                methodSpec.addStatement("if(e instanceof $T) throw ($T)e", exception.typeName, exception.typeName);
            }
            methodSpec.addStatement("e.printStackTrace()");
            methodSpec.endControlFlow();
            if (!methodModel.isVoid()) {
                methodSpec.addCode(TypeUtil.typeDefaultReturnCode(methodModel.getReturnType()));
            }
            implClass.addMethod(methodSpec.build());
        }
    }
}
