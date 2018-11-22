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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * The "actual" login page of the app. User would be directed to this page after selecting his/her
 * role. This activity got user preferences from intent extra "user".
 * After successfully log in, the user would be directed to the respective homepage for his/her role.
 */
public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference instructorReference;
    private DatabaseReference studentReference;
    private Button loginButton;
    private Button signupButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fetch intent extra from roleSelectionPage
        userRole = getIntent().getStringExtra("user");
        Log.i("user", "Current user role in login is: " + userRole);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        signupButton = (Button) findViewById(R.id.signupButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        //change signup text with respect to user role
        if ("student".equals(userRole)) {
            signupButton.setText("Create a new Student Account");
        } else if ("instructor".equals(userRole)) {
            signupButton.setText("Create a new Instructor Account");
        }
        //navigate to the Sign Up page after the user clicked the 'Sign Up' button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                navigateToActivity(signupActivity.class, userRole);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentlyLoggedIn()){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            navigateToMainPage(currentUser);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                //first check if all the fields are entered correctly
                if (emailEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(loginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (passwordEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(loginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {    //begin login action
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Login", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(loginActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();
                                        //check respective user role's validity through firebase
                                        if("student".equals(userRole)){
                                            checkStudentValidity(user);

                                        }
                                        else if("instructor".equals(userRole)){
                                            checkInstructorValidity(user);

                                        }
                                        //finish();
                                    } else {
                                        Log.w("Login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(loginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void navigateToMainPage(FirebaseUser currentUser) {
        instructorReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if the current user is an instructor navigate to the instructor page
                if(dataSnapshot.exists()){
                    navigateToActivity(Instructor_MainActivity.class);
                    finish();
                }
                else{   //if the current user is a student navigate to the student page
                    navigateToActivity(Student_MainActivity.class);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean currentlyLoggedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null ){
            return false;
        }
        return true;
    }
    private void navigateToActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    //check student account validity through firebase "Student" branch
    private void checkStudentValidity(FirebaseUser user) {
        studentReference = FirebaseDatabase.getInstance().getReference("Student");
        studentReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if this student user exists
                if(dataSnapshot.exists()){
                    Toast.makeText(loginActivity.this, "This is a student account!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, Student_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(loginActivity.this, "This is not a student account!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //check instructor account validity through firebase "instructor" brach
    private void checkInstructorValidity(FirebaseUser user){
        instructorReference = FirebaseDatabase.getInstance().getReference("Instructor");
        instructorReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if this student user exists
                if(dataSnapshot.exists()){
                    Toast.makeText(loginActivity.this, "This is an instructor account!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity.this, Instructor_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(loginActivity.this, "This is not an instructor account!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void navigateToActivity(Class c, String userRole) {
        Intent intent = new Intent(this, c);
        intent.putExtra("user", userRole);
        startActivity(intent);
    }

}
