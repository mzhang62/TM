package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link create_project.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link create_project#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_project extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PENDING = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> projectIDArray;
    private List<String> spinnerArray;
    private Spinner my_class;


    public create_project() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_project.
     */
    // TODO: Rename and change types and number of parameters
    public static create_project newInstance(String param1, String param2) {
        create_project fragment = new create_project();
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
        final View rootView = inflater.inflate(R.layout.fragment_create_project, container, false);
        Button createButton = (Button) rootView.findViewById(R.id.create);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        spinnerArray = new ArrayList<>();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference("Student");
        final DatabaseReference classReference = FirebaseDatabase.getInstance().getReference("Class");
        my_class = rootView.findViewById(R.id.my_Class);
        //inflate the class names from DB into the spinner
        studentReference.child(user.getUid()).child("myClass").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerArray.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot myClass : dataSnapshot.getChildren()){
                        String classID = myClass.getKey();
                        classReference.child(classID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String classTitle = dataSnapshot.child("classTitle").getValue(String.class);
                                    String classID = dataSnapshot.child("classID").getValue(String.class);
                                    String displayClassName = classTitle + " " + classID;
                                    spinnerArray.add(displayClassName);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        rootView.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                my_class.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
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

    @Override
    public void onClick(View view) {
        //create and cancel onClick handler
        switch (view.getId()) {
            case R.id.create:
                addProjectToDB();
                break;
            case R.id.cancel:
                FragmentTransaction cancel = getFragmentManager().beginTransaction();
                cancel.replace(R.id.flContent, new Student_MyCourses_fragment());
                cancel.commit();
                break;
        }
    }

    public void addProjectToDB() {
        final FirebaseUser user = mAuth.getInstance().getCurrentUser();
        //database references
        final DatabaseReference projectReference = FirebaseDatabase.getInstance().getReference("Projects");
        DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference("Student");
        DatabaseReference classReference = FirebaseDatabase.getInstance().getReference("Class");
        //get the rootView of this fragment
        View rootView = getView();
        //link the UI elements
        EditText projectTitle = (EditText) rootView.findViewById(R.id.enter_name);
        EditText projectDescription = (EditText) rootView.findViewById(R.id.enter_des);
        EditText projectMaxMembers = (EditText) rootView.findViewById(R.id.enter_capacity);
        Spinner projectClassID = (Spinner) rootView.findViewById(R.id.my_Class);
        //add project under "Project" branch
        ArrayList<String> members = new ArrayList<>();
        //add the current user under the project list
        //members.add(user.getUid());
        //get project info from user entered fields
        final String projectID = projectReference.push().getKey();
        String projectName = projectTitle.getText().toString().trim();
        String StringMax = projectMaxMembers.getText().toString().trim();
        String projectDes = projectDescription.getText().toString().trim();
        //get the selected item from the spinner;
        String[] selectedClass = projectClassID.getSelectedItem().toString().split(" ");
        String selectedClassName = selectedClass[0];
        String selectedClassID = selectedClass[1];
        //check if any of the fields are empty
        if (projectDes.equals("") || projectName.equals("") || StringMax.equals("")) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        int projectMax = Integer.parseInt(StringMax);
        ProjectObject this_project = new ProjectObject(projectID, projectName, PENDING, projectDes, projectMax);
        //add the project object under the Project branch
        projectReference.child(projectID).setValue(this_project);
        //add current user's ID and nickname under the member branch of this new project
        studentReference.child(user.getUid()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check if the user have a valid nickname
                if(dataSnapshot.exists()){
                    String userNickname = dataSnapshot.getValue(String.class);
                    projectReference.child(projectID).child("projectMembers").child(user.getUid()).setValue(userNickname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //add project record into user's own branch
        studentReference.child(user.getUid()).child("myProject").child(projectID).setValue(selectedClassID);
        Log.i("Spinner value", "The selected value is " + selectedClassID);
        //add the project ID under the class branch
        classReference.child(selectedClassID).child("classProject").child(projectID).setValue(true);

        Toast.makeText(getActivity(), "You've created a new project!", Toast.LENGTH_SHORT).show();
        clearFields();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.flContent, new Student_MyCourses_fragment());
        fr.commit();

    }

    public void clearFields() {
        View rootView = getView();
        EditText projectTitle = (EditText) rootView.findViewById(R.id.enter_name);
        EditText projectDescription = (EditText) rootView.findViewById(R.id.enter_des);
        EditText projectMaxMembers = (EditText) rootView.findViewById(R.id.enter_capacity);
        projectTitle.getText().clear();
        projectDescription.getText().clear();
        projectMaxMembers.getText().clear();
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
