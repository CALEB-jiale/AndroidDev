package fr.imt_atlantique.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//public class DisplayUserActivity extends AppCompatActivity implements DisplayFragment.OnDisplayFragment {
//    private User user;
//    private DisplayFragment displayFragment;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display);
//        Log.i("Lifecycle", "onCreate method");
//
//        loadData();
//
//        if (savedInstanceState != null) {
//            if (!savedInstanceState.getBoolean("IS_CREATED")) {
//                createDisplayFragment();
//            }
//        } else {
//            createDisplayFragment();
//        }
//    }
//
//    private void loadData() {
//        Log.i("debug", "loadData");
//        this.loadDataFromIntent(getIntent());
//    }
//
//    private void loadDataFromIntent(Intent intent) {
//        Log.i("debug", "loadDataFromIntent");
//
//        Bundle extras = intent.getExtras();
//        if(extras == null) {
//            Log.i("debug", "Intent = null");
//        } else {
//            Log.i("debug", "Intent is not null");
//            this.user = extras.getParcelable("USER");
//        }
//    }
//
//    public void createDisplayFragment() {
//        DisplayFragment displayFragment = DisplayFragment.newInstance(user);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction tx = fragmentManager.beginTransaction();
////        tx.hide(editFragment);
//        tx.add(R.id.linear_layout_activity_display, displayFragment);
//        tx.addToBackStack(null);
//        tx.commit();
//    }
//
//    @Override
//    public void actionEdit(View v) {
//        Intent myIntent = new Intent();
//        myIntent.putExtra("USER", this.user);
//        this.setResult(RESULT_OK, myIntent);
//        this.finish();
//    }
//
//    @Override
//    public void actionCall(View v) {
//        RelativeLayout rl = (RelativeLayout) v.getParent();
//        TextView textViewNum = rl.findViewById(R.id.tv_display_phone);
//        String number = textViewNum.getText().toString();
//
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + number));
//
//        String title = getResources().getString(R.string.chooser_title_share_photo);;
//        Intent chooser = Intent.createChooser(intent, title);
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(chooser);
//        }
//    }
//
//    @Override
//    public void setDisplayFragment(DisplayFragment fragment) {
//        displayFragment = fragment;
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("IS_CREATED", true);
//    }
//
//}
