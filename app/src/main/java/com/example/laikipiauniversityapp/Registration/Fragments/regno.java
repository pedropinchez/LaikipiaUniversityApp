package com.example.laikipiauniversityapp.Registration.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Utils.SharedPref;
import com.google.android.material.textfield.TextInputEditText;

public class regno extends Fragment {

    TextInputEditText editTextRegNo;
    SharedPref sharedPref;
    Button buttonContinue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regno, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_regno, null);
        super.onCreate(savedInstanceState);
        editTextRegNo = root.findViewById(R.id.editTextRegNumber);
        buttonContinue = root.findViewById(R.id.buttonContinue);

        sharedPref = new SharedPref(getActivity());
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("getPortalFullName", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name = sharedPref.getString("getPortalFullName", "Name");
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String regnumber = editTextRegNo.getText().toString().trim();

                if (regnumber.isEmpty()) {
                    editTextRegNo.setError("Please enter a valid reg number.");
                    editTextRegNo.requestFocus();
                    return;
                }
                Intent intent = new Intent(getActivity(), password.class);



            }
        });






        return  view;
    }
    public void Login_as_Guest(View view) {
        Intent intent = new Intent(getActivity(), success.class);
        startActivity(intent);
        startActivity(intent);
    }
}
