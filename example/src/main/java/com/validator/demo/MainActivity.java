package com.validator.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.validator.Validator;
import com.validator.format.Validate;
import com.validator.format.Email;
import com.validator.format.Mobile;
import com.validator.format.base.Max;
import com.validator.format.base.Min;
import com.validator.format.base.NotBlank;
import com.validator.format.base.NotNull;
import com.validator.format.base.Pattern;
import com.validator.format.base.Size;

import java.util.ArrayList;
import java.util.List;

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

    @NotNull(warning = "should not null")
    String mString;

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
        mList.add("list");
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
    @Validate({"mPattern", "mValue", "mString"})
    public void validate3(@Size(min = 1, max = 2, warning = "wrong list size") List list) {

    }
}
