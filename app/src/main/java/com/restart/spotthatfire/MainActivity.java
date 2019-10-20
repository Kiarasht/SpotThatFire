package com.restart.spotthatfire;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.restart.spotthatfire.ui.dashboard.ReportFragment;
import com.restart.spotthatfire.ui.home.MapsFragment;
import com.restart.spotthatfire.ui.notifications.PrepareFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static boolean[] radioValues = new boolean[9];
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemSelectedListener(this);
        navView.setSelectedItemId(R.id.navigation_report);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 999);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (menuItem.getItemId()) {
            case R.id.navigation_report:
                ft.replace(R.id.nav_host_fragment, new ReportFragment(), ReportFragment.class.getSimpleName());
                ft.commit();
                break;
            case R.id.navigation_home:
                ft.replace(R.id.nav_host_fragment, new MapsFragment(), MapsFragment.class.getSimpleName());
                ft.commit();
                break;
            case R.id.navigation_prepare:
                ft.replace(R.id.nav_host_fragment, new PrepareFragment(), PrepareFragment.class.getSimpleName());
                ft.commit();
                break;
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
