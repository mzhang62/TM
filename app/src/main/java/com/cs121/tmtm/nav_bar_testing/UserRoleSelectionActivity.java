package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Starter page for login. Let the users to select their roles: instructors or students.
 * Jump to login page after user click one of the button
 */
public class UserRoleSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginInstructor;
    Button loginStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role_selection);

        loginInstructor = findViewById(R.id.loginInstructor);
        loginStudent = findViewById(R.id.loginStudent);
        loginInstructor.setOnClickListener(this);
        loginStudent.setOnClickListener(this);
    }

    //jump to the actual login page(loginActivity.java) and pass in user role information as intent
    // into that page
    @Override
    public void onClick(View view) {
        if (view == loginInstructor) {
            Intent intent = new Intent(UserRoleSelectionActivity.this, loginActivity.class);
            intent.putExtra("user", "instructor");
            startActivity(intent);
        }
        if (view == loginStudent) {
            Intent intent = new Intent(UserRoleSelectionActivity.this, loginActivity.class);
            intent.putExtra("user", "student");
            startActivity(intent);
        }
    }
//    @Override
//    public void onBackPressed() {
//
//    }

}
