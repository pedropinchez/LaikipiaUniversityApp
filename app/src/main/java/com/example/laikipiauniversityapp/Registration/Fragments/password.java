package com.example.laikipiauniversityapp.Registration.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Utils.SharedPref;
import com.google.android.material.textfield.TextInputEditText;

public class password extends Fragment {

    TextInputEditText editTextPassword;
    Button buttonSignIn;


    Boolean firstTime = true;

    String regnumber;
    ProgressDialog progressDialog;
    SharedPref sharedPref;


    int num = 1;

    String login_url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_password, null);
        super.onCreate(savedInstanceState);

        editTextPassword = root.findViewById(R.id.editTextPortalPassword);
        buttonSignIn = root.findViewById(R.id.buttonSignIn);
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("getPortalFullName", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView fullname = root.findViewById(R.id.full_username);
        String fullnames = sharedPref.getString("getPortalFullName", "Name");
        String regno = sharedPref.getString("regno", "N**/*/*/01*");




        progressDialog = new ProgressDialog(getActivity());

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = editTextPassword.getText().toString().trim();

                if (password.isEmpty()) {

                    editTextPassword.setError("Please provide a valid password");
                    editTextPassword.requestFocus();
                    return;
                }



                new GetFullNameFirst(getActivity(), regnumber, password,buttonSignIn).execute();

            }
        });



        return  view;
    }
}
