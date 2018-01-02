package me.ele.example;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.ele.example.ref.RefListenerInfo;
import me.ele.example.ref.RefTextView;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by lizhaoxuan on 2018/1/1.
 */
@RunWith(AndroidJUnit4.class)
public class SystemClassTest {

    @Test
    public void testRefTextView() throws Exception {
        TextView textView = new TextView(InstrumentationRegistry.getTargetContext());

        RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
        assertNotNull(refTextView);

        assertEquals(refTextView.getText().toString(), "");
        textView.setText("暴打小女孩");
        assertEquals(refTextView.getText().toString(), "暴打小女孩");

        assertEquals(refTextView.getDesiredHeight(), 0);


    }

    public void testRefListenerInfo() {
        TextView textView = new TextView(InstrumentationRegistry.getTargetContext());
        RefListenerInfo refListenerInfo = null;

        try {
            RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
            assertNotNull(refTextView);
            refListenerInfo = RefImplFactory.getRefImpl(refTextView.getListenerInfo(), RefListenerInfo.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        assertNotNull(refListenerInfo);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "listener");
            }
        };
        textView.setOnClickListener(listener);
        assertEquals(listener, refListenerInfo.getListener());

    }

}
