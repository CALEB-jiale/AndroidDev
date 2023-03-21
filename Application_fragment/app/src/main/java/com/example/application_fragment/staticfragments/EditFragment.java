package com.example.application_fragment.staticfragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.application_fragment.R;
import com.google.android.material.snackbar.Snackbar;

public class EditFragment extends Fragment {

    private OnEditFragment myActivity;
    private Button buttonOK;
    private EditText inputText;
    private String text;
    private View rootView;

    public interface OnEditFragment {
        void setEditFragment(EditFragment fragment);
//        void setText(String text);
        void createDisplayFragment(String text);
//        void deleteEditFragment();
    }

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (OnEditFragment) getActivity();
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
        addListeners();

        if(savedInstanceState != null) {
            this.inputText.setText(savedInstanceState.getString("TEXT"));
        }
    }

    private void addListeners() {
        buttonOK.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    ActionOK(v);
                }
                return false;
            }
        });
    }

    private void findViews() {
        buttonOK = rootView.findViewById(R.id.button_ok);
        inputText = rootView.findViewById(R.id.input_text);
    }

    public void ActionOK(View v) {
        text = inputText.getText().toString();
//        myActivity.setText(text);
        showMessage(v);
//        myActivity.deleteEditFragment();
        myActivity.createDisplayFragment(text);
    }

    private void showMessage(View v) {
        Snackbar snackbar = Snackbar.make(v, this.text, Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TEXT", inputText.getText().toString());
    }
}