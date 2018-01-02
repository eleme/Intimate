package me.ele.example.ref;

import com.google.gson.Gson;

import me.ele.example.Constant;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;

/**
 * Created by lizhaoxuan on 2017/12/26.
 */
@RefTarget(clazz = Gson.class, optimizationRef = Constant.OPTIMIZATION_REF)
public interface RefGson {

    @GetField("DEFAULT_ESCAPE_HTML")
    boolean getDefaultLenient();

    @GetField("DEFAULT_PRETTY_PRINT")
    boolean getDefaultPrettyPrint();

}
