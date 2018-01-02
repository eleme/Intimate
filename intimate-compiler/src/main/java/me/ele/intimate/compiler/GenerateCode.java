package me.ele.intimate.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

import me.ele.intimate.compiler.model.RefFieldModel;
import me.ele.intimate.compiler.model.RefMethodModel;
import me.ele.intimate.compiler.model.RefTargetModel;

import static me.ele.intimate.compiler.TypeUtil.INTIMATE_PACKAGE;


/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class GenerateCode {

    RefTargetModel model;

    public GenerateCode(RefTargetModel model) {
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

        if (!model.isNeedForName()) {
            construction.addStatement("mData = ($T) mObject", model.getTargetName().typeName);
        }

        TypeSpec.Builder implClass = TypeSpec.classBuilder(model.getImplClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(model.getInterfaceName().typeName)
                .addMethod(construction.build())
                .addField(Object.class, "mObject")
                .addField(Class.class, "mClass");

        if (model.isNeedForName()) {
            implClass.addField(Object.class, "mData");
        } else {
            implClass.addField(model.getTargetName().typeName, "mData");
        }


        generateFiled(implClass, model.getFieldList());
        generateMethod(implClass, model.getMethodList());

        return JavaFile.builder(INTIMATE_PACKAGE, implClass.build()).build();
    }

    private void generateFiled(TypeSpec.Builder implClass, List<RefFieldModel> fieldModelList) {
        if (fieldModelList == null || fieldModelList.size() == 0) {
            return;
        }
        for (RefFieldModel fieldModel : fieldModelList) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(fieldModel.getMethodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            if (fieldModel.getParameterType() != null) {
                methodSpec.addParameter(fieldModel.getParameterType().typeName, "arg");
            }
            if (fieldModel.isSet()) {
                methodSpec.returns(fieldModel.getReturnType().typeName);
                methodSpec.addCode(TypeUtil.typeDefaultReturnCode(fieldModel.getReturnType()));
            } else {
                methodSpec.returns(fieldModel.getType().typeName);
                methodSpec.addCode(TypeUtil.typeDefaultReturnCode(fieldModel.getType()));
            }
            implClass.addMethod(methodSpec.build());
        }
    }

    private static void generateMethod(TypeSpec.Builder implClass, List<RefMethodModel> methodModels) {
        if (methodModels == null || methodModels.size() == 0) {
            return;
        }
        for (RefMethodModel methodModel : methodModels) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodModel.getName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodModel.getReturnType().typeName);

            if (methodModel.getParameterTypes() != null && methodModel.getParameterTypes().size() > 0) {
                for (int i = 0; i < methodModel.getParameterTypes().size(); i++) {
                    methodSpec.addParameter(methodModel.getParameterTypes().get(i).typeName, "arg" + i);
                }
            }

            if (!methodModel.isVoid()) {
                methodSpec.addCode(TypeUtil.typeDefaultReturnCode(methodModel.getReturnType()));
            }
            implClass.addMethod(methodSpec.build());
        }
    }


}
