package com.example.android.intellischeduler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DividerItemDecoration mDividerItemDecoration;
    private FirebaseAuth mAuth;
    private RecyclerView toDoListItems;
    final Context context = this;
    ArrayList<ToDoList> items;
    private MainScheduleAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String email_id=user.getEmail();
        String user_name[]=email_id.split("@");
        DatabaseReference myRef = db.getReference(user_name[0]);
        items=new ArrayList();

        toDoListItems=(RecyclerView)findViewById(R.id.scheduleItems);
        Log.v("tag","testing");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    ToDoList message = messageSnapshot.getValue(ToDoList.class);
                    if(message.weekStatus.equals("0000000"))
                    items.add(message);
                }
                mAdapter = new MainScheduleAdapter(items, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {

                    }
                });
                toDoListItems.setAdapter(mAdapter);

                //items=new ArrayList<ToDoList>(init.values());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//
//
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        toDoListItems.setLayoutManager(layoutManager);
        toDoListItems.setHasFixedSize(true);

        mDividerItemDecoration = new DividerItemDecoration(
                toDoListItems.getContext(),
                layoutManager.getOrientation()
        );
        toDoListItems.addItemDecoration(mDividerItemDecoration);
        Log.i("MainActivity","Started");

    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_new:
                startActivity(new Intent(getApplicationContext(),NewActivity.class));
                return true;
            case R.id.navigation_lulz:
                startActivity(new Intent(getApplicationContext(),TabWithIconActivity.class));
                return true;
            case R.id.navigation_all:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.navigation_schedule:
                startActivity(new Intent(getApplicationContext(),ScheduleDisplay.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}
}
