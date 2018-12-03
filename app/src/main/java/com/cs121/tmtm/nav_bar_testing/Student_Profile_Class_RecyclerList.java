package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Student_Profile_Class_RecyclerList extends RecyclerView.Adapter<Student_Profile_Class_RecyclerList.MyViewHolder> {
    private List<String> classList;
    private Context context;

    public Student_Profile_Class_RecyclerList(List<String> classList) {
        this.classList = classList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView className;
        private TextView classID;

        public MyViewHolder(View itemView) {
            super(itemView);
            className = (TextView) itemView.findViewById(R.id.profile_classTitle);
            classID = (TextView) itemView.findViewById(R.id.profile_classID);
        }
    }

    @NonNull
    @Override
    public Student_Profile_Class_RecyclerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_class_card_layout, viewGroup, false);
        return new Student_Profile_Class_RecyclerList.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final String classTitleID = classList.get(i);
        Log.i("In Recycler","Added" + classTitleID);
        //parse the input String (Title + " " + ID)
        String[] classTitle_ID = classTitleID.split("@");
        String classTitle = classTitle_ID[0];
        String classID = classTitle_ID[1];
        myViewHolder.className.setText(classTitle);
        myViewHolder.classID.setText(classID);
    }


    @Override
    public int getItemCount() {
        return classList.size();
    }

}
