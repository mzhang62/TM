package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.os.Bundle;
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


public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button loginButton;
    private Button signupButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = (EditText) findViewById(R.id.emailEditText) ;
        passwordEditText = (EditText) findViewById(R.id.passwordEditText) ;

        signupButton = (Button) findViewById(R.id.signupButton);
        loginButton = (Button) findViewById(R.id.loginButton);

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                navigateToActivity(signupActivity.class);
            }
        });



        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser currentUser = mAuth.getCurrentUser();



        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                if (emailEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(loginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (passwordEditText.getText().toString().trim().length() == 0) {
                    Toast.makeText(loginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
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
                                        finish();

                                    } else {
                                        Log.w("Login", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(loginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });



    }

    @Override
    public void onBackPressed() {

    }

    public void navigateToActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}
