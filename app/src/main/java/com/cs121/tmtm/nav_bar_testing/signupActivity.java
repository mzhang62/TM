package com.cs121.tmtm.nav_bar_testing;

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

public class signupActivity extends AppCompatActivity {
    private EditText email2EditText;
    private EditText password2EditText;
    private EditText password3EditText;
    private EditText nameEditText;
    private Button signupButton;
    private FirebaseAuth mAuth;

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


        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                if (email2EditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(signupActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (password2EditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(signupActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else if (!password2EditText.getText().toString().equals(password3EditText.getText().toString())) {
                    Toast.makeText(signupActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                } else {
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
                                        addUserToDB();
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

    public void addUserToDB() {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        String user_email = email2EditText.getText().toString().trim();
        String user_name = nameEditText.getText().toString().trim();
        String projectID = userReference.push().getKey();
        UserObject user = new UserObject(user_name, user_email, "UCSC", "coding", "dancing");
        userReference.child(projectID).setValue(user);
        Toast.makeText(signupActivity.this, "You've created a new project!", Toast.LENGTH_SHORT).show();
    }
}
