package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Instructor_project_page_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Instructor_project_page_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Instructor_project_page_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private DatabaseReference classReference;
    private DatabaseReference projectReference;
    private RecyclerView myView;
    private ArrayList<ProjectObject> projects;


    private OnFragmentInteractionListener mListener;

    public Instructor_project_page_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_project_page_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Instructor_project_page_fragment newInstance(String param1, String param2) {
        Instructor_project_page_fragment fragment = new Instructor_project_page_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseUser this_user = mAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_project_page_fragment, container, false);
        Bundle bundle = this.getArguments();
        String classID = null;
        if (bundle != null) {
            classID = bundle.get("classID").toString();
        }
        //reference to recyclerView
        myView = (RecyclerView) rootView.findViewById(R.id.student_project_view);
        myView.setLayoutManager(new LinearLayoutManager(getActivity()));
        classReference = FirebaseDatabase.getInstance().getReference("Class");
        projectReference = FirebaseDatabase.getInstance().getReference("Projects");
        projects = new ArrayList<>();
        //find all the projects under this class
        classReference.child(classID).child("classProject")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final ArrayList<String> projectIDs = new ArrayList<>();
                        //if there are projects in this class
                        if (dataSnapshot.exists()) {
                            //for each class ID inside this class
                            for (DataSnapshot myProjects : dataSnapshot.getChildren()) {
                                String this_project_ID = myProjects.getKey();
                                projectIDs.add(this_project_ID);

                            }
                            //if there is no project in this course yet
                            if(projectIDs.isEmpty()){
                                Toast.makeText(rootView.getContext(), "There are no project in this course.", Toast.LENGTH_SHORT).show();
                            }

                            //after fetching all the IDs, find them one by one under project ref
                            projectReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    projects.clear();
                                    //if there is such project
                                    for (String this_ID : projectIDs) {
                                        if (dataSnapshot.child(this_ID).exists()) {
                                            ProjectObject this_project = dataSnapshot.child(this_ID)
                                                    .getValue(ProjectObject.class);
                                            projects.add(this_project);

                                        }
                                    }
                                    Project_RecyclerList adaptor = new Project_RecyclerList(projects);
                                    myView.setAdapter(adaptor);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

//        projectReference = FirebaseDatabase.getInstance().getReference("Projects");
//        projects = new ArrayList<>();
//        projectReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                projects.clear();
//                for(DataSnapshot DBprojects : dataSnapshot.getChildren()){
//                    ProjectObject this_project =DBprojects.getValue(ProjectObject.class);
//                    projects.add(this_project);
//                }
//                Project_RecyclerList adaptor = new Project_RecyclerList(projects);
//                myView.setAdapter(adaptor);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        myView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
