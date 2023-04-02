package com.example.myphotoapplicationversion2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button button;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initLauncher();
    }

    private void initLauncher() {
        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
//                    Intent data = result.getData();
//                    if (data != null) {
//                        savePhoto(data);
//                    }
                }
            }
        });
    }

//    private void savePhoto(Intent intent) {
//        Bundle extras = intent.getExtras();
//        if(extras != null) {
//            Bitmap imageBitmap = extras.getParcelable("IMAGE");
//
//            try {
//                File thumbnailFile = StorageUtils.createFile(getFilesDir(), "thumbnails");
//                FileOutputStream fos = new FileOutputStream(thumbnailFile);
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }

    private void findView() {
        this.button = findViewById(R.id.button);
    }

    public void actionTakePhoto(View v) {
        Intent intent = new Intent(this, PhotoActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            activityResultLauncher.launch(intent);
        }
    }

}