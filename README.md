# android-validator
[GOING] Android Common Validator using AspectJ , Annotation , Reflect
[进行中] 基于AspectJ,注解，反射开发的Android校验库


# Gradle 集成
进行中
# Maven 集成
进行中

# Function
在某些方法调用时检查参数的合法性并进行提示
目前提供的注解有：

|注解|说明|支持类型
|--|--|--|
|NotBlank |参数不可为空|String,Collection,以及有getText()/size()方法的任意类型
|Pattern(regex) |是否是指定的正则表达式|String,以及有getText()方法的任意类型
|Max(max) |是否<=max | int
|Min(min) |是否>=min | int
|Size(min,max) |长度是否在min-max之间 | String,Collection,以及有getText()/size()方法的任意类型
|Mobile |是否是一个手机号码 | String,以及有getText()方法的任意类型
|Email |是否是一个邮箱 | String,以及有getText()方法的任意类型

# 初始化

```Java
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Validator.init(
                ValidatorConfig
                        .newInstance()
                        .setApplication(this)
                        .setWarningProvider(new WarningProvider())  //自定义warning显示方式，如不提供默认以Log输出，建议自定义。
                        .setRuleProvider(new RuleProvider())        //自定义检查规则，如不提供默认使用默认检查方式，建议自定义。
        );
    }


    class WarningProvider implements IWarningProvider {
       ...
    }

    class RuleProvider implements IRuleProvider {
       ...
    }
}

```

# Sample

```Java
public class MainActivity extends AppCompatActivity {

    @Mobile(warning = "手机号不正确1")
    TextView mTV;

    @Mobile(warning = "手机号不正确2")
    String mText = "15022729132";

    @Email(warning = "邮箱格式不正确1")
    String mEmail;

    @NotBlank(warning = "这是空")
    @CustomeValidator
    TextView mEmailText;

    @Max(value = 10, warning = "超过了10")
    int Value = 20;


    @Pattern(value = "^[0-9]*$", warning = "正则格式不正确")
    String mPattern;

    @Size(min = 0, max = 2, warning = "list大小不正确")
    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Validator.inject(this);    //初始化部分映射关系
        
        test(8);
    }

    //将会检查这些成员变量的合法性，以及参数中带有已知注解的参数合法性
    @Validate({"mTV", "mText", "mEmail", "mEmailText", "mPattern", "Value"})
    public void test(@Min(value = 10, warning = "小于10") int i) {

    }
}
```

# 自定义规则检查
##提供自定义规则检查
##具体步骤

###1.自定义注解
```Java
@Documented
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(CustomRule.class)   //必须以Constraint 修饰自定义注解，并提供自定义检查规则类
public @interface CustomeValidator {
    String value() default "CustomeValidator";
}
```

###2. 自定义检查规则
```Java
public class CustomRule implements IRuleValidator<CustomeValidator> {   //需要继承IRuleValidator，并使用步骤一中定义的注解

    @Override
    public void initialize(CustomeValidator customeValidator, Object s) {  //初始化，自行实现
    }

    @Override
    public boolean isValid() {   //具体检查自行实现
        return true;
    }

    @Override
    public void showWarning() {   //错误提示自行实现

    }
}
```

###3.使用
与默认注解一致
