package com.validator.demo;

import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jingyong.validator.Validator;
import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.MobileField;
import com.jingyong.validator.format.SizeParameter;

public class MainActivity extends AppCompatActivity {

    @MobileField
    TextView mTV;

    @MobileField(warning = "手机号不正确")
    String mText = "1502272914";

    @Override
    protected void onCreate(@Size Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Validator.inject(this);

        mTV = findViewById(R.id.tv_text);
        mTV.setText("13652020143");
        test("1", "2");
    }

    @Check
    @CheckField({"mTV", "mText", "AOS"})
    public void test(@SizeParameter(min = 1, max = 2) String s,
                     @SizeParameter(min = 1, max = 2) String s2) {

    }
}
