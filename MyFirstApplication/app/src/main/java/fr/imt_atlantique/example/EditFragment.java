package fr.imt_atlantique.example;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EditFragment extends Fragment {
    private static final String ARG_USER = "user";
    private User user;
    private OnEditFragment myActivity;
    private View rootView;
    private Button buttonValidate;
    private Button buttonAddPhone;
    private Spinner spinnerDepartment;
    private EditText textInputLastName;
    private EditText textInputFirstName;
    private EditText textInputBirthday;
    private EditText textInputBirthCity;
    private LinearLayout layoutPhones;
    private Toolbar toolbar;

    public interface OnEditFragment {
        void setEditFragment(EditFragment fragment);
        void createDisplayFragment();
        void createDateFragment();
        void setUser(User user);
    }

    public static EditFragment newInstance(User user) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity.setEditFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        this.toolbar.inflateMenu(R.menu.menu_main);
        addListeners();

        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
            update();
        }

        if (savedInstanceState != null) {
            this.user = savedInstanceState.getParcelable("USER");
            update();
        }
    }

    private void findViews() {
        this.textInputFirstName = rootView.findViewById(R.id.ti_name);
        this.textInputLastName = rootView.findViewById(R.id.ti_last_name);
        this.textInputBirthday = rootView.findViewById(R.id.ti_birthday);
        this.textInputBirthCity = rootView.findViewById(R.id.ti_city);
        this.spinnerDepartment = rootView.findViewById(R.id.spinner_department);
        this.layoutPhones = rootView.findViewById(R.id.layout_phones);
        this.buttonAddPhone = rootView.findViewById(R.id.button_add_phone);
        this.buttonValidate = rootView.findViewById(R.id.button_validate_edit_fragment);
        this.toolbar = (Toolbar) rootView.findViewById(R.id.myToolbar);
    }

    private void addListeners() {
        textInputBirthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    createDateFragment(v);
                }
                return false;
            }
        });

        buttonAddPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    addNewTelephone(v);
                }
                return false;
            }
        });

        buttonValidate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionValidate(v);
                }
                return false;
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.resetAction:
                    resetAction(item);
                    return true;
                case R.id.find_city_info:
                    findCityInfo(item);
                    return true;
                case R.id.share_city_info:
                    shareCityInfo(item);
                    return true;
                default:
                    return false;
            }
        });
    }

    public void findCityInfo(MenuItem item) {
        String url = "http://fr.wikipedia.org/?search=" + this.user.getBirthCity();

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void shareCityInfo(MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, this.user.getBirthCity());
        intent.setType("text/plain");

        String title = getResources().getString(R.string.chooser_title_share_photo);;
        Intent chooser = Intent.createChooser(intent, title);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void resetAction(MenuItem item) {
        List<String> phones = new ArrayList<>();
        this.user = new User("", "", "", "", "", phones);
        update();
    }

    private void createDateFragment(View v) {
        myActivity.createDateFragment();
    }

    //méthode de gestion téléphone
    public View addNewTelephone(View v) {
        View addPhone = getLayoutInflater().inflate(R.layout.input_phone, null);
        layoutPhones.addView(addPhone);
        return addPhone;
    }

    public void actionValidate(View v) {
        updateUser();
        myActivity.setUser(this.user);
        showMessage(v);
        myActivity.createDisplayFragment();
    }

    private void showMessage(View v) {
        Snackbar snackbar = Snackbar.make(v, this.user.getLastName() + " " + this.user.getFirstName() + " " + this.user.getBirthday() + " " + this.user.getBirthCity(), Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void updateUser() {
        String firstName = this.textInputFirstName.getText().toString();
        String lastName = this.textInputLastName.getText().toString();
        String birthday = this.textInputBirthday.getText().toString();
        String birthCity = this.textInputBirthCity.getText().toString();
        String department = this.spinnerDepartment.getSelectedItem().toString();
        List<String> phones = getPhones();
        this.user = new User(firstName, lastName, birthday, birthCity, department, phones);
    }

    private List<String> getPhones() {
        List<String> phones = new ArrayList<>();
        Log.i("debug", String.valueOf(layoutPhones.getChildCount()));
        for(int index = 0; index < layoutPhones.getChildCount(); index++) {
            View child = layoutPhones.getChildAt(index);
            EditText textInputPhoneNum = child.findViewById(R.id.ti_phone_num);
            phones.add(textInputPhoneNum.getText().toString());
        }
        return phones;
    }

    private void update() {
        textInputLastName.setText(this.user.getLastName());
        textInputFirstName.setText(this.user.getFirstName());
        textInputBirthday.setText(this.user.getBirthday());
        textInputBirthCity.setText(this.user.getBirthCity());
        setSpinnerDepartment();
        updateLayoutPhone();
    }

    private void setSpinnerDepartment() {
        for (int i = 0; i< spinnerDepartment.getCount(); i++) {
            if (spinnerDepartment.getItemAtPosition(i).toString().equalsIgnoreCase(this.user.getDepartment())){
                spinnerDepartment.setSelection(i);
                return;
            }
        }
    }

    private void updateLayoutPhone() {
        layoutPhones.removeAllViews();

        for(String phone : this.user.getPhones()) {
            View addPhone = addNewTelephone(layoutPhones);
            EditText textInputPhoneNum = addPhone.findViewById(R.id.ti_phone_num);
            textInputPhoneNum.setText(phone);
        }
    }

    public void setDate(String date) {
        this.textInputBirthday.setText(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateUser();
        outState.putParcelable("USER", this.user);
    }

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (OnEditFragment) getActivity();
    }

}