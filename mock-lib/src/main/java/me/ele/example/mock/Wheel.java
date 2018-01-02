package me.ele.example.mock;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class Wheel {

    private String name;
    private int size;

    public Wheel(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
