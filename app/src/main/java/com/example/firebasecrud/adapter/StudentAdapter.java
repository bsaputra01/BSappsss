package com.example.firebasecrud.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasecrud.R;
import com.example.firebasecrud.StudentData;
import com.example.firebasecrud.StudentRegister;
import com.example.firebasecrud.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.CardViewViewHolder>{

    private Context context;
    private ArrayList<Student> listStudent;
    private ArrayList<Student> getListStudent() {
        return listStudent;
    }
    public void setListStudent(ArrayList<Student> listStudent) {
        this.listStudent = listStudent;
    }
    public StudentAdapter(Context context) {
        this.context = context;
    }
    DatabaseReference dbStudent;
    private FirebaseAuth mAuth;

    @NonNull
    @Override
    public StudentAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_adapter, parent, false);
        dbStudent = FirebaseDatabase.getInstance().getReference("student");
        mAuth = FirebaseAuth.getInstance();
        return new StudentAdapter.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final StudentAdapter.CardViewViewHolder holder, final int position) {
        final Student student = getListStudent().get(position);
        holder.lbl_name.setText(student.getName());
        holder.lbl_nim.setText(student.getNim());
        holder.lbl_email.setText(student.getEmail());
        holder.lbl_gender.setText(student.getGender());
        holder.lbl_age.setText(student.getAge());
        holder.lbl_address.setText(student.getAddress());

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StudentRegister.class);
                Student students = new Student(student.getUid(), student.getEmail(),student.getPassword(),student.getName(),student.getNim(),student.getGender(),student.getAge(),student.getAddress());
                intent.putExtra("data_student", students);
                intent.putExtra("position", position);
                intent.putExtra("action", "edit");
                context.startActivity(intent);
            }
        });
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Konfirmasi")
                        .setIcon(R.drawable.ic_logo_logomark)
                        .setMessage("Are you sure to delete "+student.getName()+"'s data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String uid = student.getUid();
                                        mAuth.signInWithEmailAndPassword(student.getEmail(),student.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                mAuth.getCurrentUser().delete();
                                                dbStudent.child(student.getUid()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Intent in = new Intent(view.getContext(), StudentData.class);
                                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        Toast.makeText(view.getContext(), "Delete success!", Toast.LENGTH_SHORT).show();
                                                        context.startActivity(in);
                                                        dialogInterface.cancel();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }, 2000);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
            }
        });





    }

    @Override
    public int getItemCount() {
        return getListStudent().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView lbl_name, lbl_gender, lbl_nim, lbl_email, lbl_age, lbl_address;
        ImageView btn_edit, btn_del;

        CardViewViewHolder(View itemView) {
            super(itemView);
            lbl_name = itemView.findViewById(R.id.studentAdapterName);
            lbl_gender = itemView.findViewById(R.id.studentAdapterGender);
            lbl_nim = itemView.findViewById(R.id.studentAdapterNIM);
            lbl_email = itemView.findViewById(R.id.studentAdapterEmail);
            lbl_age = itemView.findViewById(R.id.studentAdapterAge);
            lbl_address = itemView.findViewById(R.id.studentAdapterAddress);
            btn_edit = itemView.findViewById(R.id.studentAdapterEditButton);
            btn_del = itemView.findViewById(R.id.studentAdapterDeleteButton);

        }
    }
}