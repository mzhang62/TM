package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentJoinProject_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentJoinProject_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentJoinProject_Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser this_user = mAuth.getCurrentUser();
    private DatabaseReference classReference;
    private DatabaseReference studentReference;

    private OnFragmentInteractionListener mListener;

    public StudentJoinProject_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentJoinProject_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentJoinProject_Fragment newInstance(String param1, String param2) {
        StudentJoinProject_Fragment fragment = new StudentJoinProject_Fragment();
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
        View rootView = inflater.inflate(R.layout.fragment_student_join_project_, container, false);
        Button joinButton = (Button) rootView.findViewById(R.id.joinProjectBtn);
        joinButton.setOnClickListener(this);
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
        switch (view.getId()) {
            case R.id.joinProjectBtn:
                addStudentToProject();
                break;
        }
    }

    //add the current student's uid and name under the projectMember branch
    //key: uid ; value: name
    //also need to add the project under the student's myProject branch
    //key: project uid ; value: class uid
    private void addStudentToProject() {
        View rootView = getView();
        EditText project_Code_from_user = (EditText) rootView.findViewById(R.id.codeText);
        final String project_Code = project_Code_from_user.getText().toString();
        classReference = FirebaseDatabase.getInstance().getReference("Class");
        studentReference = FirebaseDatabase.getInstance().getReference("Student").child(this_user.getUid());
        classReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //first determine which class does this project belong to
                String class_id = "";
                String class_title = "";
                for (DataSnapshot this_class : dataSnapshot.getChildren()) {
                    if (this_class.child("classProject").hasChild(project_Code)) {
                        class_id = this_class.getKey();
                        class_title = this_class.child("classTitle").getValue(String.class);
                        break;
                    }
                }
                FirebaseDatabase.getInstance().getReference("Student").child(this_user.getUid())
                        .child("myProject").child(project_Code).setValue(class_id);
                //get the current student's name from DB
                studentReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String student_name = dataSnapshot.child("name").getValue(String.class);
                            FirebaseDatabase.getInstance().getReference("Projects").child(project_Code)
                                    .child("projectMembers").child(this_user.getUid()).setValue(student_name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getActivity(), "You've been added to the project.", Toast.LENGTH_SHORT).show();
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
