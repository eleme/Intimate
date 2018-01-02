package me.ele.example;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.ele.example.mock.Brand;
import me.ele.example.mock.CarPrivate;
import me.ele.example.mock.Wheel;
import me.ele.example.ref.RefCarPrivate;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test "reflection" Private field and method.
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class PrivateRefTest {

    @Test
    public void testPrivateRefField() throws Exception {
        CarPrivate car = new CarPrivate();
        RefCarPrivate refCarPrivate = RefImplFactory.getRefImpl(car, RefCarPrivate.class);

        assertNotNull(refCarPrivate);

        //test String field
        assertEquals(refCarPrivate.getNameField(), "my Private car");
        refCarPrivate.setNameField("changeName");
        assertEquals(refCarPrivate.getNameField(), "changeName");

        //test Primitive field
        assertEquals(refCarPrivate.getLevelField(), 7);
        refCarPrivate.setLevelField(12);
        assertEquals(refCarPrivate.getLevelField(), 12);

        //test Object field
        assertEquals(refCarPrivate.getBrandField(), new Brand("biubiuCar", "made in china for 1986"));
        refCarPrivate.setBrandField(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarPrivate.getBrandField(), new Brand("papaCar", "made in china for 1999"));

        //test set field
        assertEquals(refCarPrivate.getWhellListField().size(), 4);
        List<Wheel> wheels_field = refCarPrivate.getWhellListField();
        wheels_field.add(new Wheel("new Wheels", 4));
        refCarPrivate.setWhellListField(wheels_field);
        assertEquals(refCarPrivate.getWhellListField().size(), 5);

    }

    @Test
    public void testPrivateRefMethod() throws Exception {
        CarPrivate car = new CarPrivate();
        RefCarPrivate refCarPrivate = RefImplFactory.getRefImpl(car, RefCarPrivate.class);

        assertNotNull(refCarPrivate);

        //test String method
        assertEquals(refCarPrivate.getName(), "my Private car");
        refCarPrivate.setName("changeName");
        assertEquals(refCarPrivate.getName(), "changeName");

        //test Primitive method
        assertEquals(refCarPrivate.getLevel(), 7);
        refCarPrivate.setLevel(12);
        assertEquals(refCarPrivate.getLevel(), 12);

        //test Object method
        assertEquals(refCarPrivate.getBrand(), new Brand("biubiuCar", "made in china for 1986"));
        refCarPrivate.setBrand(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarPrivate.getBrand(), new Brand("papaCar", "made in china for 1999"));

        //test set method
        assertEquals(refCarPrivate.getWheels().size(), 4);
        List<Wheel> wheels_method = refCarPrivate.getWheels();
        wheels_method.add(new Wheel("new Wheels", 4));
        refCarPrivate.setWheels(wheels_method);
        assertEquals(refCarPrivate.getWheels().size(), 5);

    }

}
