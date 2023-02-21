package fr.imt_atlantique.example;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Log.i("Lifecycle", "onCreate method");

        init();
    }

    private void init() {
        findViews();
        addListeners();
        loadData();
    }

    private void loadData() {

    }

    private void addListeners() {

    }

    private void findViews() {

    }


}
