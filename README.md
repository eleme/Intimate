# Intimate

Intimate provides a friendly API to make Java reflection easier and smoothness.

It core value is, Intimate will optimize the reflection call of the `Apk Code` at compile time, completely remove reflection search time，making a reflection call is as fast as a normal call.


*' `Apk Code` is the application layer code that you wrote and the library that you introduced（contains android.support）.'*

[中文 README](https://github.com/ELELogistics/Intimate/blob/master/README_zh.md)

## 开始使用

Firstly, add following code in root `build.gradle` of your project.

```
dependencies{
    classpath 'me.ele:intimate-plugin:xxx'
}
```

And then, add following code in your application module's `build.gradle`

```
apply plugin: 'me.ele.intimate-plugin'

dependencies {
    compile 'me.ele:intimate:xxx'
    annotationProcessor 'me.ele:intimate-compiler:xxx'
}
```

## The sample


Classes that are expected to reflect：

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

Use the interface to describe your need for reflection：

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

Use `RefImplFactory` create `RefUser` instance，after that, you can access any field or method of an object through the `RefUser`.

```
User user = new User("papapa", "man", 19, "Class 3");
RefUser refUser = RefImplFactory.getRefImpl(user, RefUser.class);
if(refUser != null){

    assertEquals(refUser.getName(), "papapa");
    refUser.setName("kaka");
    assertEquals(refUser.getName(), "kaka");

    assertEquals(refUser.getAge(), 19);
    refUser.setAge(19,1);
    assertEquals(refUser.getAge(), 20);
    
}
```

## API

#### @RefTarget    @RefTargetForName

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

`@RefTarget` `@RefTargetForName` Describes the target class that is expected to reflect. I call her RefInterface.

`@RefTarget（clazz=XXX.class)` 

'xxx.class' is your target class. when your target class is a private class or inner class ，don‘t get Class object,you can use `@RefTargetForName(className="xxx.xxx.xx")` ,'xxx.xxx.xxx' is class canonical name.

Intimate can optimize the reflection calls of the apk code, completely remove reflection search time. But the system code that solidifies in ROM, Still only through normal reflection.


so, for apk code, `optimizationRef` value should be true，Intimate will optimize it.

Android system class and java.lang.* and so on , `optimizationRef` value should be false

The sample：

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

**You should use `optimizationRef = true` as much as possible, to avoid unnecessary reflection search. But when System class use `optimizationRef = true`, the build will fail.**


**You should use `@RefTarget(clazz = XXX.class）` as much as possible** , because `@RefTargetForName(className = "xxx.xx.xxx.class"）` will use `Class.forName("xxx.xx.xxx.class")` to implement get Class, you should avoid such an operation.

 
#### @GetField    @SetField

```
public @interface GetField {

    String value();
    
}

public @interface SetField {

    String value();
    
}

```

`@GetField` `@SetField` Describes the `get` and `set` of Class field.

`value` is field name.

Intimate determined field type through `@GetField` annotation method's returnType.

Intimate determined field type through `@SetField` annotation method's parameterType.

Special instructions: when te field type is private Class or inner class, you can use Object deed returnType or parameterType

```
@GetField("name")
String getName();

@SetField("age")
void setName(int age);
```

In the example above, Intimate know the target is [java.lang.String : name] and  [int : age]

#### @Method

```
public @interface Method {
    String value() default "";
}
```

`@Method` Describes the `method' 

`value` is method name, can be the default, when the default，Intimate determined method name through `@Method` annotation method's name.

Intimate will match the target with `@Method` annotation method exactly the same method.(returnType, methodName, parameterList)

Special instructions: when te field type is private Class or inner class, you can use Object deed returnType or parameterType.

Examples of right and wrong are given below.

Target Class：

```
class User {
    int calculateAge(int year) {
        return 2018 - year;
    }
}

```

```
//matching User - calculateAge(int) 
@Method
int calculateAge(int year);

//matching User-calculateAge(int) 
@Method("calculateAge")
int getAge(int year);

//User don't have getAge(int)，matching failure, build failure
@Method
int getAge(int year);

//User don't have calculateAge(int,int)，matching failure, build failure
@Method
int calculateAge(int year,int month);
```

#### Exception handling

Default, when `optimizationRef = false`, Intimate will catch all exception.
when `optimizationRef = true`, if can't find field or method, build will fail.

when `optimizationRef = false`,you need handle some exception, you can do that:

```
@GetField("mListenerInfo")
Object getListenerInfo() throws IllegalAccessException, NoSuchFieldException;
    
```
`getListenerInfo()` will throw IllegalAccessException or NoSuchFieldException，，Intimate catch other exception。

#### Create RefInterface instance

you can use `RefImplFactory.getRefImpl` create RefInterface instance:

```

public class RefImplFactory {
    public static <T> T getRefImpl(Object object, Class clazz){...}
}

```

```
RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);

```

#### Cache recovery

When `@RefTarget(optimizationRef = false)`  or `@RefTargetForName(optimizationRef = false)`，Intimate will cache Field and Method, so the Intimat requires only one reflection search.

If you are certain you will not continue to reflect on a target class，can clear cache, the parameter is RefInterface class.

```
public class RefImplFactory {

    public static void clearAccess(Class refClazz){...}
    
    public static void clearAllAccess(){...}
}

```

The sample：

```
RefImplFactory.clearAccess(RefTextView.class);

RefImplFactory.clearAllAccess()；
```

 when `@RefTarget(optimizationRef = false)`  or `@RefTargetForName(optimizationRef = false)`，no cache，don't need to recycle.

#### Special sample

Special situations require special gestures.

Target Class：

```
class View {

	...
    static class ListenerInfo {
    	...
        public OnClickListener mOnClickListener;
    }

}

```
When you get  `OnClickListener mOnClickListener`，A RefInterface may not fulfill your requirements,  you need two.

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

client:

```
TextView textView = new TextView(context);
RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);

RefListenerInfo refListenerInfo = RefImplFactory.getRefImpl(refTextView.getListenerInfo(), RefListenerInfo.class);

View.OnClickListener listener = refListenerInfo.getListener();
```

## ProGuard

You should ensure that your target class will not be ProGuard or that the target class and its attributes will not be found.

## Tips:

- Inner classes should be named like `package.outer_class$inner_class`
- You should use `optimizationRef = true` as much as possible, to avoid unnecessary reflection search. But when System class use `optimizationRef = true`, the build will fail.
- When te field type is private Class or inner class, you can use Object deed returnType or parameterType.
- Get some private class or inner class object,you can use multiple RefInterface.
- When `@RefTarget(optimizationRef = true)`  or `@RefTargetForName(optimizationRef = true)`, no cache,don't need to recycle.
- You can come here， learn more cases：[app/src/androidTest/](https://github.com/ELELogistics/Intimate/tree/master/app/src/androidTest/java/me/ele/example)
- **If the Aspectjx executes first, the Intimate may be invalid**

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

