package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentArea extends AppCompatActivity {

    private BottomNavigationView bnv;
    Toolbar mbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_area);
        mbar = findViewById(R.id.StudentAreaToolbar);
        setSupportActionBar(mbar);

        bnv = findViewById(R.id.bottom_navigation);

        bnv.setOnNavigationItemSelectedListener(bottomNavMethod);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AccountFragment()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ScheduleFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_schedule:
                            getSupportActionBar().setTitle("My Schedule");
                            selectedFragment = new ScheduleFragment();
                            break;
                        case R.id.nav_courses:
                            getSupportActionBar().setTitle("My Courses");
                            selectedFragment = new CourseFragment();
                            break;
                        case R.id.nav_account:
                            getSupportActionBar().setTitle("My Account");
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;

                }
            };
}