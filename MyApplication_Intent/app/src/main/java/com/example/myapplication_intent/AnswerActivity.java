package com.example.myapplication_intent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class AnswerActivity extends AppCompatActivity {
    private String question;
    private TextView textViewQuestion;
    private String answer;
    private EditText inputAnswer;
    private Button buttonAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Log.i("Lifecycle", "onCreate method");

        init();
    }

    private void init() {
        findViews();
        addListeners();
        loadData();
    }

    private void findViews() {
        this.textViewQuestion = findViewById(R.id.tv_question);
        this.inputAnswer = findViewById(R.id.iv_answer);
        this.buttonAnswer = findViewById(R.id.bt_answer);
    }

    private void addListeners() {
    }

    private void update() {
        this.textViewQuestion.setText(this.question);
        this.inputAnswer.setText(this.answer);
    }

    public void reset() {
    }

    private void loadData() {
        Log.i("debug", "loadData");
        this.loadDataFromIntent(getIntent());
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        this.answer = myData.getString("Answer", "");
    }

    private void loadDataFromIntent(Intent intent) {
        Log.i("debug", "loadDataFromIntent");

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.i("debug", "question = null");
            this.question = "";
        } else {
            Log.i("debug", "question is not null");
            this.question = extras.getString("QUESTION");
        }
    }

    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();
        myEditor.putString("Answer", this.answer);
        myEditor.apply();
    }

    private void showSnackbar(View v) {
        Snackbar snackbar = Snackbar.make(v, this.answer, Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void pushButtonAnswer(View v) {
        this.answer = this.inputAnswer.getText().toString();
        Intent myIntent = new Intent();
        myIntent.putExtra("ANSWER", this.answer);
        this.setResult(RESULT_OK, myIntent);
        this.finish();
    }

    // méthodes du cycle de vie
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "onStop method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "onResume method");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "onPause method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", "onStart method");

        update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");

        saveData();
    }
}
