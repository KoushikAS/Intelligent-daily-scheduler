package com.example.android.intellischeduler;

/**
 * Created by AKANKSH on 3/30/2018.
 */

public class ToDoList {
    public String taskName;
    public String duration;
    public String priority;
    public String tstamp;
    public String weekStatus;

    ToDoList(){}
    ToDoList(String taskName,String duration,String priority,String tstamp,String weekStatus){
        this.taskName=taskName;
        this.duration=duration;
        this.priority=priority;
        this.tstamp=tstamp;
        this.weekStatus=weekStatus;
    }
}
