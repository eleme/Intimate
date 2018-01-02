package me.ele.example.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class CarProtected {

    protected String name;
    protected int level;
    protected Brand brand;
    protected List<Wheel> wheels;

    public CarProtected() {
        name = "my Protected car";
        level = 7;
        brand = new Brand("biubiuCar", "made in china for 1986");
        wheels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wheels.add(new Wheel("GuLu", 10));
        }
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected int getLevel() {
        return level;
    }

    protected void setLevel(int level) {
        this.level = level;
    }

    protected Brand getBrand() {
        return brand;
    }

    protected void setBrand(Brand brand) {
        this.brand = brand;
    }

    protected List<Wheel> getWheels() {
        return wheels;
    }

    protected void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
    }
    
}
