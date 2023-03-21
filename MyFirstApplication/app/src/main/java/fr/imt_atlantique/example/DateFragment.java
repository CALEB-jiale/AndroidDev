package fr.imt_atlantique.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DateFragment extends Fragment {
    private View rootView;
    private OnDateFragment myActivity;
    private TextView textView;
    private Button buttonValidate;
    private Button buttonCancel;

    public interface OnDateFragment {
        void setDateFragment(DateFragment fragment);
        void showDatePickerDialog(View v);
        void setDate(String date);
        void closeDateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity.setDateFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_date, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        addListeners();

        if(savedInstanceState != null) {
            this.textView.setText(savedInstanceState.getString("DATE"));
        }
    }

    private void addListeners() {
        buttonValidate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionValidate(v);
                }
                return false;
            }
        });

        buttonCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionCancel(v);
                }
                return false;
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
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
        myActivity.showDatePickerDialog(v);
    }

    private void actionValidate(View v) {
        myActivity.setDate(this.textView.getText().toString());
        myActivity.closeDateFragment();
    }

    private void actionCancel(View v) {
        myActivity.closeDateFragment();
    }

    private void findViews() {
        this.textView = rootView.findViewById(R.id.text_view_date_fragment);
        this.buttonValidate = rootView.findViewById(R.id.button_validate_date_fragment);
        this.buttonCancel = rootView.findViewById(R.id.button_cancel_date_fragment);
    }

    public void setDate(String date) {
        textView.setText(date);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DATE", this.textView.getText().toString());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (OnDateFragment) getActivity();
    }

    public DateFragment() {
        // Required empty public constructor
    }
}