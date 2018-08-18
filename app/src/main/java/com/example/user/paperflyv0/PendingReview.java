package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PendingReview extends AppCompatActivity {

    Button pr;
    EditText prEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_review);

        pr=findViewById(R.id.btn_pr);
        prEditText = findViewById(R.id.pr_edit);

    }

    public void gotoMainScreen(View view){
        Intent pr_intent = new Intent (PendingReview.this,PickUpActivity.class);
        startActivity(pr_intent);
        finish();
    }
}
