/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.intellischeduler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class ScheduleDisplay extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference schedule_db;
    String email_id;
    String user_name[];
    ArrayList<ToDoList> items;
    Button action,action2;
     HashMap<String,String>dummyhash=null;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_schedule);
        dummyhash = new HashMap<String, String>();
        action = (Button) findViewById(R.id.search);
        action2 = (Button) findViewById(R.id.search_1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        schedule_db = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        email_id = user.getEmail();
        user_name = email_id.split("@");
        DatabaseReference myRef = db.getReference(user_name[0]);
        items = new ArrayList();

        Log.v("tag", "testing");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ToDoList message = messageSnapshot.getValue(ToDoList.class);
                    if (message.weekStatus.equals("0000000"))
                        items.add(message);
                    /*else {
                        messageSnapshot.getRef().removeValue();
                    }*/
                }


                //items=new ArrayList<ToDoList>(init.values());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*for (ToDoList item_1 : items) {
                    //hashMap.put(item_1.taskName,item_1.duration);
                    hashMap.put("Task", item_1.taskName);
                    int temp = Integer.parseInt(item_1.priority);
                    int hour = temp / 60;
                    int min = temp % 60;
                    double f_hour = 0.0;
                    if (min <= 15)
                        f_hour = hour + 0.25;
                    else if (min <= 30)
                        f_hour = hour + 0.5;
                    else if (min <= 45)
                        f_hour = hour + 0.75;
                    else
                        f_hour = hour + 1;
                    hashMap.put("hour", Double.toString(f_hour));
                    new Communicate().execute(" http://35.227.60.228:7880/schedule_tasks");
                    hashMap.clear();
                }*/
                //new Communicate().execute(" http://35.227.60.228:7880/schedule_tasks");
                new Communicate().execute(" http://192.168.43.69:7880/schedule_tasks");
            }
        });
        action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query applesQuery = schedule_db.child(user_name[0]);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            ToDoList temp=appleSnapshot.getValue(ToDoList.class);
                            if(!(temp.weekStatus.equals("0000000")||temp.weekStatus.equals("00000000")))
                            appleSnapshot.getRef().removeValue();
                            String a ="hello";
                        }
                        items.clear();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

                for (ToDoList item_1 : items) {
                    //hashMap.put(item_1.taskName,item_1.duration);
                    //hashMap.put("Task", item_1.taskName);
                    int temp = Integer.parseInt(item_1.priority);
                    int hour = temp / 60;
                    int min = temp % 60;
                    double f_hour = 0.0;
                    if (min <= 15)
                        f_hour = hour + 0.25;
                    else if (min <= 30)
                        f_hour = hour + 0.5;
                    else if (min <= 45)
                        f_hour = hour + 0.75;
                    else
                        f_hour = hour + 1;
                    hashMap.put(item_1.taskName, Double.toString(f_hour));
                }
                //new PostingTasks().execute("http://35.227.60.228:7880/post_tasks");
                new PostingTasks().execute("http://192.168.43.69:7880/post_tasks");
            }
        });

    }






    public class Communicate extends AsyncTask<String,Void,String>{
        protected void onPreExecute(){
            super.onPreExecute();
        }
        protected String doInBackground(String... params){
            URL url=null;
            try {
                 url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //return new NetworkUtils().performPostCall(params[0],hashMap);
            //return  new NetworkUtils().performGetCall(params[0]);

                String temp=  new NetworkUtils().performGetCall(params[0]);
                return temp;


        }

        @Override
        protected void onPostExecute(String result)  {
            //super.onPostExecute(result);
            JSONObject obj=null;
            JSONObject start=null;
            JSONObject task=null;
            JSONObject end=null;
            JSONObject weekday=null;
            try {
                 obj = new JSONObject(result);

                 start=obj.getJSONObject("start");
                 task=obj.getJSONObject("Task");
                end=obj.getJSONObject("end");
                weekday=obj.getJSONObject("weekday");
                int n=start.length();
                String a="a";
                for(int i=0;i<n;i++)
                {
                    ToDoList item;
                    String tname=task.getString(Integer.toString(i));
                    String st=start.getString(Integer.toString(i));
                    String en=end.getString(Integer.toString(i));
                    String day=weekday.getString(Integer.toString(i));
                    int dayno=Integer.parseInt(day);
                    String dstatus="";
                    int j,k;
                    for(j=0;j<dayno;j++)
                    {
                        dstatus=dstatus+"0";
                    }
                    dstatus=dstatus+"1";
                    for(k=j;k<7;k++)
                        dstatus=dstatus+"0";
                    item = new ToDoList(tname,"3",st+" - "+en,"",dstatus);
                    schedule_db.child(user_name[0]).push().setValue(item);  //The insert line important

                }
                //schedule_db.child(user_name[0]).push().setValue(item);  The insert line important
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class PostingTasks extends AsyncTask<String,Void,String>{
        protected void onPreExecute(){
            super.onPreExecute();
        }
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String temp=new NetworkUtils().performPostCall(params[0],hashMap);
            return "done";
        }
    }


    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.showschedule, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.navigation_end_sch:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }}
}

