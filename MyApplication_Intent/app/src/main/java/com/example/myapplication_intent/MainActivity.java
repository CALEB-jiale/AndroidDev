package com.example.myapplication_intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private String question;
    private String answer;
    private EditText inputQuestion;
    private Button buttonAskQuestion;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Lifecycle", "onCreate method");

        init();
    }

    private void init() {
        initLauncher();
        findViews();
        addListeners();
        loadData();
    }

    private void initLauncher() {
        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        loadDataFromIntent(data);
                        showSnackbar(buttonAskQuestion);
                    }
                }
            }
        });
    }

    private void findViews() {
        this.buttonAskQuestion = findViewById(R.id.bt_ask_question);
        this.inputQuestion = findViewById(R.id.iv_question);
    }

    private void addListeners() {
    }

    private void update() {
        this.inputQuestion.setText(this.question);
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);

        this.question = myData.getString("Question", "");
    }

    private void loadDataFromIntent(Intent intent) {
        Log.i("debug", "loadDataFromIntent");

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.i("debug", "answer = null");
            this.answer = "";
        } else {
            Log.i("debug", "answer is not null");
            this.answer = extras.getString("ANSWER");
        }
    }

    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.putString("Question", this.question);

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

    public void pushButtonAskQuestion(View v) {
        this.question = this.inputQuestion.getText().toString();

        Intent intent = new Intent(this, AnswerActivity.class).putExtra("QUESTION", this.question);
        if (intent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(intent);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("debug", "return");
//        if(data) {
//            this.loadDataFromIntent(data);
//            showSnackbar(this.buttonAskQuestion);
//        }
//    }

    public void reset() {
        this.inputQuestion.setText("");
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