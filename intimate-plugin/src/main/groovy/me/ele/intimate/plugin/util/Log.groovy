package me.ele.intimate.plugin.util

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

/**
 * Created by lizhaoxuan on 2018/1/3.
 */

public class Log {
    public static Debug = true
    public static Logger logger = Logging.getLogger("intimate")

    public static void d(Object msg) {
        if (Debug) {
            println("Intimate: " + msg.toString())
        }
    }

    public static void d_(Object msg) {
        if (Debug) {
            print("Intimate: " + msg.toString())
        }
    }

    public static void _d(Object msg) {
        if (Debug) {
            println(msg.toString())
        }
    }

    public static void error(Object msg) {
        logger.error("Intimate: " + msg.toString())
    }

}
