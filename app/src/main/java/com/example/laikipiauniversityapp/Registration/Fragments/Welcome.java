package com.example.laikipiauniversityapp.Registration.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Registration.NonSwipeableViewPager;
import com.example.laikipiauniversityapp.Registration.ViewPagerAdapter;
import com.example.laikipiauniversityapp.Registration.terms;
import com.google.firebase.auth.FirebaseAuth;

public class Welcome extends Fragment {
Button proceed;
    private int currentStep = 0;
    private int CurrentItem=0;
    ViewPagerAdapter viewPagerAdapter;
    NonSwipeableViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_welcome, null);
        super.onCreate(savedInstanceState);
        proceed = (Button) root.findViewById(R.id.proceed);
        CurrentItem=viewPager.getCurrentItem();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentItem=viewPager.getCurrentItem();
                currentStep++;
            }
        });

        return  view;
    }


}
