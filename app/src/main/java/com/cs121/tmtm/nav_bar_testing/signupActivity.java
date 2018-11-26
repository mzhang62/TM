package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class signupActivity extends AppCompatActivity {
    private EditText email2EditText;
    private EditText password2EditText;
    private EditText password3EditText;
    private EditText nameEditText;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email2EditText = (EditText) findViewById(R.id.email2EditText);
        password2EditText = (EditText) findViewById(R.id.password2EditText);
        password3EditText = (EditText) findViewById(R.id.password3EditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        signupButton = (Button) findViewById(R.id.signup2Button);
        mAuth = FirebaseAuth.getInstance();
        userRole = getIntent().getStringExtra("user");
        Log.i("user", "Current user role in signup is: " + userRole);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //check user-entered fields' validity
                if (email2EditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(signupActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (password2EditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(signupActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (!password2EditText.getText().toString().equals(password3EditText.getText().toString())) {
                    Toast.makeText(signupActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                } else { //start saving new user
                    String email = email2EditText.getText().toString();
                    String password = password3EditText.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(signupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Signup", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if ("student".equals(userRole)) {
                                            addStudentToDB(user);
                                        } else if ("instructor".equals(userRole)) {
                                            addInstructorToDB(user);
                                        }
                                        finish();
                                    } else {
                                        Log.w("Signup", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(signupActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addInstructorToDB(FirebaseUser user) {
        DatabaseReference instructorReference = FirebaseDatabase.getInstance().getReference("Instructor");
        String user_email = email2EditText.getText().toString().trim();
        String user_name = nameEditText.getText().toString().trim();
        String userID = user.getUid();
        ArrayList<String> myClass = new ArrayList<>();
        InstructorObject new_user = new InstructorObject(userID, user_name, user_email, myClass);
        instructorReference.child(userID).setValue(new_user);
        Toast.makeText(signupActivity.this, "You've created a new instructor account!", Toast.LENGTH_SHORT).show();
    }

    private void addStudentToDB(FirebaseUser user) {
        DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference("Student");
        String user_email = email2EditText.getText().toString().trim();
        String user_name = nameEditText.getText().toString().trim();
        String userID = user.getUid();
        ArrayList<String> myClass = new ArrayList<>();
        ArrayList<String> myProject = new ArrayList<>();
        StudentObject new_user = new StudentObject(userID,user_name, user_email, myClass, myProject);
        studentReference.child(userID).setValue(new_user);
        Toast.makeText(signupActivity.this, "You've created a new student account!", Toast.LENGTH_SHORT).show();
    }

}
