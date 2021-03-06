package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link class_page.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link class_page#newInstance} factory method to
 * create an instance of this fragment.
 */
public class class_page extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int APPROVED = 1;
    private static final int DENIED = -1;
    private static final int PENDING = 0;
    private DatabaseReference projectReference;
    private ArrayList<ProjectObject> projects;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView myView;

    private OnFragmentInteractionListener mListener;

    public class_page() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment class_page.
     */
    // TODO: Rename and change types and number of parameters
    public static class_page newInstance(String param1, String param2) {
        class_page fragment = new class_page();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This onCreate is different than an Activity's onCreate method
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * All the views are declared here
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_class_page, container, false);
        //reference to recyclerView
        myView = (RecyclerView) rootView.findViewById(R.id.projectView);
        myView.setLayoutManager(new LinearLayoutManager(getActivity()));
        projectReference = FirebaseDatabase.getInstance().getReference("Projects");
        projects = new ArrayList<>();
        projectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects.clear();
                for(DataSnapshot DBprojects : dataSnapshot.getChildren()){
                    ProjectObject this_project =DBprojects.getValue(ProjectObject.class);
                    projects.add(this_project);
                }
                Project_RecyclerList adaptor = new Project_RecyclerList(projects);
                myView.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        String projectID = projectReference.push().getKey();
//        String projectName = "TA Master";
//        ArrayList<String> members = new ArrayList<>();
//        members.add("Haofan");
//        members.add("Michael");
//        members.add("Yun Duo");
//        members.add("Dun Dun");
//        int projectStatus = PENDING;
//        String projectDescription = "TA Master is a project management project to easy the life of instructors.  " +
//                "This APP makes the project approval/denial process much easier and quicker.";
//        int projectMaxMemeber = 4;
//        ProjectObject testingProject = new ProjectObject(projectID,projectName,members,projectStatus,projectDescription,projectMaxMemeber);
//        projects.add(testingProject);
//        projectReference.child(projectID).setValue(testingProject);

        myView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Treat this as the onCreate method in an activity
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
