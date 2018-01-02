package me.ele.example;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ele.example.ref.RefRecyclerView;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by lizhaoxuan on 2018/1/1.
 */
@RunWith(AndroidJUnit4.class)
public class CompileClassTest {

    @Test
    public void testAppClass() throws Exception {
        RecyclerView recyclerView = new RecyclerView(InstrumentationRegistry.getTargetContext());

        RefRecyclerView refRecyclerView = RefImplFactory.getRefImpl(recyclerView, RefRecyclerView.class);
        assertNotNull(refRecyclerView);

        assertEquals(refRecyclerView.getLastTouchY(), 0);
        refRecyclerView.setLastTouchY(11);
        assertEquals(refRecyclerView.getLastTouchY(), 11);

    }

}
