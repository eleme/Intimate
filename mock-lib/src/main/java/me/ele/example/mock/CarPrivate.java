package me.ele.example.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class CarPrivate {

    private String name;
    private int level;
    private Brand brand;
    private List<Wheel> wheels;

    public CarPrivate() {
        name = "my Private car";
        level = 7;
        brand = new Brand("biubiuCar", "made in china for 1986");
        wheels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wheels.add(new Wheel("GuLu", 10));
        }
    }

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    private Brand getBrand() {
        return brand;
    }

    private void setBrand(Brand brand) {
        this.brand = brand;
    }

    private List<Wheel> getWheels() {
        return wheels;
    }

    private void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
    }
}
