package me.ele.intimate;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */

public class RefImplFactory {

    public static <T> T getRefImpl(Object object, Class clazz) {
        String name = clazz.getCanonicalName();
        return createRefImpl(object, name);
    }

    private static <T> T createRefImpl(Object object, String name) {
        //Compile time generate code
        return null;
    }
}
