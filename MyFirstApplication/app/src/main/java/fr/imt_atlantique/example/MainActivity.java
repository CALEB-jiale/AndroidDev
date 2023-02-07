package fr.imt_atlantique.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private String nom;
    private String prenom;
    private String dateNaissance;
    private String villeNaissance;
    private EditText textInputNom;
    private EditText textInputPrenom;
    private EditText textInputDateNaissance;
    private EditText textInputVilleNaissance;
    private LinearLayout layoutPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Lifecycle", "onCreate method");
        findViews();
        addListeners();
    }

    private void findViews() {
        textInputNom = findViewById(R.id.ti_nom);
        textInputPrenom = findViewById(R.id.ti_prenom);
        textInputDateNaissance = findViewById(R.id.ti_date_naissance);
        textInputVilleNaissance = findViewById(R.id.ti_ville_naissance);
        layoutPhones = findViewById(R.id.layout_phones);
    }
    public EditText getTextInputDateNaissance() {
        return this.textInputDateNaissance;
    }

    private void addListeners() {
        textInputDateNaissance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(v);
                }
                return false;
            }
        });
    }

    public void resetAction(MenuItem item) {
        this.nom = "";
        this.prenom = "";
        this.dateNaissance = "";
        this.villeNaissance = "";
        textInputNom.setText("");
        textInputPrenom.setText("");
        textInputDateNaissance.setText("");
        textInputVilleNaissance.setText("");
        layoutPhones.removeAllViews();
    }

    public void validateAction (View v) {
        this.nom = textInputNom.getText().toString();
        this.prenom = textInputPrenom.getText().toString();
        this.dateNaissance = textInputDateNaissance.getText().toString();
        this.villeNaissance = textInputVilleNaissance.getText().toString();
        this.showMessage(v);
    }

    public void showMessage(View v) {
        Snackbar.make(v, this.nom + " " + this.prenom + " " + this.dateNaissance + " " + this.villeNaissance, Snackbar.LENGTH_LONG).show();
    }

    public void AddNewTelephone(View v) {
        View add_phone = getLayoutInflater().inflate(R.layout.input_phone, null);
        layoutPhones.addView(add_phone);
    }

    public void DeletePhone(View v) {
        RelativeLayout rl = (RelativeLayout) v.getParent();
        rl.removeAllViews();
    }

    public void showDatePickerDialog(View v) {
        Log.i("debug", "test");
        DialogFragment newFragment = new DatePickerFragment(textInputDateNaissance);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
    }
}