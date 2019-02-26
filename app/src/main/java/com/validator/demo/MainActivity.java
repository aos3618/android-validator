package com.validator.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jingyong.validator.Validator;
import com.jingyong.validator.ValidatorBuilder;
import com.jingyong.validator.format.Check;
import com.jingyong.validator.format.CheckField;
import com.jingyong.validator.format.Email;
import com.jingyong.validator.format.Mobile;
import com.jingyong.validator.format.base.Pattern;
import com.jingyong.validator.format.base.Size;
import com.jingyong.validator.rule.IWarningProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Mobile(warning = "手机号不正确1")
    TextView mTV;

    @Mobile(warning = "手机号不正确2")
    String mText = "15022729147";

    @Email(warning = "邮箱格式不正确1")
    String mEmail;

    @Email(warning = "邮箱格式不正确2")
    TextView mEmailText;

    @Pattern(value = "^[0-9]*$", warning = "正则格式不正确")
    String mPattern;

    @Size(min = 1, max = 2, warning = "list大小不正确")
    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(@android.support.annotation.Size Bundle savedInstanceState) {
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
    public void test(@Size(min = 1, max = 2, warning = "s 格式不正确")
                             String s,
//                     @Pattern(value = "^[0-9]*$", warning = "mEmail 格式不正确")
                     @Size(min = 1, max = 2, warning = "mList 格式不正确")
                             List mList) {

    }

    class WarningProvider implements IWarningProvider {

        @Override
        public void show(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
