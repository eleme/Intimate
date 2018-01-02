package me.ele.example.ref;

import java.util.List;

import me.ele.example.Constant;
import me.ele.example.mock.Brand;
import me.ele.example.mock.CarPrivate;
import me.ele.example.mock.Wheel;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.Method;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
@RefTarget(clazz = CarPrivate.class, optimizationRef = Constant.OPTIMIZATION_REF)
public interface RefCarPrivate {

    @GetField("name")
    String getNameField();

    @SetField("name")
    void setNameField(String name);

    @GetField("level")
    int getLevelField();

    //set 方法返回值随意,反正不生效
    @SetField("level")
    String setLevelField(int level);

    @GetField("brand")
    Brand getBrandField();

    @SetField("brand")
    void setBrandField(Brand brand);

    @GetField("wheels")
    List<Wheel> getWhellListField();

    @SetField("wheels")
    void setWhellListField(List<Wheel> wheels);

    @Method
    String getName();

    @Method
    void setName(String name);

    @Method
    int getLevel();

    @Method
    void setLevel(int level);

    @Method
    Brand getBrand();

    @Method
    void setBrand(Brand brand);

    @Method
    List<Wheel> getWheels();

    @Method
    void setWheels(List<Wheel> wheels);


}
