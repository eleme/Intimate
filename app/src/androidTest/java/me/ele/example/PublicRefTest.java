package me.ele.example;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import me.ele.example.mock.Brand;
import me.ele.example.mock.CarPublic;
import me.ele.example.mock.Wheel;
import me.ele.example.ref.RefCarPublic;
import me.ele.intimate.RefImplFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test "reflection" Private field and method.
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class PublicRefTest {

    @Test
    public void testPublicRefField() throws Exception {
        CarPublic car = new CarPublic();
        RefCarPublic refCarPublic = RefImplFactory.getRefImpl(car, RefCarPublic.class);

        assertNotNull(refCarPublic);
        //test String field
        assertEquals(refCarPublic.getNameField(), "my Public car");
        refCarPublic.setNameField("changeName");
        assertEquals(refCarPublic.getNameField(), "changeName");

        //test Primitive field
        assertEquals(refCarPublic.getLevelField(), 7);
        refCarPublic.setLevelField(12);
        assertEquals(refCarPublic.getLevelField(), 12);

        //test Object field
        assertEquals(refCarPublic.getBrandField(), new Brand("biubiuCar", "made in china for 1986"));
        refCarPublic.setBrandField(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarPublic.getBrandField(), new Brand("papaCar", "made in china for 1999"));

        //test set field
        assertEquals(refCarPublic.getWhellListField().size(), 4);
        List<Wheel> wheels_field = refCarPublic.getWhellListField();
        wheels_field.add(new Wheel("new Wheels", 4));
        refCarPublic.setWhellListField(wheels_field);
        assertEquals(refCarPublic.getWhellListField().size(), 5);
    }

    @Test
    public void testPublicRefMethod() throws Exception {
        CarPublic car = new CarPublic();
        RefCarPublic refCarPublic = RefImplFactory.getRefImpl(car, RefCarPublic.class);
        assertNotNull(refCarPublic);

        //test String method
        assertEquals(refCarPublic.getName(), "my Public car");
        refCarPublic.setName("changeName");
        assertEquals(refCarPublic.getName(), "changeName");

        //test Primitive method
        assertEquals(refCarPublic.getLevel(), 7);
        refCarPublic.setLevel(12);
        assertEquals(refCarPublic.getLevel(), 12);

        //test Object method
        assertEquals(refCarPublic.getBrand(), new Brand("biubiuCar", "made in china for 1986"));
        refCarPublic.setBrand(new Brand("papaCar", "made in china for 1999"));
        assertEquals(refCarPublic.getBrand(), new Brand("papaCar", "made in china for 1999"));

        //test set method
        assertEquals(refCarPublic.getWheels().size(), 4);
        List<Wheel> wheels_method = refCarPublic.getWheels();
        wheels_method.add(new Wheel("new Wheels", 4));
        refCarPublic.setWheels(wheels_method);
        assertEquals(refCarPublic.getWheels().size(), 5);
    }

}
