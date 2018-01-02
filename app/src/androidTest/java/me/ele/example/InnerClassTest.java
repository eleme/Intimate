package me.ele.example;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ele.example.mock.InnerClazz;
import me.ele.example.ref.RefInnerClazz;
import me.ele.example.ref.RefPrivateInnerClass;
import me.ele.example.ref.RefPublicStaticInnerClass;
import me.ele.example.ref.RefStaticInnerClass;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;

/**
 * Created by lizhaoxuan on 2017/12/31.
 */
@RunWith(AndroidJUnit4.class)
public class InnerClassTest {

    @Test
    public void testUnUseShell() throws Exception {
        //利用反射创建RefFactory类
        assertEquals(true, testPrivateInnerClass());
        assertEquals(true, testStaticInnerClass());
        assertEquals(true, testStaticPublicInnerClass());

    }

    @Test
    public void testUseShell() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(true, testPrivateInnerClass());
        assertEquals(true, testStaticInnerClass());
        assertEquals(true, testStaticPublicInnerClass());
    }

    private boolean testPrivateInnerClass() {
        RefInnerClazz refInnerClazz = RefImplFactory.getRefImpl(new InnerClazz(), RefInnerClazz.class);
        if (refInnerClazz == null) {
            return false;
        }
        RefPrivateInnerClass refPrivateInnerClass = RefImplFactory.getRefImpl(refInnerClazz.getPrivateInnerClass(),
                RefPrivateInnerClass.class);
        if (refPrivateInnerClass == null) {
            return false;
        }
        assertEquals(refPrivateInnerClass.getName(), "defaultInnerClass");
        return true;
    }

    private boolean testStaticInnerClass() {
        RefInnerClazz refInnerClazz = RefImplFactory.getRefImpl(new InnerClazz(), RefInnerClazz.class);
        if (refInnerClazz == null) {
            return false;
        }
        RefStaticInnerClass refStaticInnerClass = RefImplFactory.getRefImpl(refInnerClazz.getStaticInnerClass(),
                RefStaticInnerClass.class);
        if (refStaticInnerClass == null) {
            return false;
        }
        assertEquals(refStaticInnerClass.getName(), "staticInnerClass");
        return true;
    }

    private boolean testStaticPublicInnerClass() {
        RefInnerClazz refInnerClazz = RefImplFactory.getRefImpl(new InnerClazz(), RefInnerClazz.class);
        if (refInnerClazz == null) {
            return false;
        }
        RefPublicStaticInnerClass refPublicStaticInnerClass = RefImplFactory.getRefImpl(refInnerClazz.getPublicStaticInnerClass(),
                RefPublicStaticInnerClass.class);
        if (refPublicStaticInnerClass == null) {
            return false;
        }
        assertEquals(refPublicStaticInnerClass.getName(), "publicStaticInnerClass");
        return true;
    }

}
