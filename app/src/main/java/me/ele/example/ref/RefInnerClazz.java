package me.ele.example.ref;

import me.ele.example.Constant;
import me.ele.example.mock.InnerClazz;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */
@RefTarget(clazz = InnerClazz.class, optimizationRef = Constant.OPTIMIZATION_REF)
public interface RefInnerClazz {

    @GetField("privateInnerClass")
    Object getPrivateInnerClass();

    @GetField("staticInnerClass")
    Object getStaticInnerClass();

    @GetField("publicStaticInnerClass")
    Object getPublicStaticInnerClass();

}
