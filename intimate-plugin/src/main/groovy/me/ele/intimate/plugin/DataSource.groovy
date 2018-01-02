package me.ele.intimate.plugin;

/**
 * Created by lizhaoxuan on 2017/12/27.
 */
public class DataSource {

    public def static intimateConfig = [:]
    public def static todoList = []
    public def static refFactoryShellName = ""
    public def static implMap = [:]

    public static void clear() {
        intimateConfig = [:]
        todoList = []
        refFactoryShellName = ""
        implMap = [:]
    }
}
