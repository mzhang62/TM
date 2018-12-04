package com.cs121.tmtm.nav_bar_testing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link create_class.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link create_class#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_class extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public create_class() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_class.
     */
    // TODO: Rename and change types and number of parameters
    public static create_class newInstance(String param1, String param2) {
        create_class fragment = new create_class();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_class, container, false);
        Button createButton = (Button) rootView.findViewById(R.id.create);
        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        createButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
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
                addClassToDB();
                break;
            case R.id.cancel:
                FragmentTransaction cancel = getFragmentManager().beginTransaction();
                cancel.replace(R.id.flContent, new Instructor_MyCourses_fragment());
                cancel.commit();
                break;
        }
    }
    public void addClassToDB() {
        DatabaseReference classReference = FirebaseDatabase.getInstance().getReference("Class");
        DatabaseReference instructorReference = FirebaseDatabase.getInstance().getReference("Instructor");

        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        View rootView = getView();
        EditText classTitle = (EditText) rootView.findViewById(R.id.enter_name);
        EditText classDescription = (EditText) rootView.findViewById(R.id.enter_des);
        String classID = classReference.push().getKey();
        String className = classTitle.getText().toString().trim();
        String classDes = classDescription.getText().toString().trim();
        if (classDes.equals("") || className.equals("")) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<String> instructor_array = new ArrayList<>();
        ArrayList<String> student_array = new ArrayList<>();
        ArrayList<String> project_array = new ArrayList<>();
        ClassObject this_class = new ClassObject(classID, className,classDes, instructor_array, student_array,project_array);
        classReference.child(classID).setValue(this_class);
        classReference.child(classID).child("classInstructor").child(user.getUid()).setValue(true);
        instructorReference.child(user.getUid()).child("myClass").child(classID).setValue(className);
        Toast.makeText(getActivity(), "You've created a new class!", Toast.LENGTH_SHORT).show();
        clearFields();
        FragmentTransaction fr = getFragmentManager().beginTransaction();
        fr.replace(R.id.flContent, new Instructor_MyCourses_fragment());
        fr.commit();
    }

    public void clearFields() {
        View rootView = getView();
        EditText projectTitle = (EditText) rootView.findViewById(R.id.enter_name);
        EditText projectDescription = (EditText) rootView.findViewById(R.id.enter_des);
        projectTitle.getText().clear();
        projectDescription.getText().clear();
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
