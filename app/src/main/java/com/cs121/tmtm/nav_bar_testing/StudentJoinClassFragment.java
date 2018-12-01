package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
 * {@link StudentJoinClassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentJoinClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentJoinClassFragment extends Fragment implements View.OnClickListener {
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

    private OnFragmentInteractionListener mListener;

    public StudentJoinClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstructorJoinClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentJoinClassFragment newInstance(String param1, String param2) {
        StudentJoinClassFragment fragment = new StudentJoinClassFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_join_class, container, false);
        Button joinButton = (Button) rootView.findViewById(R.id.joinClassBtn);
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
            case R.id.joinClassBtn:
                addStudentToClass();
                break;
        }
    }

    private void addInstructorToClass() {
        View rootView = getView();
        EditText classCode = (EditText) rootView.findViewById(R.id.codeText);
        String class_Code = classCode.getText().toString();

        FirebaseDatabase.getInstance().getReference("Instructor").child(this_user.getUid())
                .child("myClass").child(class_Code).setValue(true);

        FirebaseDatabase.getInstance().getReference("Class").child(class_Code)
                .child("classInstructor").child(this_user.getUid()).setValue(true);
        Toast.makeText(getActivity(), "You've been added to the class!!!!", Toast.LENGTH_SHORT).show();
    }

    private void addStudentToClass() {
        View rootView = getView();
        EditText classCode = (EditText) rootView.findViewById(R.id.codeText);
        final String class_Code = classCode.getText().toString();
        classReference = FirebaseDatabase.getInstance().getReference("Class").child(class_Code);
        classReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String classTitle = dataSnapshot.child("classTitle").getValue(String.class);
                    FirebaseDatabase.getInstance().getReference("Student").child(this_user.getUid())
                            .child("myClass").child(class_Code).setValue(classTitle);

                    FirebaseDatabase.getInstance().getReference("Class").child(class_Code)
                            .child("classStudent").child(this_user.getUid()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toast.makeText(getActivity(), "You've been added to the class as a student.", Toast.LENGTH_SHORT).show();

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
