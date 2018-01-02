package me.ele.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.ele.example.mock.CarPrivate;
import me.ele.example.ref.RefCarPrivate;
import me.ele.intimate.RefImplFactory;

import static junit.framework.Assert.assertEquals;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    int type = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);

//        RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);
//        refTextView.getText();
//        Log.d("TAG", refTextView.getText().toString());
//        Log.d("TAG", refTextView.getDesiredHeight() + " ");
//
//        int type = 1000;
//
//        User user = new User("kaka", "男", 19, "三年二班");
//
//        RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
//        Log.d("TAG", "getName:" + refUser.getName());
//        Log.d("TAG", "getSexStr: " + refUser.getSexStr());
//        refUser.setSexRef("你猜猜ss");
//        Log.d("TAG", "getSex：" + refUser.getSex());
//        refUser.setAge(1, 2);
//        Log.d("TAG", "getAge: " + refUser.getAge());
//        Log.d("TAG", "getAgeStr: " + refUser.getAgeStr());
//        Log.d("TAG", "getClassName: " + refUser.getClassName());
//
//
//        RefGson refGson = RefImplFactory.getRefImpl(new Gson(), RefGson.class);
//        Log.d("TAG", "getDefaultLenient:" + refGson.getDefaultLenient());
//        Log.d("TAG", "getDefaultPrettyPrint:" + refGson.getDefaultPrettyPrint());
//        new CarPrivate();
//        RefCarPrivate carPrivate = RefImplFactory.getRefImpl(new CarPrivate(), RefCarPrivate.class);
//        Log.d("TAG", "carPrivate:" + carPrivate.getNameField());
//
//        RecyclerView recyclerView = new RecyclerView(this);
//        RefRecyclerView refRecyclerView = RefImplFactory.getRefImpl(recyclerView, RefRecyclerView.class);
//        Log.d("TAG", "recyclerView:" + refRecyclerView.getLastTouchY());
//        refRecyclerView.setLastTouchY(11);
//        Log.d("TAG", "recyclerView:" + refRecyclerView.getLastTouchY());

        testStaticInnerClass();
    }

    private void testStaticInnerClass() {
        CarPrivate car1 = new CarPrivate();
        CarPrivate car2 = new CarPrivate();

        RefCarPrivate carPrivate1 = RefImplFactory.getRefImpl(car1, RefCarPrivate.class);

        assertEquals(carPrivate1.getNameField(), "my Private car");
        carPrivate1.setNameField("car1");
        assertEquals(carPrivate1.getNameField(), "car1");

        assertEquals(carPrivate1.getLevel(), 7);
        carPrivate1.setLevel(12);
        assertEquals(carPrivate1.getLevel(), 12);

        RefCarPrivate carPrivate2 = RefImplFactory.getRefImpl(car2, RefCarPrivate.class);

        assertEquals(carPrivate2.getNameField(), "my Private car");
        carPrivate2.setNameField("car2");
        assertEquals(carPrivate2.getNameField(), "car2");

        assertEquals(carPrivate2.getLevel(), 7);
        carPrivate2.setLevel(99);
        assertEquals(carPrivate2.getLevel(), 99);
    }

}
