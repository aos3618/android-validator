package com.validator.demo;

import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jingyong.validator.Validator;
import com.jingyong.validator.ValidatorBuilder;
import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.field.CheckField;
import com.jingyong.validator.format.field.EmailField;
import com.jingyong.validator.format.field.MobileField;
import com.jingyong.validator.format.field.PatternField;
import com.jingyong.validator.format.field.SizeField;
import com.jingyong.validator.format.parameter.PatternParameter;
import com.jingyong.validator.format.parameter.SizeParameter;
import com.jingyong.validator.rule.IWarningProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @MobileField(warning = "手机号不正确2")
    TextView mTV;

    @MobileField(warning = "手机号不正确2")
    String mText = "15022729147";

    @EmailField(warning = "邮箱格式不正确1")
    String mEmail;

    @EmailField(warning = "邮箱格式不正确2")
    TextView mEmailText;

    @PatternField(value = "^[0-9]*$", warning = "正则格式不正确")
    String mPattern;

    @SizeField(min = 1, max = 2, warning = "list大小不正确")
    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Size Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ValidatorBuilder().setApplication(getApplication()).setWarningProvider(new WarningProvider()).build();

        Validator.inject(this);

        mEmailText = findViewById(R.id.tv_email);
        mTV = findViewById(R.id.tv_text);
        mTV.setText("13652020143");

        mEmail = "@";
        mPattern = "123";
        test("11", mList);
    }

    @Check
    @CheckField({"mTV", "mText", "mEmail", "mEmailText", "mPattern", "mList"})
    public void test(@SizeParameter(min = 1, max = 2, warning = "s 格式不正确")
                             String s,
//                     @PatternParameter(value = "^[0-9]*$", warning = "mEmail 格式不正确")
                     @SizeParameter(min = 1, max = 2, warning = "mList 格式不正确")
                             List mList) {

    }

    class WarningProvider implements IWarningProvider {

        @Override
        public void show(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
