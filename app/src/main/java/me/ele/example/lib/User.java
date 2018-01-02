package me.ele.example.lib;

/**
 * Created by lizhaoxuan on 2017/12/18.
 */

public class User {

    private String name;
    protected String sex;
    private int age;

    private String className;

    public User(String name, String sex, int age, String className) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.className = className;
    }

    private void setAge(int a, int b) {
        this.age = a + b;
    }

    String getAgeStr() {
        return String.valueOf(age);
    }

    protected String getSexStr() {
        return sex;
    }

    private String getClassName() {
        return className;
    }

}
