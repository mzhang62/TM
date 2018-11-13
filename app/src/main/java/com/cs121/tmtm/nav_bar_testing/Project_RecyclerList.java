package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.media.Image;
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
    private static final int APPROVED = 1;
    private static final int DENIED = -1;
    private static final int PENDING = 0;

    public Project_RecyclerList(List<ProjectObject> projectList) {
        this.projectList = projectList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName;
        private ImageView projectStatus;
        public MyViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.groupName);
            projectStatus = (ImageView) itemView.findViewById(R.id.statusIMG);
        }
    }

    @NonNull
    @Override
    public Project_RecyclerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.instructor_project_card_layout, viewGroup,false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Project_RecyclerList.MyViewHolder myViewHolder, int i) {
        final ProjectObject this_project = projectList.get(i);
        String projectName = this_project.getProjectName();
        myViewHolder.projectName.setText(projectName);
        int projectStatus = this_project.getProjectAcceptedStatus();
        if(projectStatus == APPROVED){
            myViewHolder.projectStatus.setImageResource(R.drawable.approved_icon);
        }
        else if (projectStatus == DENIED){
            myViewHolder.projectStatus.setImageResource(R.drawable.denied_icon);
        }
        else {
            myViewHolder.projectStatus.setImageResource(R.drawable.pending_icon);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}
