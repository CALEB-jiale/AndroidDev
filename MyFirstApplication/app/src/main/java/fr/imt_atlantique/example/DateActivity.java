package fr.imt_atlantique.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class DateActivity extends AppCompatActivity implements DatePickerFragment.OnDateSet{
    private TextView textViewDate;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        Log.i("Lifecycle", "onCreate method");

        init();
    }

    private void init() {
        findViews();
        addListeners();
//        loadData();
//        update();
    }

//    private void loadData() {
//        Log.i("debug", "loadData");
//        this.loadDataFromIntent(getIntent());
//    }
//
//    private void loadDataFromIntent(Intent intent) {
//        Log.i("debug", "loadDataFromIntent");
//
//        Bundle extras = intent.getExtras();
//        if(extras == null) {
//            Log.i("debug", "Intent = null");
//        } else {
//            Log.i("debug", "Intent is not null");
//            this.date = extras.getString("DATE", "");
//        }
//    }

    private void addListeners() {
        textViewDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(v);
                }
                return false;
            }
        });
    }

    public void showDatePickerDialog(View v) {
        Log.i("debug", "showDatePickerDialog");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void findViews() {
        this.textViewDate = findViewById(R.id.textViewDate);
    }

//    private void update() {
//        this.textViewDate.setText(this.date);
//    }

    public void dateValidateAction(View v) {
        this.date = this.textViewDate.getText().toString();
        Intent myIntent = new Intent();
        myIntent.putExtra("DATE", this.date);
        this.setResult(Activity.RESULT_OK, myIntent);
        this.finish();
    }

    public void dateCancelAction(View v) {
        Intent myIntent = new Intent();
//        myIntent.putExtra("DATE", this.date);
        this.setResult(Activity.RESULT_CANCELED, myIntent);
        this.finish();
    }

    @Override
    public void onDateChanged(int year, int month, int dayOfMonth) {
        String myDate = "day/month/year: " + dayOfMonth + "/" + month + "/" + year;
        this.textViewDate.setText(myDate);
    }
}
