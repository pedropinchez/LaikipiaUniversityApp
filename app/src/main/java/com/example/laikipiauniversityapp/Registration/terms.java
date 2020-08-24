package com.example.laikipiauniversityapp.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.laikipiauniversityapp.MainActivity;
import com.example.laikipiauniversityapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class terms extends AppCompatActivity {
    CheckBox check;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        check=findViewById(R.id.check);
        start=findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked())
                {
                    startActivity(new Intent(getApplicationContext(), signup.class));
                }
                else
                {
                    Toast.makeText(terms.this, "Please accept our terms and condition", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(terms.this, signup.class));

        }
    }

    public void Proceed(View view) {


        startActivity(new Intent(terms.this, signup.class));

    }

}
