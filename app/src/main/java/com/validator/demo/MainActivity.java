package com.validator.demo;

import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jingyong.validator.Validator;
import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.EmailField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.format.PatternField;
import com.jingyong.validator.format.PatternParameter;
import com.jingyong.validator.format.SizeParameter;

public class MainActivity extends AppCompatActivity {

    @MobileField
    TextView mTV;

    @MobileField(warning = "手机号不正确")
    String mText = "15022729147";

    @EmailField(warning = "邮箱格式不正确1")
    String mEmail;

    @EmailField(warning = "邮箱格式不正确2")
    TextView mEmailText;

    @PatternField(value = "^[0-9]*$", warning = "正则格式不正确")
    String mPattern;

    @Override
    protected void onCreate(@Size Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Validator.inject(this);

        mEmailText = findViewById(R.id.tv_email);
        mTV = findViewById(R.id.tv_text);
        mTV.setText("13652020143");

        mEmail = "@";
        mPattern = "12a3";
        test("1231", mEmailText);
    }

    @Check
    @CheckField({"mTV", "mText", "mEmail", "mEmailText", "mPattern"})
    public void test(@SizeParameter(min = 1, max = 2) String s,
                     @PatternParameter("^[0-9]*$")
                     @SizeParameter(min = 1, max = 2) TextView mEmail) {

    }
}
