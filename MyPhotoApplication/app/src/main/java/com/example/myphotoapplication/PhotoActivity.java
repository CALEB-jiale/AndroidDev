package com.example.myphotoapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap imageBitmap;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        findView();
        initLauncher();

        if (savedInstanceState != null) {
            this.imageBitmap = savedInstanceState.getParcelable("IMAGE");
            imageView.setImageBitmap(imageBitmap);
        } else {
            takePhoto();
        }
    }

    private void initLauncher() {
        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                }
                if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    returnWithNoPhoto();
                }
            }
        });
    }

    private void findView() {
        this.imageView = findViewById(R.id.imageView);
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(takePictureIntent);
        }
    }

    public void savePhoto(View v) {
        Intent myIntent = new Intent();
        myIntent.putExtra("IMAGE", this.imageBitmap);
        this.setResult(RESULT_OK, myIntent);
        this.finish();
    }

    public void deletePhoto(View v) {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    public void returnWithNoPhoto() {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("IMAGE", this.imageBitmap);
    }
}