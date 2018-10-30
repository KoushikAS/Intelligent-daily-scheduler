package com.example.android.intellischeduler;

/**
 * Created by AKANKSH on 4/9/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//import droidmentor.tabwithviewpager.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FridayFragment extends Fragment {
    private FirebaseAuth mAuth;

    private RecyclerView toDoListItems;
    DividerItemDecoration mDividerItemDecoration;
    ArrayList<ToDoList> items;
    private ScheduleAdapter mAdapter;
    public FridayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview= inflater.inflate(R.layout.fragment_monday, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String email_id=user.getEmail();
        String user_name[]=email_id.split("@");
        DatabaseReference myRef = db.getReference(user_name[0]);
        toDoListItems=(RecyclerView)rootview.findViewById(R.id.scheduleItems);
        items=new ArrayList();
        //ToDoList item1=new ToDoList("string","string","string","string");
        //items.add(item1);
        //items.add(item1);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    ToDoList message = messageSnapshot.getValue(ToDoList.class);
                    String day = message.weekStatus;
                    //String time_items[]=day.split(" ");
                    if(day.charAt(5)=='1')
                        items.add(message);
                }
                mAdapter = new ScheduleAdapter(items);
                toDoListItems.setAdapter(mAdapter);

                //items=new ArrayList<ToDoList>(init.values());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAdapter = new ScheduleAdapter(items);
        toDoListItems.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        toDoListItems.setLayoutManager(layoutManager);
        toDoListItems.setHasFixedSize(true);

        mDividerItemDecoration = new DividerItemDecoration(
                toDoListItems.getContext(),
                layoutManager.getOrientation()
        );
        toDoListItems.addItemDecoration(mDividerItemDecoration);
        return rootview;
    }


}