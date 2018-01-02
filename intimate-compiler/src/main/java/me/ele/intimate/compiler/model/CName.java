package me.ele.intimate.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * Created by lizhaoxuan on 2017/12/19.
 */

public class CName {

    public String packageName;
    public String className;
    public String fullName;
    public TypeName typeName;
    public boolean isPrimitive = false;

    public CName(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        this.fullName = packageName + "." + className;
        typeName = ClassName.get(packageName, className);
    }

    public CName(String fullName) {
        initPackageAndClassName(fullName);
        typeName = ClassName.get(packageName, className);
    }

    public CName(TypeMirror typeMirror) {
        if (typeMirror.getKind().isPrimitive()) {
            this.isPrimitive = true;
            this.packageName = "";
            this.className = typeMirror.toString();
            this.fullName = className;
        } else {
            initPackageAndClassName(typeMirror.toString());
        }
        this.typeName = TypeName.get(typeMirror);
    }

    private void initPackageAndClassName(String name) {
        if (name.contains("<")) {
            fullName = name.substring(0, name.indexOf("<"));
        } else {
            fullName = name;
        }
        int lastIndex = fullName.lastIndexOf(".");
        if (lastIndex != -1) {
            packageName = fullName.substring(0, lastIndex);
            className = fullName.substring(lastIndex + 1, fullName.length());
        } else {
            packageName = fullName;
            className = fullName;
        }
    }

}
