package com.example.application_fragment.staticfragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application_fragment.R;

public class DisplayFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private TextView textView;
    private View rootView;
    private OnDisplayFragment myActivity;

    private String text;

    public interface OnDisplayFragment {
        void setDisplayFragment(DisplayFragment fragment);
    }

    public DisplayFragment() {
        // Required empty public constructor
    }

    public static DisplayFragment newInstance(String text) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity = (OnDisplayFragment) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myActivity.setDisplayFragment(this);

        if (getArguments() != null) {
            text = getArguments().getString(ARG_TEXT);
        }

        if (savedInstanceState != null) {
            text = savedInstanceState.getString("TEXT");
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
        setText(text);
    }

    private void addListeners() {
    }

    private void findViews() {
        textView = rootView.findViewById(R.id.text_view);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TEXT", text);
    }
}