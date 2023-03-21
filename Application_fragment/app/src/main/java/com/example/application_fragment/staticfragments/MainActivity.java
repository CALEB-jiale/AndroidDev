package com.example.application_fragment.staticfragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.application_fragment.R;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements DisplayFragment.OnDisplayFragment, EditFragment.OnEditFragment {
    private EditFragment editFragment;
    private DisplayFragment displayFragment;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (!savedInstanceState.getBoolean("IS_CREATED")) {
                createEditFragment();
            }
        } else {
            createEditFragment();
        }
    }

    public void createEditFragment() {
        editFragment = new EditFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.add(R.id.linear_layout, editFragment);
//        tx.addToBackStack(null);
        tx.commit();
    }

//    @Override
//    public void deleteEditFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction tx = fragmentManager.beginTransaction();
//        tx.remove(editFragment);
//        tx.commit();
//    }

    @Override
    public void createDisplayFragment(String text) {
        displayFragment = DisplayFragment.newInstance(text);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.hide(editFragment);
        tx.add(R.id.linear_layout, displayFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

//    @Override
//    public void setText(String text) {
//        this.text = text;
//        this.displayFragment.setText(text);
//    }

    @Override
    public void setEditFragment(EditFragment fragment) {
        this.editFragment = fragment;
    }

    @Override
    public void setDisplayFragment(DisplayFragment fragment) {
        this.displayFragment = fragment;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_CREATED", true);
    }
}