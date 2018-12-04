package com.cs121.tmtm.nav_bar_testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class PostComment_Activity extends AppCompatActivity {
    EditText ReviewText;
    RatingBar RatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);
        ReviewText = findViewById(R.id.ReviewText);
    }

    /**
     * the user wants to submit his/her own review
     * return review as a string to RestaurantInfo(include onSuccess Code)
     *
     * @param view
     */
    public void submitReview(View view) {
        String inputReview = ReviewText.getText().toString().trim();
        if (inputReview.equals("")) {
            Toast.makeText(this,
                    "Please enter a valid review.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userComment", inputReview);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * the user wants to cancel this session
     * return to RestaurantInfo with no return string form this activity
     *
     * @param view
     */
    public void cancelReview(View view) {
        finish();
    }
}
