package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginStudent extends AppCompatActivity {

    Toolbar loginStudentToolbar;
    EditText loginStudentEmailInput, loginStudentPasswordInput;
    String loginStudentEmail, loginStudentPassword;
    Button loginStudentButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);

        loginStudentToolbar = findViewById(R.id.loginStudentToolbar);
        setSupportActionBar(loginStudentToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("student");


        loginStudentEmailInput = findViewById(R.id.loginStudentEmail);
        loginStudentPasswordInput = findViewById(R.id.loginStudentPassword);

        loginStudentEmailInput.addTextChangedListener(theTextWatcher);
        loginStudentPasswordInput.addTextChangedListener(theTextWatcher);

        loginStudentButton = findViewById(R.id.loginStudentButton);

        loginStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStudent();
            }
        });
    }


    public void loginStudent() {
        mAuth.signInWithEmailAndPassword(loginStudentEmail,loginStudentPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent in  = new Intent(LoginStudent.this, StudentArea.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Toast.makeText(LoginStudent.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(in);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException malFormed) {
                                Toast.makeText(LoginStudent.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Toast.makeText(LoginStudent.this, "Email already registered!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginStudent.this, "Register failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private TextWatcher theTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            loginStudentEmail = loginStudentEmailInput.getEditableText().toString().trim();
            loginStudentPassword = loginStudentPasswordInput.getEditableText().toString().trim();
            if (!loginStudentEmail.isEmpty() && !loginStudentPassword.isEmpty()) {
                loginStudentButton.setEnabled(true);
            } else {
                loginStudentButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}