package fr.imt_atlantique.example;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements EditFragment.OnEditFragment, DatePickerFragment.OnDateSet, DateFragment.OnDateFragment, DisplayFragment.OnDisplayFragment {

    private EditFragment editFragment;
    private DisplayFragment displayFragment;
    private DateFragment dateFragment;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Lifecycle", "onCreate method");

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        loadData();

        if (savedInstanceState != null) {
            if (!savedInstanceState.getBoolean("IS_CREATED")) {
                createEditFragment();
            }
        } else {
            createEditFragment();
        }
    }

    public void createEditFragment() {
        editFragment = EditFragment.newInstance(this.user);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
//        tx.hide(editFragment);
        tx.add(R.id.linear_layout_activity_main, editFragment);
//        tx.addToBackStack(null);
        tx.commit();
    }

    // méthodes de gestion des données
    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.putString("FirstName", this.user.getFirstName());
        myEditor.putString("LastName", this.user.getLastName());
        myEditor.putString("Birthday", this.user.getBirthday());
        myEditor.putString("BirthCity", this.user.getBirthCity());
        myEditor.putString("Department", this.user.getDepartment());
        Set<String> phoneSet = new HashSet<>(this.user.getPhones());
        myEditor.putStringSet("Phones", phoneSet);

        myEditor.apply();
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);

        String firstName = myData.getString("FirstName", "");
        String lastName = myData.getString("LastName", "");
        String birthday = myData.getString("Birthday", "");
        String birthCity = myData.getString("BirthCity", "");
        String department = myData.getString("Department", "");
        Set<String> phoneSet = myData.getStringSet("Phones", new HashSet<>());
        List<String> phones = new ArrayList<>();
        phones.addAll(phoneSet);

        this.user = new User(firstName, lastName, birthday, birthCity, department, phones);
    }

    public void deletePhone(View v) {
        RelativeLayout rl = (RelativeLayout) v.getParent();
        ViewGroup parentView = (ViewGroup) rl.getParent();
        parentView.removeView(rl);
    }

    @Override
    public void setEditFragment(EditFragment fragment) {
        this.editFragment = fragment;
    }

    @Override
    public void createDisplayFragment() {
        displayFragment = DisplayFragment.newInstance(this.user);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.hide(editFragment);
        tx.add(R.id.linear_layout_activity_main, displayFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void createDateFragment() {
        dateFragment = new DateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.hide(editFragment);
        tx.add(R.id.linear_layout_activity_main, dateFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setDateFragment(DateFragment fragment) {
        this.dateFragment = fragment;
    }

    @Override
    public void showDatePickerDialog(View v) {
        Log.i("debug", "showDatePickerDialog");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void setDate(String date) {
        this.editFragment.setDate(date);
    }

    @Override
    public void closeDateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.remove(dateFragment);
        tx.show(editFragment);
        tx.commit();
    }

    @Override
    public void onDateChanged(int year, int month, int dayOfMonth) {
        dateFragment.setDate("day/month/year: " + dayOfMonth + "/" + month + "/" + year);
    }

    @Override
    public void setDisplayFragment(DisplayFragment fragment) {
        this.displayFragment = fragment;
    }

    @Override
    public void closeDisplayFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.remove(displayFragment);
        tx.show(editFragment);
        tx.commit();
    }

    @Override
    public void actionCall(View v) {
        RelativeLayout rl = (RelativeLayout) v.getParent();
        TextView textViewNum = rl.findViewById(R.id.tv_display_phone);
        String number = textViewNum.getText().toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));

        String title = getResources().getString(R.string.chooser_title_share_photo);;
        Intent chooser = Intent.createChooser(intent, title);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
        saveData();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_CREATED", true);
    }
}