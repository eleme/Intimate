package me.ele.intimate.plugin.util;

/**
 * Created by lizhaoxuan on 2018/1/3.
 */

public class Log {
    public static Debug = true

    public static void d(Object msg) {
        if (Debug) {
            println("Intimate: " + msg.toString())
        }
    }

}
