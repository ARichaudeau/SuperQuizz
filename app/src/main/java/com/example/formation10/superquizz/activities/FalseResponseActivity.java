package com.example.formation10.superquizz.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.formation10.superquizz.R;

public class FalseResponseActivity extends AppCompatActivity {

    private TextView textViewScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_false_response);

        textViewScreen = findViewById(R.id.textView_screen);
    }

}
