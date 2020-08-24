package com.example.laikipiauniversityapp.Registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.laikipiauniversityapp.MainActivity;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Registration.Fragments.Welcome;
import com.example.laikipiauniversityapp.Registration.Fragments.password;
import com.example.laikipiauniversityapp.Registration.Fragments.photoselection;
import com.example.laikipiauniversityapp.Registration.Fragments.regno;
import com.example.laikipiauniversityapp.Registration.Fragments.success;
import com.facebook.login.LoginFragment;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;


public class signup extends AppCompatActivity {
    private int currentStep = 0;
    private int CurrentItem=0;
    Button btn_init;

    ViewPagerAdapter viewPagerAdapter;
    NonSwipeableViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final StepView stepView = findViewById(R.id.step_view);
        viewPager=findViewById(R.id.viewpager);
        btn_init=findViewById(R.id.btn_init);
        if (ContextCompat.checkSelfPermission(signup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new Welcome(), "Welcome");
        viewPagerAdapter.addFrag(new regno(), "regno");
        viewPagerAdapter.addFrag(new password(), "password");
        viewPagerAdapter.addFrag(new photoselection(), "photoselection");
        viewPagerAdapter.addFrag(new success(), "success");

        viewPager.setAdapter(viewPagerAdapter);
        //viewPager.setCurrentItem(0);
        CurrentItem=viewPager.getCurrentItem();




        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                Toast.makeText(signup.this, "Step " + step, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                    CurrentItem++;

                    viewPager.setCurrentItem(CurrentItem);
                } else {
                    stepView.done(true);
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    CurrentItem--;

                    viewPager.setCurrentItem(CurrentItem);
                }
                stepView.done(false);
                stepView.go(currentStep, true);
            }
        });
        findViewById(R.id.btn_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> steps = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    steps.add("Step " + (i + 1));
                    btn_init.setVisibility(View.GONE);

                }
                stepView.setSteps(steps);
            }

        });

    }
}
