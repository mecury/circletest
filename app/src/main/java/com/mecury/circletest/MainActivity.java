package com.mecury.circletest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mecury.circlelibrary.CircleBackground;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private CircleBackground circleBackground;
    private boolean isRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        circleBackground = (CircleBackground) findViewById(R.id.circle);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    isRunning = false;
                }else{
                    isRunning = true;
                }

                if (isRunning){
                    circleBackground.startAnimation();
                }else{
                    circleBackground.endAnimation();
                }

            }
        });
    }
}
