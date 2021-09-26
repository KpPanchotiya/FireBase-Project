package com.example.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity {
    //shows the data that we insert from send data
    //TextView tv_name,tv_contact,tv_city;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Student> list;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        //tv_name = findViewById(R.id.sh_name);
        //tv_contact = findViewById(R.id.sh_contact);
        //tv_city = findViewById(R.id.sh_city);
        recyclerView = findViewById(R.id.listview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        getData();
    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrive user list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    list.add(student);
                }
                myAdapter.notifyDataSetChanged();
                //retrive one user
                //String dname = snapshot.child("name").getValue().toString();
                //String dcontact = snapshot.child("contact").getValue().toString();
                //String dcity = snapshot.child("city").getValue().toString();
//
                //tv_name.setText(dname);
                //tv_contact.setText(dcontact);
                //tv_city.setText(dcity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}