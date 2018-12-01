package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Instructor_Class_RecyclerList extends RecyclerView.Adapter<Instructor_Class_RecyclerList.MyViewHolder> {

    private List<String> classList;
    private Context context;

    public Instructor_Class_RecyclerList(List<String> classList) {
        this.classList = classList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView className;
        public MyViewHolder(View itemView) {
            super(itemView);
            className = (TextView) itemView.findViewById(R.id.classTitle);
        }
    }

    @NonNull
    @Override
    public Instructor_Class_RecyclerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.class_card_layout, viewGroup,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Instructor_Class_RecyclerList.MyViewHolder myViewHolder, int i) {
        final String classTitleID = classList.get(i);
        //parse the input String (Title + " " + ID)
        String[] classTitle_ID = classTitleID.split(" ");
        String classTitle = classTitle_ID[0];
        final String classID = classTitle_ID[1];

        myViewHolder.className.setText(classTitle);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO link to student_version_project page
                Instructor_project_page_fragment project_fragment = new Instructor_project_page_fragment();
                Bundle args = new Bundle();
                args.putString("classID", classID);
                project_fragment.setArguments(args);
                //inflate the fragment
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, project_fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }
}
