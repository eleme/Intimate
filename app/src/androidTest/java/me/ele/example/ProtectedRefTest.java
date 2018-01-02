package me.ele.example;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.ele.example.mock.Brand;
import me.ele.example.mock.CarProtected;
import me.ele.example.mock.Wheel;
import me.ele.example.ref.RefCarProtected;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test "reflection" Private field and method.
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class ProtectedRefTest {

    @Test
    public void testProtectedRefField() throws Exception {
        CarProtected car = new CarProtected();
        RefCarProtected refCarProtected = RefImplFactory.getRefImpl(car, RefCarProtected.class);
        assertNotNull(refCarProtected);

        //test String field
        assertEquals(refCarProtected.getNameField(), "my Protected car");
        refCarProtected.setNameField("changeName");
        assertEquals(refCarProtected.getNameField(), "changeName");

        //test Primitive field
        assertEquals(refCarProtected.getLevelField(), 7);
        refCarProtected.setLevelField(12);
        assertEquals(refCarProtected.getLevelField(), 12);

        //test Object field
        assertEquals(refCarProtected.getBrandField(), new Brand("biubiuCar", "made in china for 1986"));
        refCarProtected.setBrandField(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarProtected.getBrandField(), new Brand("papaCar", "made in china for 1999"));

        //test set field
        assertEquals(refCarProtected.getWhellListField().size(), 4);
        List<Wheel> wheels_field = refCarProtected.getWhellListField();
        wheels_field.add(new Wheel("new Wheels", 4));
        refCarProtected.setWhellListField(wheels_field);
        assertEquals(refCarProtected.getWhellListField().size(), 5);
    }

    @Test
    public void testProtectedRefMethod() throws Exception {
        CarProtected car = new CarProtected();
        RefCarProtected refCarProtected = RefImplFactory.getRefImpl(car, RefCarProtected.class);
        assertNotNull(refCarProtected);

        //test String method
        assertEquals(refCarProtected.getName(), "my Protected car");
        refCarProtected.setName("changeName");
        assertEquals(refCarProtected.getName(), "changeName");

        //test Primitive method
        assertEquals(refCarProtected.getLevel(), 7);
        refCarProtected.setLevel(12);
        assertEquals(refCarProtected.getLevel(), 12);

        //test Object method
        assertEquals(refCarProtected.getBrand(), new Brand("biubiuCar", "made in china for 1986"));
        refCarProtected.setBrand(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarProtected.getBrand(), new Brand("papaCar", "made in china for 1999"));

        //test set method
        assertEquals(refCarProtected.getWheels().size(), 4);
        List<Wheel> wheels_method = refCarProtected.getWheels();
        wheels_method.add(new Wheel("new Wheels", 4));
        refCarProtected.setWheels(wheels_method);
        assertEquals(refCarProtected.getWheels().size(), 5);
    }

}
