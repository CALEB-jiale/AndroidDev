package com.example.compteur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private int num;
    private Button myButton;
    private EditText myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("debug", "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        loadData();

//        if(savedInstanceState != null) {
//            Log.i("debug", "0");
//            num = savedInstanceState.getInt("num", 0);
//        }
    }

    @Override
    protected void onStart() {
        Log.i("debug", "start");
        super.onStart();

        update();
    }

    @Override
    protected void onDestroy() {
        Log.i("debug", "destroy");
        super.onDestroy();

        saveData();
    }

    private void update() {
        this.myText.setText("" +this.num);
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data.xml", Context.MODE_PRIVATE);

        num = myData.getInt("num", 0);
    }

    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.putInt("num", num);

        myEditor.apply();
    }

    public void clickButton(View v) {
        this.num++;
        this.myText.setText("" +this.num);
    }

    private void findViews() {
        this.myButton = findViewById(R.id.myButton);
        this.myText = findViewById(R.id.myText);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("debug", "saveInstance");
//        outState.putInt("num", num);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.i("debug", "restoreInstance");
        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState != null) {
//            Log.i("debug", "1");
//            num = savedInstanceState.getInt("num", 0);
//        }
//        this.myText.setText("" +this.num);
    }
}