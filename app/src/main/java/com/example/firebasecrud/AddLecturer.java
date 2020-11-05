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

import com.example.firebasecrud.R;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.firebasecrud.model.Lecturer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddLecturer extends AppCompatActivity {

    EditText addLecturerNameInput, addLecturerExpertiseInput;
    RadioButton radioButton;
    RadioGroup addLecturerGenderRG;
    String addLecturerName="", addLecturerExpertise="", addLecturerGender="male", action = "";
    Button addLecturerButton;
    Toolbar addLecturerTB;
    Lecturer lecturer;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecturer);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        addLecturerTB = findViewById(R.id.addLecturerToolbar);
        setSupportActionBar(addLecturerTB);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        addLecturerExpertiseInput = findViewById(R.id.addLecturerExpertise);
        addLecturerNameInput = findViewById(R.id.addLecturerName);
        addLecturerNameInput.addTextChangedListener(theTextWatcher);
        addLecturerExpertiseInput.addTextChangedListener(theTextWatcher);

        addLecturerButton = findViewById(R.id.addLecturerButton);
        addLecturerGenderRG = findViewById(R.id.addLecturerGender);

        addLecturerGenderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButton = findViewById(i);
                addLecturerGender = radioButton.getText().toString();
                if (!addLecturerName.isEmpty() && !addLecturerExpertise.isEmpty() && !addLecturerGender.isEmpty()) {
                    addLecturerButton.setEnabled(true);
                } else {
                    addLecturerButton.setEnabled(false);
                }

            }
        });



        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        if (action.equals("add")) {
            getSupportActionBar().setTitle(R.string.addlecturer);
            addLecturerButton.setText(R.string.addlecturer);
            addLecturerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addLecturerName = addLecturerNameInput.getEditableText().toString().trim();
                    addLecturerExpertise = addLecturerExpertiseInput.getEditableText().toString().trim();
                    addLecturerGender = radioButton.getText().toString();
                    addLecturer(addLecturerName, addLecturerGender, addLecturerExpertise);
                }
            });
        } else{

            getSupportActionBar().setTitle("Edit Lecturer");
            lecturer = intent.getParcelableExtra("edit_data_lect");
            addLecturerNameInput.setText(lecturer.getName());
            addLecturerExpertiseInput.setText(lecturer.getExpertise());
            if(lecturer.getGender().equalsIgnoreCase("male")){
                addLecturerGenderRG.check(R.id.addLecturerMale);
            }else{
                addLecturerGenderRG.check(R.id.addLecturerFemale);
            }

            addLecturerButton.setText("Edit Lecturer");
            addLecturerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addLecturerName = addLecturerNameInput.getEditableText().toString().trim();
                    addLecturerExpertise = addLecturerExpertiseInput.getEditableText().toString().trim();
                    addLecturerGender = radioButton.getText().toString();

                    Map<String,Object> params = new HashMap<>();
                    params.put("name", addLecturerName);
                    params.put("expertise", addLecturerExpertise);
                    params.put("gender", addLecturerGender);
                    mDatabase.child("lecturer").child(lecturer.getId()).updateChildren(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent;
                            intent = new Intent(AddLecturer.this, LecturerData.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
                            startActivity(intent, options.toBundle());
                            finish();
                        }
                    });
                }
            });
        }

    }

    public void addLecturer(String mname, String mgender, String mexpertise) {
        String mid = mDatabase.child("lecturer").push().getKey();
        Lecturer lecturer = new Lecturer(mid, mname, mgender, mexpertise);
        mDatabase.child("lecturer").child(mid).setValue(lecturer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddLecturer.this, "Lecturer Added Successfully", Toast.LENGTH_SHORT).show();
                addLecturerName = addLecturerNameInput.getEditableText().toString().trim();
                addLecturerExpertise = addLecturerExpertiseInput.getEditableText().toString().trim();
                addLecturerGender = radioButton.getText().toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddLecturer.this, "Insert Data Failed!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private TextWatcher theTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            addLecturerName = addLecturerNameInput.getEditableText().toString().trim();
            addLecturerExpertise = addLecturerExpertiseInput.getEditableText().toString().trim();
            if (!addLecturerName.isEmpty() && !addLecturerExpertise.isEmpty() && !addLecturerGender.isEmpty()) {
                addLecturerButton.setEnabled(true);
            } else {
                addLecturerButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lecturer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(AddLecturer.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        } else if (id == R.id.lecturer_list) {
            Intent intent = new Intent(AddLecturer.this, LecturerData.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
            startActivity(intent, options.toBundle());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(AddLecturer.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AddLecturer.this);
        startActivity(intent, options.toBundle());
        finish();
    }
}
