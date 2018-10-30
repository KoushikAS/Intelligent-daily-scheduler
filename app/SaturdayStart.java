package com.example.android.intellischeduler;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * Created by AKANKSH on 4/29/2018.
 */

public class SaturdayStart extends AppCompatActivity {
    ArrayList<String> slots;
    String t_start;
    String t_end;
    TextView textView;
    private EditText time_start,time_end;
    private Button add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_monday);
        slots = new ArrayList<>();
        textView = (TextView) findViewById(R.id.time_slots);
        textView.setText("Saturday slots \n\n\n");
        time_start=(EditText)findViewById(R.id.mon_start_time);
        time_end=(EditText)findViewById(R.id.mon_end_time);
        time_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                }
                int hour = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    hour = c.get(Calendar.HOUR_OF_DAY);
                }
                int minute = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    hour = c.get(Calendar.MINUTE);
                }


                TimePickerDialog timePickerDialog = new TimePickerDialog(SaturdayStart.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time_start.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        time_end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                }
                int hour = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    hour = c.get(Calendar.HOUR_OF_DAY);
                }
                int minute = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    hour = c.get(Calendar.MINUTE);
                }


                TimePickerDialog timePickerDialog = new TimePickerDialog(SaturdayStart.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time_end.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        add=(Button)findViewById(R.id.add_time);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String start=time_start.getText().toString();
                String end=time_end.getText().toString();
                slots.add(start+" - "+end+"\n\n");
                String fin="Saturday slots \n\n\n";
                for(String slot:slots){
                    fin=fin+slot;
                }
                textView.setText(fin);
                time_start.setText("");
                time_end.setText("");
            }
        });
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.start, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_done:
                startActivity(new Intent(getApplicationContext(),SundayStart.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}
}



