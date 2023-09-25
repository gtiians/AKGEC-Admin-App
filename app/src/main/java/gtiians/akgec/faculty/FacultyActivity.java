package gtiians.akgec.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FacultyActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView appliedDepartment, civilDepartment, csDepartment, eeeDepartment, eceDepartment, meDepartment, itDepartment;
    private LinearLayout appliedNoData, civilNoData, csNoData, eeeNoData, eceNoData, meNoData, itNoData;
    private List<TeacherData> list1, list2, list3, list4, list5, list6, list7;

    private TeacherAdapter teacherAdapter;

    private DatabaseReference reference, databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);


        appliedDepartment = findViewById(R.id.appliedDepartment);
        civilDepartment = findViewById(R.id.civilDepartment);
        csDepartment = findViewById(R.id.csDepartment);
        eeeDepartment = findViewById(R.id.eeeDepartment);
        eceDepartment = findViewById(R.id.eceDepartment);
        meDepartment = findViewById(R.id.meDepartment);
        itDepartment = findViewById(R.id.itDepartment);

        appliedNoData = findViewById(R.id.appliedNoData);
        civilNoData = findViewById(R.id.civilNoData);
        csNoData = findViewById(R.id.csNoData);
        eeeNoData = findViewById(R.id.eeeNoData);
        eceNoData = findViewById(R.id.eceNoData);
        meNoData = findViewById(R.id.meNoData);
        itNoData = findViewById(R.id.itNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculties");

        appliedDepartment();
        civilDepartment();
        csDepartment();
        eeeDepartment();
        eceDepartment();
        meDepartment();
        itDepartment();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> startActivity(new Intent(FacultyActivity.this,AddTeacherActivity.class)));

    }


    private void appliedDepartment() {
        databaseReference = reference.child("Applied Sciences & Humanities");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    appliedNoData.setVisibility(View.VISIBLE);
                    appliedDepartment.setVisibility(View.GONE);
                }else {
                    appliedNoData.setVisibility(View.GONE);
                    appliedDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    appliedDepartment.setHasFixedSize(true);
                    appliedDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list1, FacultyActivity.this, "Applied Sciences & Humanities");
                    appliedDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void civilDepartment() {
        databaseReference = reference.child("Civil Engineering");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2 = new ArrayList<>();
                if (!snapshot.exists()){
                    civilNoData.setVisibility(View.VISIBLE);
                    civilDepartment.setVisibility(View.GONE);
                }else {
                    civilNoData.setVisibility(View.GONE);
                    civilDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    civilDepartment.setHasFixedSize(true);
                    civilDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list2, FacultyActivity.this, "Civil Engineering");
                    civilDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void csDepartment() {
        databaseReference = reference.child("Computer Science and Engineering");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list3, FacultyActivity.this,"Computer Science and Engineering");
                    csDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void eeeDepartment() {
        databaseReference = reference.child("Electrical and Electronics Engineering");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4 = new ArrayList<>();
                if (!snapshot.exists()){
                    eeeNoData.setVisibility(View.VISIBLE);
                    eeeDepartment.setVisibility(View.GONE);
                }else {
                    eeeNoData.setVisibility(View.GONE);
                    eeeDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    eeeDepartment.setHasFixedSize(true);
                    eeeDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list4, FacultyActivity.this,"Electrical and Electronics Engineering");
                    eeeDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void eceDepartment() {
        databaseReference = reference.child("Electronics and Communication Engineering");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5 = new ArrayList<>();
                if (!snapshot.exists()){
                    eceNoData.setVisibility(View.VISIBLE);
                    eceDepartment.setVisibility(View.GONE);
                }else {
                    eceNoData.setVisibility(View.GONE);
                    eceDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    eceDepartment.setHasFixedSize(true);
                    eceDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list5, FacultyActivity.this, "Electronics and Communication Engineering");
                    eceDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void meDepartment() {
        databaseReference = reference.child("Mechanical Engineering");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6 = new ArrayList<>();
                if (!snapshot.exists()){
                    meNoData.setVisibility(View.VISIBLE);
                    meDepartment.setVisibility(View.GONE);
                }else {
                    meNoData.setVisibility(View.GONE);
                    meDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list6.add(data);
                    }
                    meDepartment.setHasFixedSize(true);
                    meDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list6, FacultyActivity.this, "Mechanical Engineering");
                    meDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void itDepartment() {
        databaseReference = reference.child("Information Technology");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list7 = new ArrayList<>();
                if (!snapshot.exists()){
                    itNoData.setVisibility(View.VISIBLE);
                    itDepartment.setVisibility(View.GONE);
                }else {
                    itNoData.setVisibility(View.GONE);
                    itDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        TeacherData data = dataSnapshot.getValue(TeacherData.class);
                        list7.add(data);
                    }
                    itDepartment.setHasFixedSize(true);
                    itDepartment.setLayoutManager(new LinearLayoutManager(FacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list7, FacultyActivity.this,"Information Technology");
                    itDepartment.setAdapter(teacherAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
