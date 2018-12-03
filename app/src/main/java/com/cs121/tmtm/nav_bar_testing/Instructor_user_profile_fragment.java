package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * {@link Instructor_user_profile_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Instructor_user_profile_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Instructor_user_profile_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private TextView instructorName;
    private TextView instructorID;
    private RecyclerView classView;
    private RecyclerView projectView;
    private DatabaseReference instructorReference;
    private DatabaseReference projectReference;
    private ArrayList<String> classes;
    private ArrayList<String> projectIDs;
    private ArrayList<String> projects;

    private OnFragmentInteractionListener mListener;

    public Instructor_user_profile_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_user_profile_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Instructor_user_profile_fragment newInstance(String param1, String param2) {
        Instructor_user_profile_fragment fragment = new Instructor_user_profile_fragment();
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
        View rootView = inflater.inflate(R.layout.fragment_instructor_user_profile, container, false);
        FirebaseUser this_user = mAuth.getInstance().getCurrentUser();
        classView = (RecyclerView) rootView.findViewById(R.id.profile_classView);
        classView.setLayoutManager(new LinearLayoutManager(getActivity()));
        instructorName = (TextView) rootView.findViewById(R.id.instructorName);
        instructorID = (TextView) rootView.findViewById(R.id.instructorID);
        instructorName.setText(this_user.getEmail());
        instructorID.setText(this_user.getUid());


        instructorReference = FirebaseDatabase.getInstance().getReference("Instructor");
        classes = new ArrayList<>();
        //display the class recycler list
        instructorReference.child(this_user.getUid()).child("myClass").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classes.clear();
                for(DataSnapshot myClasses : dataSnapshot.getChildren()){
                    String classID = myClasses.getKey();
                    String classTitle = myClasses.getValue(String.class);
                    String classTitleID = classTitle + "@" + classID;
                    classes.add(classTitleID);
                    Log.i("Instructor class", "Add class: " + classTitleID);
                }
                Student_Profile_Class_RecyclerList adaptor = new Student_Profile_Class_RecyclerList(classes);
                classView.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        classView.setItemAnimator(new DefaultItemAnimator());
        //display the project recycler list
        projects = new ArrayList<>();
        instructorReference.child(this_user.getUid()).child("myProject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectIDs = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot myProjects : dataSnapshot.getChildren()){
                        String projectID = myProjects.getKey();
                        projectIDs.add(projectID);
                        Log.i("myProject","Added : " + projectID);
                    }
                    projects.clear();
                    projectReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(String this_ID : projectIDs){
                                    String projectName = dataSnapshot.child(this_ID).child("projectName").getValue(String.class);
                                    String projectNameID = projectName + "@" + this_ID;
                                    projects.add(projectNameID);
                                }
                                //set adaptor
                                Student_Profile_Class_RecyclerList adaptor = new Student_Profile_Class_RecyclerList(projects);
                                projectView.setAdapter(adaptor);
                            }
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
