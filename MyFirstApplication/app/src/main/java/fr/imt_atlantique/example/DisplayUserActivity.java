package fr.imt_atlantique.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DisplayUserActivity extends AppCompatActivity {
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewBirthday;
    private TextView textViewBirthCity;
    private TextView textViewDepartment;
    private LinearLayout layoutPhones;
    private User user;
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
        update();
    }

    private void loadData() {
        Log.i("debug", "loadData");
        this.loadDataFromIntent(getIntent());
    }

    private void loadDataFromIntent(Intent intent) {
        Log.i("debug", "loadDataFromIntent");

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.i("debug", "Intent = null");
        } else {
            Log.i("debug", "Intent is not null");
            this.user = extras.getParcelable("USER");
        }
    }

    private void update() {
        this.textViewFirstName.setText(this.user.getFirstName());
        this.textViewLastName.setText(this.user.getLastName());
        this.textViewBirthday.setText(this.user.getBirthday());
        this.textViewBirthCity.setText(this.user.getBirthCity());
        this.textViewDepartment.setText(this.user.getDepartment());
        updateLayoutPhone(this.user.getPhones());
    }

    private void updateLayoutPhone(List<String> phones) {
        layoutPhones.removeAllViews();
        int i = 1;
        for(String phone : phones) {
            View addPhone = addNewTelephone(layoutPhones);
            TextView textViewPhone = addPhone.findViewById(R.id.tv_display_phone);
            textViewPhone.setText(phone);
            TextView textViewNum = addPhone.findViewById(R.id.label_phone_num);
            textViewNum.setText(String.valueOf(i));
            i++;
        }
    }

    public View addNewTelephone(View v) {
        View addPhone = getLayoutInflater().inflate(R.layout.display_phone, null);
        layoutPhones.addView(addPhone);
        return addPhone;
    }

    private void addListeners() {
    }

    private void findViews() {
        textViewFirstName = findViewById(R.id.tv_display_name);
        textViewLastName = findViewById(R.id.tv_display_last_name);
        textViewBirthday = findViewById(R.id.tv_display_birthday);
        textViewBirthCity = findViewById(R.id.tv_display_birth_city);
        layoutPhones = findViewById(R.id.layout_phones);
        textViewDepartment = findViewById(R.id.tv_display_department);
    }

    public void editAction(View v) {
        Intent myIntent = new Intent();
        myIntent.putExtra("USER", this.user);
        this.setResult(RESULT_OK, myIntent);
        this.finish();
    }

    public void callAction(View v) {
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


}
