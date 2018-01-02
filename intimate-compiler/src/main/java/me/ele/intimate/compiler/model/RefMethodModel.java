package me.ele.intimate.compiler.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.type.TypeMirror;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefMethodModel {

    private String name;
    private CName returnType;
    private List<CName> thrownTypes;
    private List<CName> parameterTypes;
    private String methodContentCode;

    public RefMethodModel(String name, TypeMirror returnType, List<CName> parameterTypes, List<? extends TypeMirror> thrownTypes) {
        this.name = name;
        this.returnType = new CName(returnType);
        this.parameterTypes = parameterTypes;
        this.methodContentCode = generateMethodContentCode();
        this.thrownTypes = new ArrayList<>();
        for (TypeMirror throwType : thrownTypes) {
            this.thrownTypes.add(new CName(throwType));
        }
    }

    public List<CName> getThrownTypes() {
        return thrownTypes;
    }

    public String getName() {
        return name;
    }

    public CName getReturnType() {
        return returnType;
    }

    public List<CName> getParameterTypes() {
        return parameterTypes;
    }

    public boolean isVoid() {
        return returnType.className.equals("void");
    }

    private String generateMethodContentCode() {
        StringBuilder builder = new StringBuilder();
        if (this.returnType.fullName.equals("void")) {
            builder.append("mData.").append(name).append("(");
        } else {
            builder.append("return mData.").append(name).append("(");
        }
        if (parameterTypes != null && parameterTypes.size() > 0) {
            for (int i = 1; i <= parameterTypes.size(); i++) {
                builder.append("$").append(i);
                if (i != parameterTypes.size()) {
                    builder.append(",");
                }
            }
        }
        builder.append(");");
        return builder.toString();
    }

}
