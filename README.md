# android-validator
[GOING] Android Common Validator using AspectJ , Annotation , Reflect

[进行中] 基于AspectJ,注解，反射开发的Android校验库


# Gradle 集成
进行中
# Maven 集成
进行中

# Function
在某些方法调用时检查参数的合法性并进行提示
目前提供的注解有(持续加入中)：

|注解|说明|支持类型
|--|--|--|
|NotBlank |不可为空|String,Collection,以及有getText()/size()方法的任意类型
|NotNull |不可为Null|任意类型
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
                        .setWarningProvider(new WarningProvider())  //custom how to show warning, default is Log
                        .setRuleProvider(new RuleProvider())        //custom the check rule
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

    //validate if mTV is a mobile when @Validate method invoked
    @Mobile(warning = "wrong mobile number 1")
    TextView mTV;

    @Mobile(warning = "wrong mobile number 2")
    String mText = "15022729132";

    //validate if mEmail is a email when @Validate method invoked
    @Email(warning = "wrong email 1")
    String mEmail;

    //validate if mEmailText is not empty when @Validate method invoked
    @NotBlank(warning = "a empty field")
    @CustomeValidator
    TextView mEmailText;

    //validate if mValue is less than 10 when @Validate method invoked
    @Max(value = 10, warning = "need less than 10")
    int mValue = 8;

    //validate if mPattern matched the regex when @Validate method invoked
    @Pattern(value = "^[0-9]*$", warning = "not matched regex")
    String mPattern;

    //validate if mList size is 0-2 when @Validate method invoked
    @Size(min = 0, max = 2, warning = "wrong list size")
    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Validator.inject(this);

        mEmailText = findViewById(R.id.tv_email);
        mEmailText.setText("123");
        mTV = findViewById(R.id.tv_text);
        mTV.setText("13651234143");
        mEmail = "@";
        mPattern = "123";

        findViewById(R.id.tv_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate3(mList);
            }
        });

        mTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate1("11", 8);
            }
        });

        mEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate2("123");
            }
        });
    }

    //validate the field :mTV,mText;
    //validate the parameter : s , i
    @Validate({"mTV", "mText"})
    public void validate1(@CustomeValidator String s,
                          @Min(value = 10, warning = "need more than 10") int i) {

    }

    //validate the field :mEmail,mEmailText;
    //validate the parameter : pattern
    @Validate({"mEmail", "mEmailText"})
    public void validate2(@Pattern(value = "^[0-9]*$", warning = "not matched regex") String pattern) {

    }

    //validate the field :mPattern,mValue;
    //validate the parameter : list
    @Validate({"mPattern", "mValue"})
    public void validate3(@Size(min = 1, max = 2, warning = "wrong list size") List list) {

    }
}
```

# 自定义规则检查
### 提供自定义规则检查
### 具体步骤

#### 1.自定义注解
```Java
@Documented
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(CustomRule.class)   //Must have a Constraint Annotation，and define a check rule
public @interface CustomeValidator {
    String value() default "CustomeValidator";
}
```

#### 2. 自定义检查规则
```Java
public class CustomRule implements IRuleValidator<CustomeValidator> {   //need implement IRuleValidator and step1 Annotation as generics

    @Override
    public void initialize(CustomeValidator customeValidator, Object s) {  //init
    }

    @Override
    public boolean isValid() {   // how to valid the rule
        return true;
    }

    @Override
    public void showWarning() {   //how to show warning

    }
}
```

#### 3.使用
与默认注解一致
