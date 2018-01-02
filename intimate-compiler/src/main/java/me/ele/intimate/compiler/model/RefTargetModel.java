package me.ele.intimate.compiler.model;

import java.util.ArrayList;
import java.util.List;

import static me.ele.intimate.compiler.TypeUtil.INTIMATE_PACKAGE;


/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class RefTargetModel {

    private CName interfaceName;
    private CName targetName;
    private String implFullName;
    private String implClassName;
    private boolean needForName;
    private boolean optimizationRef;
    private List<RefFieldModel> fieldList = new ArrayList<>();
    private List<RefMethodModel> methodList = new ArrayList<>();

    public RefTargetModel(String interfaceName, String className, boolean needForName, boolean optimizationRef) {
        this.interfaceName = new CName(interfaceName);
        this.targetName = new CName(className);
        this.needForName = needForName;
        this.optimizationRef = optimizationRef;
        this.implClassName = this.interfaceName.className + "$$Intimate";
        this.implFullName = INTIMATE_PACKAGE + "." + this.implClassName;
    }

    public void addField(RefFieldModel field) {
        fieldList.add(field);
    }

    public void addMethod(RefMethodModel method) {
        methodList.add(method);
    }

    public CName getInterfaceName() {
        return interfaceName;
    }

    public CName getTargetName() {
        return targetName;
    }

    public boolean isNeedForName() {
        return needForName;
    }

    public boolean isOptimizationRef() {
        return optimizationRef;
    }

    public List<RefFieldModel> getFieldList() {
        return fieldList;
    }

    public List<RefMethodModel> getMethodList() {
        return methodList;
    }

    public String getImplClassName() {
        return implClassName;
    }

    public String getImplFullName() {
        return implFullName;
    }
}
