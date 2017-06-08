package com.yehu.edittextinputdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.yehu.edittextinputdemo.Utils.ViewInputUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInputUtils.setOnContentTextCompleteListener((EditText) findViewById(R.id.edit_input), 5, 2, null);
    }
}
