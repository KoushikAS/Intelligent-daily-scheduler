package com.example.android.intellischeduler;

/**
 * Created by AKANKSH on 4/1/2018.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class
MainScheduleAdapter extends RecyclerView.Adapter<MainScheduleAdapter.itemHolder> {

    private ArrayList<ToDoList> items;
    private final ClickListener listener;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    //private ToDoList[] items;

    public MainScheduleAdapter(ArrayList<ToDoList> items, ClickListener listener) {
        this.items = items;
        this.listener=listener;
    }

    public itemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.main_row_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        itemHolder viewHolder = new itemHolder(view,listener);

        return viewHolder;
    }

    public void onBindViewHolder(itemHolder holder, int position) {
        //Log.d(TAG, "#" + position);
        //holder.bind(position);
        holder.bind(items.get(position));
    }

    public int getItemCount() {
        return items.size();
    }

    class itemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Button delete;
        private WeakReference<ClickListener> listenerRef;
        TextView taskNameView,routineStatus;
        public itemHolder(View itemView, ClickListener listener) {

            super(itemView);
            listenerRef = new WeakReference<>(listener);
            delete=(Button)itemView.findViewById(R.id.delete);
            taskNameView = (TextView) itemView.findViewById(R.id.taskName);
            routineStatus=(TextView)itemView.findViewById(R.id.routine_status);
            delete.setOnClickListener(this);
        }

        public void onClick(View v){
            if(v.getId()==delete.getId()){
                mAuth=FirebaseAuth.getInstance();
                user=mAuth.getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                String test=taskNameView.getText().toString();
                String email_id=user.getEmail();
                String user_name[]=email_id.split("@");
                Query applesQuery = ref.child(user_name[0]).orderByChild("taskName").equalTo(taskNameView.getText().toString());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        items.clear();
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            ToDoList temp=appleSnapshot.getValue(ToDoList.class);
                            //if(temp.weekStatus.equals("0000000")||temp.weekStatus.equals("00000000"))
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
                items.clear();
            }
        }
        void bind(ToDoList temp){
            taskNameView.setText(temp.taskName);
            if(temp.weekStatus.equals("0000000"))
                routineStatus.setText("Non_routine");
            else
                routineStatus.setText("Routine");
            //dateTimeView.setText(temp.tstamp);
        }

    }
}
