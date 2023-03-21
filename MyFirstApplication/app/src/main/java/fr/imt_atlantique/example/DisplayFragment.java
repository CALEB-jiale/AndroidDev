package fr.imt_atlantique.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DisplayFragment extends Fragment {
    private View rootView;
    private OnDisplayFragment myActivity;
    private TextView textViewLastName;
    private TextView textViewFirstName;
    private TextView textViewBirthday;
    private TextView textViewBirthCity;
    private TextView textViewDepartment;
    private Button buttonEdit;
    private LinearLayout layoutPhones;
    private User user;
    private static final String ARG_USER = "USER";

    public interface OnDisplayFragment {
        void setDisplayFragment(DisplayFragment fragment);
        void closeDisplayFragment();
        void actionCall(View v);
    }

    public static DisplayFragment newInstance(User user) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity.setDisplayFragment(this);
        if (getArguments() != null) {
            this.user = getArguments().getParcelable("USER");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_display, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        addListeners();
        update();
    }

    private void findViews() {
        this.buttonEdit = rootView.findViewById(R.id.button_edit_fragment_display);
        this.textViewFirstName = rootView.findViewById(R.id.tv_display_name);
        this.textViewLastName = rootView.findViewById(R.id.tv_display_last_name);
        this.textViewBirthday = rootView.findViewById(R.id.tv_display_birthday);
        this.textViewBirthCity = rootView.findViewById(R.id.tv_display_birth_city);
        this.textViewDepartment = rootView.findViewById(R.id.tv_display_department);
        this.layoutPhones = rootView.findViewById(R.id.layout_phones_fragment_display);
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

    private View addNewTelephone(View v) {
        View addPhone = getLayoutInflater().inflate(R.layout.display_phone, null);
        layoutPhones.addView(addPhone);
        return addPhone;
    }

    private void addListeners() {
        buttonEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionEdit(v);
                }
                return false;
            }
        });
    }

    public void actionEdit(View v) {
        myActivity.closeDisplayFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (OnDisplayFragment) getActivity();
    }

    public DisplayFragment() {
        // Required empty public constructor
    }
}