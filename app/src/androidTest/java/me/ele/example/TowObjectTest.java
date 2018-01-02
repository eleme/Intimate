package me.ele.example;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ele.example.mock.CarPrivate;
import me.ele.example.ref.RefCarPrivate;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by lizhaoxuan on 2018/1/2.
 */
@RunWith(AndroidJUnit4.class)
public class TowObjectTest {

    @Test
    public void testTwoObject() throws Exception {

        CarPrivate car1 = new CarPrivate();
        CarPrivate car2 = new CarPrivate();

        RefCarPrivate carPrivate1 = RefImplFactory.getRefImpl(car1, RefCarPrivate.class);

        assertEquals(carPrivate1.getNameField(), "my Private car");
        carPrivate1.setNameField("car1");
        assertEquals(carPrivate1.getNameField(), "car1");

        assertEquals(carPrivate1.getLevel(), 7);
        carPrivate1.setLevel(12);
        assertEquals(carPrivate1.getLevel(), 12);

        RefCarPrivate carPrivate2 = RefImplFactory.getRefImpl(car2, RefCarPrivate.class);

        assertEquals(carPrivate2.getNameField(), "my Private car");
        carPrivate2.setNameField("car2");
        assertEquals(carPrivate2.getNameField(), "car2");

        assertEquals(carPrivate2.getLevel(), 7);
        carPrivate2.setLevel(99);
        assertEquals(carPrivate2.getLevel(), 99);


    }

}
