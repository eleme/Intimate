package me.ele.example;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ele.example.lib.User;
import me.ele.example.ref.RefUser;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by lizhaoxuan on 2018/1/1.
 */
@RunWith(AndroidJUnit4.class)
public class AppClassTest {

    @Test
    public void testAppClass() throws Exception {
        User user = new User("kaka", "男", 19, "三年二班");

        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
        assertNotNull(refUser);

        assertEquals(refUser.getName(), "kaka");

        assertEquals(refUser.getSexStr(), "男");
        refUser.setSexRef("你猜猜");
        assertEquals(refUser.getSexStr(), "你猜猜");

        assertEquals(refUser.getAge(), 19);
        refUser.setAge(19, 1);
        assertEquals(refUser.getAge(), 20);
        assertEquals(refUser.getAgeStr(), "20");

    }

}
