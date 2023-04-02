package com.example.myphotoapplicationversion2;

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
import android.graphics.ImageDecoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap imageBitmap;
    private Uri photoURI;
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
                Log.i("TEST", " fini?");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.i("TEST", " ok?");
                    setBitmap();
//                    Intent data = result.getData();
//                    if (data != null) {
//
//                    }
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

    private void setBitmap() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                imageBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), photoURI));
            } else {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageView.setImageBitmap(imageBitmap);
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if ((takePictureIntent.resolveActivity(getPackageManager()) != null)  && StorageUtils.isExternalStorageWritable()) {
            Log.i("TEST", "writable");
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = StorageUtils.createFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i("TEST", "photo file not null");
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.myphotoapplicationversion2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activityResultLauncher.launch(takePictureIntent);
            }
        }
    }

    public void savePhoto(View v) {
        saveBitmap();
        Intent myIntent = new Intent();
//        myIntent.putExtra("IMAGE", this.imageBitmap);
        this.setResult(RESULT_OK, myIntent);
        this.finish();
    }

    public void saveBitmap() {
        try {
//            File thumbnailFile = StorageUtils.createFile(getFilesDir(), "thumbnails");
            File thumbnailFile = StorageUtils.createFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "thumbnails");
            FileOutputStream fos = new FileOutputStream(thumbnailFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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