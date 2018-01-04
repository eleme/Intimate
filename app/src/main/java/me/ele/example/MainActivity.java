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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        //系统类
        RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
        refTextView.getText();
        Log.d(TAG, "TextView text:" + refTextView.getText().toString());
        Log.d(TAG, "TextView getDesiredHeight:" + refTextView.getDesiredHeight());

        //获取内部类以及异常处理
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "listener");
            }
        };
        textView.setOnClickListener(listener);
        try {
            RefListenerInfo refListenerInfo = RefImplFactory.getRefImpl(refTextView.getListenerInfo(), RefListenerInfo.class);
            if (listener == refListenerInfo.getListener()) {
                Log.d(TAG, "OnClickListener getListener success");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        //普通类
        User user = new User("kaka", "男", 19, "三年二班");
        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
        Log.d(TAG, "User getName:" + refUser.getName());
        Log.d(TAG, "User getSexStr: " + refUser.getSexStr());
        refUser.setSexRef("你猜猜ss");
        Log.d(TAG, "User getSex：" + refUser.getSex());
        refUser.setAge(1, 2);
        Log.d(TAG, "User getAge: " + refUser.getAge());

        //第三方库-常量
        RefGson refGson = RefImplFactory.getRefImpl(new Gson(), RefGson.class);
        Log.d(TAG, "Gson getDefaultLenient:" + refGson.getDefaultLenient());
        Log.d(TAG, "Gson getDefaultPrettyPrint:" + refGson.getDefaultPrettyPrint());

        //第三方库-android.support
        RecyclerView recyclerView = new RecyclerView(this);
        RefRecyclerView refRecyclerView = RefImplFactory.getRefImpl(recyclerView, RefRecyclerView.class);
        Log.d(TAG, "recyclerView:" + refRecyclerView.getLastTouchY());
        refRecyclerView.setLastTouchY(11);
        Log.d(TAG, "recyclerView:" + refRecyclerView.getLastTouchY());
    }

}
