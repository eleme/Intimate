# Intimate

Intimate provides a friendly API to make Java reflection easier and more smooth.

Its core value is optimizing the reflection call at compile time, which completely save reflection search time，and make a reflection call as fast as a normal call.


*' Intiamte only take action on the code that you write and the library introduced（contains android.support.*）. But the system code will not be affected'*

[中文 README](https://github.com/ELELogistics/Intimate/blob/master/README_zh.md)

## Usage

Firstly, add following code in root `build.gradle` of your project.

```
dependencies{
    classpath 'me.ele:intimate-plugin:1.0.0'
}
```

And then, add following code in your application module's `build.gradle`

```
apply plugin: 'me.ele.intimate-plugin'

dependencies {
    compile 'me.ele:intimate:1.0.0'
    annotationProcessor 'me.ele:intimate-compiler:1.0.0'
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

Use the interface to describe what your need for reflection：

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

Use `RefImplFactory` to create `RefUser` instance. Then, you can access any field or method of an object through the `RefUser`.

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

`@RefTarget` and `@RefTargetForName` describe the target class to be reflected. I call her `RefInterface`.

`@RefTarget（clazz=XXX.class)` 

'xxx.class' is your target class. When it is a private class or inner class，form which you can't get `Class` object, you can use `@RefTargetForName(className="xxx.xxx.xx")`, where 'xxx.xxx.xxx' is class canonical name.

Intimate can optimize the reflection call at compile time, which completely save reflection search time，and make a reflection call as fast as a normal call

so, for Android system class, java.lang.* and etc., `optimizationRef` value should be false.

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

**To avoid unnecessary reflection search, the value of optimizationRef is highly recommended to be true. But when System class use `optimizationRef = true`, the build will fail.**


**You should use `@RefTarget(clazz = XXX.class）` as much as possible**, because `@RefTargetForName(className = "xxx.xx.xxx.class"）` will use `Class.forName("xxx.xx.xxx.class")`, you should avoid such an operation.

 
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

Special instructions: when te field type is private Class or inner class, you can convert the returnType or parameterType to Object.

```
@GetField("name")
String getName();

@SetField("age")
void setName(int age);

@SetField("user")
void setUser(Object user);
```

In the example above, Intimate know the target is [java.lang.String : name], [int : age] and [Object : user]

#### @Method

```
public @interface Method {
    String value() default "";
}
```

`@Method` Describes the 'method' 

`value` is method name which can be omitted. When it's omitted，Intimate deduces method name through `@Method` annotation.

Intimate will find the function with signature matching the one annotated by `@Method`. (with same returnType, methodName, parameterList)

Special instructions: when te field type is private Class or inner class, you can convert the returnType or parameterType to Object.

Both correct and wrong examples are shown as follows.

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

By default, when `optimizationRef = false`, Intimate will catch all exception.
when `optimizationRef = true`, if can't find field or method and build will fail.

when `optimizationRef = false`,you need handle some exception, you can do that:

```
@GetField("mListenerInfo")
Object getListenerInfo() throws IllegalAccessException, NoSuchFieldException;
    
```
`getListenerInfo()` will throw IllegalAccessException or NoSuchFieldException, Intimate catch other exception.

#### Create RefInterface instance

you can use `RefImplFactory.getRefImpl` to create RefInterface instance:

```

public class RefImplFactory {
    public static <T> T getRefImpl(Object object, Class clazz){...}
}

```

```
RefTextView refTextView = RefImplFactory.getRefImpl(textView, RefTextView.class);

```

#### Cache managerment

When `@RefTarget(optimizationRef = false)` or `@RefTargetForName(optimizationRef = false)`，Intimate will cache Field and Method, so the Intimat requires only one reflection search.

If you don't need reflection on some class any more，cache could be cleaned. The parameter is RefInterface class.

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

When `@RefTarget(optimizationRef = false)`  or `@RefTargetForName(optimizationRef = false)`, cache is disabled and you don't need to clear it.

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
When you get  `OnClickListener mOnClickListener`，Single RefInterface may not fulfill your requirements,  you need two.

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

You should ensure that your target class will not be obfuscated or that the target class and its attributes will not be found.

## Tips:

- Inner class should be named like `package.outer_class$inner_class`
- To avoid unnecessary reflection search, the value of optimizationRef is highly recommended to be true. But when System class use `optimizationRef = true`, the build will fail.
- when te field type is private Class or inner class, you can convert the returnType or parameterType to Object.
- To get some private class or inner class object,you can use multiple RefInterface.
- You can visit the link to learn more. [app/src/androidTest/](https://github.com/ELELogistics/Intimate/tree/master/app/src/androidTest/java/me/ele/example)
- **If Aspectjx executes first, the Intimate may be invalid**

## License

<img alt="Apache-2.0 license" src="https://lucene.apache.org/images/mantle-power.png" width="128">

Intimate is available under the Apache-2.0 license. See the LICENSE file for more info.
