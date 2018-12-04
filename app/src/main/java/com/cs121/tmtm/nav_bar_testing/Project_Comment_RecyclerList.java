package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Project_Comment_RecyclerList extends RecyclerView.Adapter<Project_Comment_RecyclerList.MyViewHolder>{
    private List<String> commentList;
    private Context context;

    public Project_Comment_RecyclerList(List<String> classList) {
        this.commentList = classList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView thisComment;
        public MyViewHolder(View itemView) {
            super(itemView);
            thisComment = (TextView) itemView.findViewById(R.id.thisComment);
        }
    }

    @NonNull
    @Override
    public Project_Comment_RecyclerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View listItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_card_layout, viewGroup,false);
        return new Project_Comment_RecyclerList.MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull Project_Comment_RecyclerList.MyViewHolder myViewHolder, int i) {
        String comment = commentList.get(i);
        myViewHolder.thisComment.setText(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}
