package me.ele.example.mock;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class InnerClazz {

    private PrivateInnerClass privateInnerClass = new PrivateInnerClass();
    private StaticInnerClass staticInnerClass = new StaticInnerClass();
    private PublicStaticInnerClass publicStaticInnerClass = new PublicStaticInnerClass();

    public class PrivateInnerClass {
        String name = "defaultInnerClass";

        String getName() {
            return name;
        }
    }

    private static class StaticInnerClass {
        String name = "staticInnerClass";

        String getName() {
            return name;
        }
    }

    public static class PublicStaticInnerClass {
        String name = "publicStaticInnerClass";
    }

}
