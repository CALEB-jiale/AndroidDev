package fr.imt_atlantique.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private String lastName;
    private String firstName;
    private String birthday;
    private String birthCity;

    private String departement;
    private HashSet<String> phones;
    private Spinner spinnerDepartement;
    private EditText textInputLastName;
    private EditText textInputFirstName;
    private EditText textInputBirthday;
    private EditText textInputBirthCity;
    private LinearLayout layoutPhones;

    // méthodes de création de la fenêtre
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Lifecycle", "onCreate method");

        init();
    }

    private void init() {
        findViews();
        addListeners();
        loadData();
    }

    private void findViews() {
        textInputLastName = findViewById(R.id.ti_nom);
        textInputFirstName = findViewById(R.id.ti_prenom);
        textInputBirthday = findViewById(R.id.ti_date_naissance);
        textInputBirthCity = findViewById(R.id.ti_ville_naissance);
        layoutPhones = findViewById(R.id.layout_phones);
        spinnerDepartement = findViewById(R.id.departements_spinner);
    }

    private void addListeners() {
        textInputBirthday.setOnTouchListener(new View.OnTouchListener() {
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
        DialogFragment newFragment = new DatePickerFragment(textInputBirthday);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void update() {
        textInputLastName.setText(this.lastName);
        textInputFirstName.setText(this.firstName);
        textInputBirthday.setText(this.birthday);
        textInputBirthCity.setText(this.birthCity);
        setSpinnerDepartement();
        updateLayoutPhone();
    }

    private void setSpinnerDepartement() {
        for (int i=0; i<spinnerDepartement.getCount(); i++) {
            if (spinnerDepartement.getItemAtPosition(i).toString().equalsIgnoreCase(this.departement)){
                spinnerDepartement.setSelection(i);
                return;
            }
        }
    }

    // méthodes de manipulation des buttons

    public void resetAction(MenuItem item) {
        this.lastName = "";
        this.firstName = "";
        this.birthday = "";
        this.birthCity = "";
        this.departement = "";
        this.phones.clear();
        textInputLastName.setText("");
        textInputFirstName.setText("");
        textInputBirthday.setText("");
        textInputBirthCity.setText("");
        layoutPhones.removeAllViews();
    }

    public void validateAction (View v) {
        this.lastName = textInputLastName.getText().toString();
        this.firstName = textInputFirstName.getText().toString();
        this.birthday = textInputBirthday.getText().toString();
        this.birthCity = textInputBirthCity.getText().toString();
        this.departement = spinnerDepartement.getSelectedItem().toString();
        updatePhones();
        this.showMessage(v);
        saveData();
    }

    private void showMessage(View v) {
        Snackbar snackbar = Snackbar.make(v, this.lastName + " " + this.firstName + " " + this.birthday + " " + this.birthCity + "Num: " + this.phones.size(), Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    //méthode de gestion téléphone
    public View AddNewTelephone(View v) {
        View addPhone = getLayoutInflater().inflate(R.layout.input_phone, null);
        layoutPhones.addView(addPhone);
        return addPhone;
    }

    public void DeletePhone(View v) {
        RelativeLayout rl = (RelativeLayout) v.getParent();
        ViewGroup parentView = (ViewGroup) rl.getParent();
        parentView.removeView(rl);
    }

    private void updatePhones() {
        this.phones.clear();
        Log.i("debug", String.valueOf(layoutPhones.getChildCount()));
        for(int index = 0; index < layoutPhones.getChildCount(); index++) {
            View child = layoutPhones.getChildAt(index);
            EditText textInputPhoneNum = child.findViewById(R.id.ti_phone_num);
            this.phones.add(textInputPhoneNum.getText().toString());
        }
    }

    private void updateLayoutPhone() {
        layoutPhones.removeAllViews();
        for(String phone : this.phones) {
            View addPhone = AddNewTelephone(layoutPhones);
            EditText textInputPhoneNum = addPhone.findViewById(R.id.ti_phone_num);
            textInputPhoneNum.setText(phone);
        }
    }

    // méthodes de gestion des données
    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.putString("FirstName", this.firstName);
        myEditor.putString("LastName", this.lastName);
        myEditor.putString("Birthday", this.birthday);
        myEditor.putString("BirthCity", this.birthCity);
        myEditor.putString("Departement", this.departement);
        myEditor.putStringSet("Phones", this.phones);

        myEditor.apply();
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);

        this.firstName = myData.getString("FirstName", "");
        this.lastName = myData.getString("LastName", "");
        this.birthday = myData.getString("Birthday", "");
        this.birthCity = myData.getString("BirthCity", "");
        this.departement = myData.getString("Departement", "");
        this.phones = (HashSet<String>) myData.getStringSet("Phones", new HashSet<>());
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