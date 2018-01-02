package me.ele.example.ref;

import android.view.View;

import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTargetForName;

/**
 * Created by lizhaoxuan on 2017/12/15.
 */
@RefTargetForName(className = "android.view.View$ListenerInfo", optimizationRef = false)
public interface RefListenerInfo {

    @GetField("mOnClickListener")
    View.OnClickListener getListener();

}
