package com.example.android.intellischeduler;

/**
 * Created by AKANKSH on 3/31/2018.
 */

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewActivity extends AppCompatActivity {

    private Button reset,note_it;
    ListView listView,lol;
    ArrayAdapter<String> adapter;
    private EditText name,priority,duration;
    Date currentTime=null;
    String ctime=null;
    final Context context = this;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference schedule_db;
    private FirebaseAuth mAuth;


    public NewActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        String[] dotw={"Schedule it for me","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};


        mFirebaseDatabase = FirebaseDatabase.getInstance();

        listView=(ListView)findViewById(R.id.Days_of_the_week);

        reset=(Button)findViewById(R.id.Reset);
        note_it=(Button)findViewById(R.id.Note_it);

        name=(EditText)findViewById(R.id.Name);
        priority=(EditText)findViewById(R.id.Priority);
        duration=(EditText)findViewById(R.id.Duration);

        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                name.setText("");
                priority.setText("");
                duration.setText("");
            }
        });

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,dotw);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        note_it.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int[]array1={0,0,0,0,0,0,0};
                String weekStatus="";
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                if(checked.keyAt(0)==0){}
                else{
                for (int i = 0; i < checked.size(); i++) {
                    array1[checked.keyAt(i)-1]=1;
                }}
                for(int j=0;j<array1.length;j++){
                    weekStatus=weekStatus+array1[j];
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                     currentTime = Calendar.getInstance().getTime();
                     ctime=currentTime.toString();
                }
                mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                String email_id=user.getEmail();
                Log.v("Test","Doing the firebase thing");
                String name1=name.getText().toString();
                String priority1=priority.getText().toString();
                String extent1=duration.getText().toString();
                ToDoList item=new ToDoList(name1,priority1,extent1,ctime,weekStatus);
                schedule_db = mFirebaseDatabase.getReference();
                String user_name[]=email_id.split("@");
                schedule_db.child(user_name[0]).push().setValue(item);
                Toast toast=Toast.makeText(getApplicationContext(),"Noted !!",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

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
                startActivity(new Intent(getApplicationContext(),MondayStart.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}


}
