package me.ele.example.ref;

import android.support.v7.widget.RecyclerView;

import me.ele.example.Constant;
import me.ele.intimate.annotation.GetField;
import me.ele.intimate.annotation.RefTarget;
import me.ele.intimate.annotation.SetField;

/**
 * Created by lizhaoxuan on 2017/12/31.
 */
@RefTarget(clazz = RecyclerView.class, optimizationRef = Constant.OPTIMIZATION_REF)
public interface RefRecyclerView {

    @GetField("mLastTouchY")
    int getLastTouchY();

    @SetField("mLastTouchY")
    void setLastTouchY(int itemsChanged);

}
