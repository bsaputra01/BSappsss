package com.example.firebasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button addStudentBtn, addLecturerBtn, addCourseBtn, studentLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addStudentBtn = findViewById(R.id.starterAddStudentButton);
        addLecturerBtn = findViewById(R.id.starterAddLecturerButton);
        addCourseBtn = findViewById(R.id.starterAddCourseButton);
        studentLoginBtn = findViewById(R.id.starterLoginStudentButton);

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentRegister.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        addLecturerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddLecturer.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCourse.class);
                intent.putExtra("action", "add");
                startActivity(intent);
            }
        });
        studentLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginStudent.class);
                startActivity(intent);
            }
        });
    }
}
