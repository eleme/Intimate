package me.ele.example.ref;

import me.ele.example.lib.User;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */
@RefTarget(clazz = User.class, optimizationRef = true)
public interface RefUser {

    @Method
    void setAge(int a, int b);

    @GetField("name")
    String getName();

    @SetField("name")
    void setName(String value);

    @SetField("age")
    void setName(int value);

    @GetField("sex")
    String getSex();

    @GetField("age")
    int getAge();

    @SetField("sex")
    void setSexRef(String sex);

    @Method
    String getAgeStr();

    @Method
    String getSexStr();

    @Method
    String getClassName();
}
