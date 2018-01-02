# Intimate

Intimate 提供了友好的 API 让 java 反射的使用更加简单平滑。

其最核心的价值在于 Intimate 将在编译期对 apk 内部代码（您编写的 app 代码或引入的第三方库）的调用进行反射优化，使得反射查找与调用就像普通调用一样快捷且无任何代价。

*‘Apk内部代码包含您编写的 app 应用层代码以及引入的第三方库（含Android support）代码。’*

## 开始使用

在根目录的 `build.gradle` 添加：

```
dependencies{
    classpath 'me.ele:intimate-plugin:0.0.1'
}
```

在 app 目录的`build.gradle`添加：

```
apply plugin: 'me.ele.intimate-plugin'

dependencies {
	compile 'me.ele:intimate:0.0.1'
    annotationProcessor 'me.ele:intimate-compiler:0.0.1'
}
```

## 示例



希望反射调用的类：

```
public class User {

    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void setAge(int a, int b) {
        this.age = a + b;
    }

}
```

通过接口描述你对反射的需求：

```
@RefTarget(clazz = User.class, optimizationRef = true)
public interface RefUser {

    @GetField("name")
    String getName();

    @SetField("name")
    void setName(String value);

    @GetField("age")
    int getAge();

    @Method
    void setAge(int a, int b);
}
```
使用 RefImplFactory 创建 RefUser 实例，之后便可以通过接口 RefUser 实现对某个对象任意属性或方法的访问

```
User user = new User("暴打小女孩", "男", 19, "三年二班");
RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
if(refUser != null){
	assertEquals(refUser.getName(), "暴打小女孩");
    refUser.setName("kaka");
    assertEquals(refUser.getName(), "kaka");

	assertEquals(refUser.getAge(), 19);
    refUser.setAge(19,1);
    assertEquals(refUser.getAge(), 20);
}
```

## API

#### @RefTarget @RefTargetForName

```
public @interface RefTarget {

    Class clazz();
    
    boolean optimizationRef();
}

public @interface RefTargetForName {

    String className();
    
    boolean optimizationRef();
}
```

`@RefTarget` `@RefTargetForName` 对描述期望反射的目标类。

`RefTarget.clazz` 属性传入期望反射的目标类的 Class ，当你期望反射的目标类是某个不对外暴露的私有类或内部类，无法取得 Class对象时，可以使用`RefTargetForName.className`通过目标类的字符串类名进行描述。

Intimate可以对apk内部代码（您编写的app代码或引入的第三方库）的调用进行反射优化，使得反射调用就像普通调用一样快捷且无任何代价，对固化在ROM中的系统类则依然只能通过常规反射进行调用。

所以，对于第三方库的反射，`optimizationRef` 值应该为 true，Intimate 将对其进行优化。
Android 系统类以及 java 核心库等 System 类 `optimizationRef` 值应该为 false

示例：

```
@RefTarget(clazz = RecyclerView.class, optimizationRef = true)
public interface RefRecyclerView {

    @GetField("mLastTouchY")
    int getLastTouchY();

    @SetField("mLastTouchY")
    void setLastTouchY(int itemsChanged);
}

@RefTargetForName(className = "android.view.View$ListenerInfo", optimizationRef = false)
public interface RefListenerInfo {

    @GetField("mOnClickListener")
    View.OnClickListener getListener();

}


```

**应尽可能的使用`optimizationRef = true`，以避免不必要的反射查找耗时，但当System 类使用`optimizationRef = true`时，将构建失败。**

**应尽可能的使用`RefTarget.clazz = XXX.class`** ,因为`RefTargetForName.className = "xxx.xx.xxx.class"` 属性将使用 `Class.forName("xxx.xx.xxx.class")`实现 Class的获取，你应该避免这样的操作。


#### @GetField  @SetField

```
public @interface GetField {
    String value();
}

public @interface SetField {
    String value();
}

```

`@GetField` `@SetField` 描述对类属性的 get 与 set。

`value` 值描述属性的名称，Intimate 通过 @GetField 修饰的方法的返回值确定属性的类型，通过 @SetField 的参数值确定属性的类型。

需要特别说明的是，当某个属性的类型是内部类或私有类时，你可以用Object来修饰返回值或参数。

```
@GetField("name")
String getName();

@SetField("age")
void setName(int age);
```

上面两个例子中，Intimate 得知将要反射调用的两个属性分别为 [java.lang.String : name] , [int : age] 。

#### @Method

```
public @interface Method {
    String value() default "";
}
```

`@Method` 描述对类方法的调用。

`value` 值描述方法名，可缺省。缺省时，Intimate 默认方法名为`@Method`修饰的方法名。

Intimate 将匹配目标类中与`@Method`修饰的方法完全一样（返回类型,方法名,参数列表）的方法。

需要特别说明的是，当某个属性的类型是内部类或私有类时，你可以用Object来修饰返回值或参数。

下面将给出正确与错误的示例。
目标类：
```
class User{
	int calculateAge(int year){
    	return 2018-year;
    }
}

```

```
//匹配User calculateAge方法
@Method
int calculateAge(int year);

//匹配User calculateAge方法
@Method("calculateAge")
int getAge(int year);

//User类中无getAge(int)方法，匹配失败,构建失败
@Method
int getAge(int year);

//User类中无calculateAge(int,int)方法，匹配失败，构建失败
@Method
int calculateAge(int year,int month);
```

#### 异常处理

默认情况下，当`optimizationRef = false` 时，Intimate 将会 catch 掉所有异常。*`optimizationRef = true`时若找不到方法或属性，编译期便会抛出异常。*

此时如果你需要对某些异常进行处理，可以这样做：

```
@GetField("mListenerInfo")
    Object getListenerInfo() throws IllegalAccessException, NoSuchFieldException;
    
```
在调用`getListenerInfo()`时，如果遇到这两个异常将会抛出，catch其余异常。

#### 特殊示例

一切特别的场景需要特别的姿势。

目标类：

```
class View {

	...
    static class ListenerInfo {
    	...
        public OnClickListener mOnClickListener;
    }

}

```
当你期望反射得到 `OnClickListener mOnClickListener`时，一个描述接口可能并不能实现你的需求，此时你需要两个。

```
@RefTarget(clazz = TextView.class, optimizationRef = false)
public interface RefTextView {
    @GetField("mListenerInfo")
    Object getListenerInfo() throws IllegalAccessException, NoSuchFieldException;
}

@RefTargetForName(className = "android.view.View$ListenerInfo", optimizationRef = false)
public interface RefListenerInfo {
    @GetField("mOnClickListener")
    View.OnClickListener getListener();
}
```

调用

```
TextView textView = new TextView(context);
RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);

RefListenerInfo refListenerInfo = RefImplFactory.getRefImpl(refTextView.getListenerInfo(), RefListenerInfo.class);

View.OnClickListener listener = refListenerInfo.getListener();
```

## Tips:

- 内部类应该命名为 package.outer_class$inner_class
- 应尽可能的使用`optimizationRef = true`，但当系统类修饰`optimizationRef = true`时将构建失败，合理识别目标类的类型
- 当某个属性的类型是内部类或私有类时，你可以用Object来修饰返回值或参数
- 对于某些内部类或私有类，可以通过多个Ref接口结合使用
- 其他使用示例可以在Test case中查看：[app/src/androidTest/](https://github.com/ELELogistics/Intimate/tree/master/app/src/androidTest/java/me/ele/example)

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

