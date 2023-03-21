package fr.imt_atlantique.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//public class DateActivity extends AppCompatActivity implements DatePickerFragment.OnDateSet, DateFragment.OnDateFragment {
//    private String date;
//    private DateFragment dateFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_date);
//        Log.i("Lifecycle", "onCreate method");
//
//        if (savedInstanceState != null) {
//            if (!savedInstanceState.getBoolean("IS_CREATED")) {
//                createDateFragment();
//            }
//        } else {
//            createDateFragment();
//        }
//    }
//
//    public void createDateFragment() {
//        dateFragment = new DateFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction tx = fragmentManager.beginTransaction();
//        tx.add(R.id.linear_layout_activity_date, dateFragment);
//        tx.commit();
//    }
//
//    @Override
//    public void setDateFragment(DateFragment fragment) {
//        this.dateFragment = fragment;
//    }
//
//    @Override
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    @Override
//    public void finishActivity(Boolean hasResult) {
//        Intent myIntent = new Intent();
//
//        if (hasResult) {
//            myIntent.putExtra("DATE", this.date);
//            this.setResult(Activity.RESULT_OK, myIntent);
//        } else {
//            this.setResult(Activity.RESULT_CANCELED, myIntent);
//        }
//
//        this.finish();
//    }
//
//    @Override
//    public void onDateChanged(int year, int month, int dayOfMonth) {
//        this.date = "day/month/year: " + dayOfMonth + "/" + month + "/" + year;
//        dateFragment.setDate(this.date);
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("IS_CREATED", true);
//    }
//
//    @Override
//    public void showDatePickerDialog(View v) {
//        Log.i("debug", "showDatePickerDialog");
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }
//}
