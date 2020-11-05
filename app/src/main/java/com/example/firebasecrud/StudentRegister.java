package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.firebasecrud.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity {
    private Toolbar registerStudentToolbar;
    private EditText registerStudentName;
    private EditText registerStudentNIM;
    private EditText registerStudentEmail;
    private EditText registerStudentPassword;
    private EditText registerStudentAge;
    private EditText registerStudentAddress;
    private RadioGroup registerStudentGender;
    private RadioButton registerStudentRB;
    private Button registerButton;
    String sUID = "", sName = "", sNIM = "", sEmail = "", sPassword = "", sGender = "", sAge = "", sAddress = "", action = "";
    Student student;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        registerStudentToolbar = findViewById(R.id.registerStudentToolbar);
        setSupportActionBar(registerStudentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        registerStudentName = findViewById(R.id.registerStudentName);
        registerStudentNIM = findViewById(R.id.registerStudentNIM);
        registerStudentEmail = findViewById(R.id.registerStudentEmail);
        registerStudentPassword = findViewById(R.id.registerStudentPassword);
        registerStudentAge = findViewById(R.id.registerStudentAge);
        registerStudentAddress = findViewById(R.id.registerStudentAddress);

        registerStudentName.addTextChangedListener(theTextWatcher);
        registerStudentNIM.addTextChangedListener(theTextWatcher);
        registerStudentEmail.addTextChangedListener(theTextWatcher);
        registerStudentPassword.addTextChangedListener(theTextWatcher);
        registerStudentAge.addTextChangedListener(theTextWatcher);
        registerStudentAddress.addTextChangedListener(theTextWatcher);


        registerButton = findViewById(R.id.registerButton);

        registerStudentGender = findViewById(R.id.registerStudentGender);
        registerStudentGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                registerStudentRB = findViewById(i);
                sGender = registerStudentRB.getText().toString();
                if (!sName.isEmpty() && !sNIM.isEmpty() && !sGender.isEmpty() && !sEmail.isEmpty() && !sPassword.isEmpty() && !sAge.isEmpty() && !sAddress.isEmpty()) {
                    registerButton.setEnabled(true);
                } else {
                    registerButton.setEnabled(false);
                }
            }
        });

        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equalsIgnoreCase("add")) {
            mDatabase = FirebaseDatabase.getInstance().getReference("student");
            getSupportActionBar().setTitle(R.string.regstudent);
            registerButton.setText(R.string.regstudent);
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addStudent();
                }
            });
        } else if (action.equalsIgnoreCase("edit")) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            registerStudentEmail.setFocusable(false);
            registerStudentPassword.setFocusable(false);
            getSupportActionBar().setTitle("Edit Student");
            registerButton.setText("Edit");
            student = intent.getParcelableExtra("data_student");
            registerStudentPassword.setText(student.getPassword());
            registerStudentName.setText(student.getName());
            registerStudentNIM.setText(student.getNim());
            registerStudentEmail.setText(student.getEmail());
            if (student.getGender().equalsIgnoreCase("male")) {
                registerStudentGender.check(R.id.registerStudentMale);
            } else{
                registerStudentGender.check(R.id.registerStudentFemale);
            }
            registerStudentAge.setText(student.getAge());
            registerStudentAddress.setText(student.getAddress());

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFormValue();

                    Map<String, Object> params = new HashMap<>();
                    params.put("name", sName);
                    params.put("address", sAddress);
                    params.put("gender", sGender);
                    params.put("age", sAge);
                    params.put("email", sEmail);
                    params.put("password", student.getPassword());
                    params.put("nim", sNIM);
                    mDatabase.child("student").child(student.getUid()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent;
                            intent = new Intent(StudentRegister.this, StudentData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }


    }

    public void addStudent() {
        getFormValue();
        mAuth.createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(StudentRegister.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sUID = mAuth.getCurrentUser().getUid();
                            Student student = new Student(sUID, sEmail, sPassword, sName, sNIM, sGender, sAge, sAddress);
                            mDatabase.child(sUID).setValue(student).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(StudentRegister.this, "Student register successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StudentRegister.this, StudentData.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
                                    startActivity(intent, options.toBundle());
                                    finish();
                                }
                            });
                            mAuth.signOut();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException malFormed) {
                                Toast.makeText(StudentRegister.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(StudentRegister.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(StudentRegister.this, "Register failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent;
            intent = new Intent(StudentRegister.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        } else if (id == R.id.student_list) {
            Intent intent = new Intent(StudentRegister.this, StudentData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getFormValue() {
        sName = registerStudentName.getEditableText().toString().trim();
        sNIM = registerStudentNIM.getEditableText().toString().trim();
        sEmail = registerStudentEmail.getEditableText().toString().trim();
        sPassword = registerStudentPassword.getEditableText().toString().trim();
        sAge = registerStudentAge.getEditableText().toString().trim();
        sAddress = registerStudentAddress.getEditableText().toString().trim();
    }

    private TextWatcher theTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            getFormValue();
            if (!sName.isEmpty() && !sNIM.isEmpty() && !sGender.isEmpty() && !sEmail.isEmpty() && !sPassword.isEmpty() && !sAge.isEmpty() && !sAddress.isEmpty()) {
                registerButton.setEnabled(true);
            } else {
                registerButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(StudentRegister.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StudentRegister.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
