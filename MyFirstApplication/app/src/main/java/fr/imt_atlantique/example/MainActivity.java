package fr.imt_atlantique.example;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements EditFragment.OnEditFragment, DatePickerFragment.OnDateSet, DateFragment.OnDateFragment, DisplayFragment.OnDisplayFragment {
    private EditFragment editFragment;
    private DisplayFragment displayFragment;
    private DateFragment dateFragment;
    private User user;
    private Uri photoURI;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Lifecycle", "onCreate method");

        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        loadData();
        createEditFragment();
        initLauncher();
    }

    public void createEditFragment() {
        if(editFragment == null) {
            editFragment = EditFragment.newInstance(this.user);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction tx = fragmentManager.beginTransaction();
            tx.add(R.id.linear_layout_activity_main, editFragment);
            tx.commit();
//            if(imageBitmap != null) {
//                editFragment.setBitmap(imageBitmap);
//            }
        }
    }

    private void initLauncher() {
        this.activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    manageBitmap();
                }
            }
        });
    }

    private void manageBitmap() {
        Log.i("DEBUG", photoFile.getPath());
        user.setPhotoPath(photoFile.getPath());
        editFragment.updateUserPhoto(photoFile.getPath());
        Bitmap bitmap = getBitmap(photoURI);
        setBitmap(bitmap);
        saveBitmap(bitmap);
    }

    private Bitmap getBitmap(Uri bitmapUri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), bitmapUri));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), bitmapUri);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }

    private void setBitmap(Bitmap bitmap) {
        editFragment.setBitmap(bitmap);
    }

    private void saveBitmap(Bitmap bitmap) {
        try {
            File thumbnailFile = StorageUtils.createFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "thumbnails");
            FileOutputStream fos = new FileOutputStream(thumbnailFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // méthodes de gestion des données
    private void saveData() {
        Log.i("debug", "saveData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myData.edit();

        myEditor.putString("FirstName", this.user.getFirstName());
        myEditor.putString("LastName", this.user.getLastName());
        myEditor.putString("Birthday", this.user.getBirthday());
        myEditor.putString("BirthCity", this.user.getBirthCity());
        myEditor.putString("Department", this.user.getDepartment());
        myEditor.putString("PhotoPath", this.user.getPhotoPath());
        Set<String> phoneSet = new HashSet<>(this.user.getPhones());
        myEditor.putStringSet("Phones", phoneSet);

        myEditor.apply();
    }

    private void loadData() {
        Log.i("debug", "loadData");
        SharedPreferences myData = getSharedPreferences("data", Context.MODE_PRIVATE);

        String firstName = myData.getString("FirstName", "");
        String lastName = myData.getString("LastName", "");
        String birthday = myData.getString("Birthday", "");
        String birthCity = myData.getString("BirthCity", "");
        String department = myData.getString("Department", "");
        String photoPath = myData.getString("PhotoPath", "");
        Set<String> phoneSet = myData.getStringSet("Phones", new HashSet<>());
        List<String> phones = new ArrayList<>(phoneSet);

        this.user = new User(firstName, lastName, birthday, birthCity, department, photoPath, phones);

//        if (photoPath.length() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            this.imageBitmap = ThumbnailUtils.createImageThumbnail(photoPath, 1);
//        }
    }

    public void deletePhone(View v) {
        RelativeLayout rl = (RelativeLayout) v.getParent();
        ViewGroup parentView = (ViewGroup) rl.getParent();
        parentView.removeView(rl);
    }

    @Override
    public void setEditFragment(EditFragment fragment) {
        this.editFragment = fragment;
    }

    @Override
    public void createDisplayFragment() {
        displayFragment = DisplayFragment.newInstance(this.user);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.hide(editFragment);
        tx.add(R.id.linear_layout_activity_main, displayFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void createDateFragment() {
        dateFragment = new DateFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.hide(editFragment);
        tx.add(R.id.linear_layout_activity_main, dateFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if ((takePictureIntent.resolveActivity(getPackageManager()) != null)  && StorageUtils.isExternalStorageWritable()) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = StorageUtils.createFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "fr.imt_atlantique.example.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activityResultLauncher.launch(takePictureIntent);
            }
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public Bitmap getThumbnail(String path) {
        Log.i("DEBUG", path);
        File file = new File(path);
        Uri photoUri = FileProvider.getUriForFile(this,
                "fr.imt_atlantique.example.fileprovider",
                file);
        Bitmap thumbnail = getBitmap(photoUri);

//        File imageFile = new File(path);
//
//        if (StorageUtils.isExternalStorageReadable()) {
//            ContentResolver cr = getContentResolver();
//
//            String fileName = imageFile.getName(); // Get the file name
//            Log.i("DEBUG", fileName);
//            Pattern pattern = Pattern.compile("\\d+"); // Create a regular expression to match numbers
//            Matcher matcher = pattern.matcher(fileName);
//            if (matcher.find()) {
//                long imageId = Long.parseLong(matcher.group()); // Extract the first number as the image ID
//
//                requestPermission(); // Request permission from the user
//
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    long groupId = MediaStore.Images.Thumbnails.MINI_KIND; // Set the thumbnail group ID
//                    int kind = MediaStore.Images.Thumbnails.MINI_KIND; // Set the thumbnail kind
//                    thumbnail = MediaStore.Images.Thumbnails.getThumbnail(cr, imageId, groupId, kind, null); // Get the thumbnail
//                }
//            }
//        }

        return thumbnail;
    }

    @Override
    public void setDateFragment(DateFragment fragment) {
        this.dateFragment = fragment;
    }

    @Override
    public void showDatePickerDialog(View v) {
        Log.i("debug", "showDatePickerDialog");
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void setDate(String date) {
        this.editFragment.setDate(date);
    }

    @Override
    public void closeDateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.remove(dateFragment);
        tx.show(editFragment);
        fragmentManager.popBackStack();
        tx.commit();
    }

    @Override
    public void onDateChanged(int year, int month, int dayOfMonth) {
        dateFragment.setDate("day/month/year: " + dayOfMonth + "/" + month + "/" + year);
    }

    @Override
    public void setDisplayFragment(DisplayFragment fragment) {
        this.displayFragment = fragment;
    }

    @Override
    public void closeDisplayFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction tx = fragmentManager.beginTransaction();
        tx.remove(displayFragment);
        tx.show(editFragment);
        fragmentManager.popBackStack();
        tx.commit();
    }

    @Override
    public void actionCall(View v) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
        saveData();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}