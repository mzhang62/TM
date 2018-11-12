package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Project_RecyclerList extends RecyclerView.Adapter<Project_RecyclerList.MyViewHolder> {

    private List<ProjectObject> projectList;
    private Context context;

    public Project_RecyclerList(List<ProjectObject> projectList) {
        this.projectList = projectList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName;
        private TextView projectStatus;
        public MyViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.projectNameText);
            projectStatus = (TextView) itemView.findViewById(R.id.projectStatusText);
        }
    }

    @NonNull
    @Override
    public Project_RecyclerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_card_layout, viewGroup,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Project_RecyclerList.MyViewHolder myViewHolder, int i) {
        ProjectObject this_project = projectList.get(i);
        String projectName = this_project.getProjectName();
        myViewHolder.projectName.setText(projectName);
        String projectStatus = this_project.getProjectAcceptedStatus();
        myViewHolder.projectStatus.setText(projectStatus);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}
