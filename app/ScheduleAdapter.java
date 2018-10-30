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
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class
ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.itemHolder> {

    private ArrayList<ToDoList> items;
    //private ToDoList[] items;

    public ScheduleAdapter(ArrayList<ToDoList> items) {
        this.items = items;
    }

    public itemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.row_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        itemHolder viewHolder = new itemHolder(view);

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

    class itemHolder extends RecyclerView.ViewHolder {
        TextView taskNameView,durationView;
        RatingBar priorityView;

        itemHolder(View itemView) {
            super(itemView);
            taskNameView = (TextView) itemView.findViewById(R.id.taskName);
            durationView=(TextView) itemView.findViewById(R.id.duration);
            priorityView=(RatingBar) itemView.findViewById(R.id.priority);
            //dateTimeView=(TextView) itemView.findViewById(R.id.date_time);
        }
        void bind(ToDoList temp){
            taskNameView.setText(temp.taskName);
            durationView.setText("Takes "+temp.priority+" mins for completion");
            //durationView.setText(temp.priority);
            priorityView.setRating((Float.parseFloat(temp.duration)));
            priorityView.setIsIndicator(true);
            //dateTimeView.setText(temp.tstamp);
        }

    }
}
