package me.ele.example.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaoxuan on 2017/12/29.
 */

public class CarPublic {

    public String name;
    public int level;
    public Brand brand;
    public List<Wheel> wheels;

    public CarPublic() {
        name = "my Public car";
        level = 7;
        brand = new Brand("biubiuCar", "made in china for 1986");
        wheels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            wheels.add(new Wheel("GuLu", 10));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Wheel> getWheels() {
        return wheels;
    }

    public void setWheels(List<Wheel> wheels) {
        this.wheels = wheels;
    }

}
