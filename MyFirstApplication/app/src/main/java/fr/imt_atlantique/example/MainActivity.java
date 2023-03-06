package fr.imt_atlantique.example;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String lastName;
    private String firstName;
    private String birthday;
    private String birthCity;

    private String department;
    private List<String> phones;
    private Spinner spinnerDepartment;
    private EditText textInputLastName;
    private EditText textInputFirstName;
    private EditText textInputBirthday;
    private EditText textInputBirthCity;
    private LinearLayout layoutPhones;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<Intent> activityDateLauncher;

    // méthodes de création de la fenêtre
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
                    }
                }
            }
        });

        this.activityDateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.i("debug", "RESULT_OK");
                    Intent data = result.getData();
                    if (data != null) {
                        loadDateFromIntent(data);
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Log.i("debug", "RESULT_CANCELED");
                }
            }
        });
    }

    private void loadDateFromIntent(Intent intent) {
        Log.i("debug", "loadDateFromIntent");

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.i("debug", "answer = null");
        } else {
            Log.i("debug", "answer is not null");
            String date = extras.getString("DATE");
            textInputBirthday.setText(date);
        }
    }

    private void loadDataFromIntent(Intent intent) {
        Log.i("debug", "loadDataFromIntent");

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.i("debug", "answer = null");
        } else {
            Log.i("debug", "answer is not null");
            User user = extras.getParcelable("USER");
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.birthCity = user.getBirthCity();
            this.birthday = user.getBirthday();
            this.department = user.getDepartment();
            this.phones = user.getPhones();
            update();
        }
    }

    private void findViews() {
        textInputLastName = findViewById(R.id.ti_nom);
        textInputFirstName = findViewById(R.id.ti_prenom);
        textInputBirthday = findViewById(R.id.ti_date_naissance);
        textInputBirthCity = findViewById(R.id.ti_ville_naissance);
        layoutPhones = findViewById(R.id.layout_phones);
        spinnerDepartment = findViewById(R.id.departements_spinner);
    }

    private void addListeners() {
        textInputBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    goToDateActivity();
                }
                return false;
            }
        });
    }

    private void goToDateActivity() {
//        Intent intent = new Intent(this, DateActivity.class).putExtra("DATE", this.textInputBirthday.getText().toString());
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            activityDateLauncher.launch(intent);
//        }

        Intent intent = new Intent(Intent.ACTION_PICK);

        String title = getResources().getString(R.string.chooser_title_pick_date);;
        Intent chooser = Intent.createChooser(intent, title);

        activityDateLauncher.launch(chooser);
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
        setSpinnerDepartment();
        updateLayoutPhone();
    }

    private void setSpinnerDepartment() {
        for (int i = 0; i< spinnerDepartment.getCount(); i++) {
            if (spinnerDepartment.getItemAtPosition(i).toString().equalsIgnoreCase(this.department)){
                spinnerDepartment.setSelection(i);
                return;
            }
        }
    }

    // méthodes de manipulation des buttons

    public void findCityInfo(MenuItem item) {
        String url = "http://fr.wikipedia.org/?search=" + this.birthCity;

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void shareCityInfo(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, this.birthCity);
        intent.setType("text/plain");

        String title = getResources().getString(R.string.chooser_title_share_photo);;
        Intent chooser = Intent.createChooser(intent, title);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void resetAction(MenuItem item) {
        this.lastName = "";
        this.firstName = "";
        this.birthday = "";
        this.birthCity = "";
        this.department = "";
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
        this.department = spinnerDepartment.getSelectedItem().toString();
        updatePhones();
        this.showMessage(v);
        saveData();

        User user = new User(this.firstName, this.lastName, this.birthday, this.birthCity, this.department, this.phones);
        Intent intent = new Intent(this, DisplayUserActivity.class).putExtra("USER", user);
        if (intent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(intent);
        }
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
    public View addNewTelephone(View v) {
        View addPhone = getLayoutInflater().inflate(R.layout.input_phone, null);
        layoutPhones.addView(addPhone);
        return addPhone;
    }

    public void deletePhone(View v) {
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
            View addPhone = addNewTelephone(layoutPhones);
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
        myEditor.putString("Department", this.department);
        Set<String> phoneSet = new HashSet<>(this.phones);
        myEditor.putStringSet("Phones", phoneSet);

        myEditor.apply();
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);

        this.firstName = myData.getString("FirstName", "");
        this.lastName = myData.getString("LastName", "");
        this.birthday = myData.getString("Birthday", "");
        this.birthCity = myData.getString("BirthCity", "");
        this.department = myData.getString("Department", "");
        Set<String> phoneSet = myData.getStringSet("Phones", new HashSet<>());
        this.phones = new ArrayList<>();
        this.phones.addAll(phoneSet);

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