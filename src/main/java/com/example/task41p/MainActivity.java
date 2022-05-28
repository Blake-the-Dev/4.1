package com.example.task41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String saveTaskName;
    Chronometer countUp;
    String saveTime;
    private Long timeRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView timerStart = findViewById(R.id.btnStart);
        ImageView timerStop = findViewById(R.id.btnStop);
        ImageView timerSave = findViewById(R.id.btnSave);
        EditText getTaskName = findViewById(R.id.enterTaskName);
        TextView displayTaskName = findViewById(R.id.txtTaskName);
        TextView displaySavedTime = findViewById(R.id.txtSavedTime);


        if(savedInstanceState != null )
        {
            countUp = findViewById(R.id.txtTimer);
            timeRotate = savedInstanceState.getLong("resumeRotate");
            countUp.setBase(SystemClock.elapsedRealtime() - timeRotate);
            countUp.start();
        }

        SharedPreferences restoredData = getSharedPreferences("pastSessionData", MODE_PRIVATE);
        String restoredTaskName = restoredData.getString("oldTaskName", "");
        String restoredSavedTime = restoredData.getString("oldSaveTime", "");

        if (!restoredTaskName.isEmpty())
        {
            displayTaskName.setText(" " + restoredTaskName + " ");
        }

        if (!restoredSavedTime.isEmpty())
        {
            displaySavedTime.setText(restoredSavedTime);
        }


        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUp = findViewById(R.id.txtTimer);
                countUp.setBase(SystemClock.elapsedRealtime());
                countUp.start();
            }
        });

        timerStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUp.stop();
            }
        });

        timerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUp.stop();
                saveTaskName = getTaskName.getText().toString();
                saveTime = countUp.getText().toString();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        timeRotate = SystemClock.elapsedRealtime() - countUp.getBase();
        outState.putLong("resumeRotate", timeRotate);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences restoredData = getSharedPreferences("pastSessionData", MODE_PRIVATE);
        SharedPreferences.Editor oldData = restoredData.edit();
        EditText getTaskName = findViewById(R.id.enterTaskName);
        saveTime = countUp.getText().toString();

        oldData.putString("oldTaskName", getTaskName.getText().toString());
        oldData.putString("oldSaveTime", saveTime);

        oldData.commit();
    }
}