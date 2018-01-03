package me.ele.intimate.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.lang.model.element.Modifier;

import me.ele.intimate.compiler.model.CName;
import me.ele.intimate.compiler.model.RefFieldModel;
import me.ele.intimate.compiler.model.RefMethodModel;
import me.ele.intimate.compiler.model.RefTargetModel;

import static me.ele.intimate.compiler.TypeUtil.INTIMATE_PACKAGE;
import static me.ele.intimate.compiler.TypeUtil.REF_IMPL_FACTORY;


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
        for (RefFieldModel fieldModel : fieldModelList) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(fieldModel.getMethodName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            for (CName exception : fieldModel.getThrownTypes()) {
                methodSpec.addException(exception.typeName);
            }
            if (fieldModel.getParameterType() != null) {
                methodSpec.addParameter(fieldModel.getParameterType().typeName, "arg");
            }
            methodSpec.returns(fieldModel.getReturnType().typeName);
            methodSpec.beginControlFlow("try")
                    .addStatement("$T field = $T.getField($T.class,$S)", Field.class, REF_IMPL_FACTORY, model.getInterfaceName().typeName, fieldModel.getName())
                    .beginControlFlow("if(field == null)")
                    .addStatement("field = mClass.getDeclaredField($S)", fieldModel.getName())
                    .addStatement("field.setAccessible(true)")
                    .addStatement("$T.putField($T.class,$S,field)", REF_IMPL_FACTORY, model.getInterfaceName().typeName, fieldModel.getName())
                    .endControlFlow();
            if (fieldModel.isSet()) {
                methodSpec.addStatement("field.set(mObject, arg)");
            } else {
                methodSpec.addStatement("return($T)field.get(mObject)", fieldModel.getType().typeName);
            }
            methodSpec.endControlFlow()
                    .beginControlFlow("catch (Exception e)");
            for (CName exception : fieldModel.getThrownTypes()) {
                methodSpec.addStatement("if(e instanceof $T) throw ($T)e", exception.typeName, exception.typeName);
            }
            methodSpec.addStatement("e.printStackTrace()");
            methodSpec.endControlFlow();
            methodSpec.addCode(TypeUtil.typeDefaultReturnCode(fieldModel.getReturnType()));
            implClass.addMethod(methodSpec.build());
        }
    }

    private void generateMethod(TypeSpec.Builder implClass, List<RefMethodModel> methodModels) {
        if (methodModels == null || methodModels.size() == 0) {
            return;
        }
        for (RefMethodModel methodModel : methodModels) {
            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(methodModel.getName())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodModel.getReturnType().typeName);

            StringBuilder getMethodCode = new StringBuilder("method =  mClass.getDeclaredMethod($S");
            StringBuilder invokeCode = new StringBuilder("method.invoke(mObject");
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
                    .addStatement("$T method = $T.getMethod($T.class,$S)", Method.class, REF_IMPL_FACTORY, model.getInterfaceName().typeName, methodModel.getName())
                    .beginControlFlow("if(method == null)")
                    .addStatement(getMethodCode.toString(), methodModel.getName())
                    .addStatement("method.setAccessible(true)")
                    .addStatement("$T.putMethod($T.class,$S,method)", REF_IMPL_FACTORY, model.getInterfaceName().typeName, methodModel.getName())
                    .endControlFlow();

            if (methodModel.isVoid()) {
                methodSpec.addStatement(invokeCode.toString());
            } else {
                methodSpec.addStatement("return ($T)" + invokeCode.toString(), methodModel.getReturnType().typeName);
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
