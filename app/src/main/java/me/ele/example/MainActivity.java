package me.ele.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import me.ele.example.lib.User;
import me.ele.example.ref.RefGson;
import me.ele.example.ref.RefListenerInfo;
import me.ele.example.ref.RefRecyclerView;
import me.ele.example.ref.RefTextView;
import me.ele.example.ref.RefUser;
import me.ele.intimate.RefImplFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Intimate";

    private TextView textView;
    StringBuilder builder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);


        //系统类
        testSystem();
        //获取内部类以及异常处理
        testInnerClass();

        //普通类
        testDefault();

        //第三方库-常量
        testConstant();

        //第三方库-android.support
        testAndroidSupport();

        textView.setText(builder.toString());
    }

    private void testSystem() {
        RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
        if (refTextView == null) {
            printLog("refTextView == null");
            return;
        }
        printLog("TextView text:" + refTextView.getText().toString());
        printLog("TextView getDesiredHeight:" + refTextView.getDesiredHeight());
    }

    private void testInnerClass() {
        RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
        if (refTextView == null) {
            printLog("refTextView == null");
            return;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "listener");
            }
        };
        textView.setOnClickListener(listener);
        try {
            RefListenerInfo refListenerInfo = RefImplFactory.getRefImpl(refTextView.getListenerInfo(), RefListenerInfo.class);
            if (refListenerInfo == null) {
                printLog("refListenerInfo == null");
                return;
            }
            if (listener == refListenerInfo.getListener()) {
                printLog("OnClickListener getListener success");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void testDefault() {
        User user = new User("kaka", "男", 19, "三年二班");
        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
        if (refUser == null) {
            printLog("refUser == null");
            return;
        }
        printLog("User getName:" + refUser.getName());
        printLog("User getSexStr: " + refUser.getSexStr());
        refUser.setSexRef("你猜猜ss");
        printLog("User getSex：" + refUser.getSex());
        refUser.setAge(1, 2);
        printLog("User getAge: " + refUser.getAge());
    }

    private void testConstant() {
        RefGson refGson = RefImplFactory.getRefImpl(new Gson(), RefGson.class);
        if (refGson == null) {
            printLog("refGson == null");
            return;
        }
        printLog("Gson getDefaultLenient:" + refGson.getDefaultLenient());
        printLog("Gson getDefaultPrettyPrint:" + refGson.getDefaultPrettyPrint());
    }

    private void testAndroidSupport() {
        RecyclerView recyclerView = new RecyclerView(this);
        RefRecyclerView refRecyclerView = RefImplFactory.getRefImpl(recyclerView, RefRecyclerView.class);
        if (refRecyclerView == null) {
            printLog("refRecyclerView == null");
            return;
        }
        printLog("recyclerView:" + refRecyclerView.getLastTouchY());
        refRecyclerView.setLastTouchY(11);
        printLog("recyclerView:" + refRecyclerView.getLastTouchY());
    }

    private void printLog(String string) {
        Log.d(TAG, string);
        builder.append(string).append("\n");
    }
}
