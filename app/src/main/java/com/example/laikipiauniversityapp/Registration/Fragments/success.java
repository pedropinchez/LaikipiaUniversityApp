package com.example.laikipiauniversityapp.Registration.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.laikipiauniversityapp.HomeActivity;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Utils.Constants;
import com.example.laikipiauniversityapp.Utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class success extends Fragment {

    TextInputEditText username;
    Button buttonContinue;
    ProgressDialog progressDialog;
    SharedPref sharedPref;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_success, null);
        super.onCreate(savedInstanceState);
        username = root.findViewById(R.id.guest_username);
        buttonContinue = root.findViewById(R.id.buttonContinue);

        sharedPref = new SharedPref(getActivity());

        progressDialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();

        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        final String email = "k" + android_id + Constants.EMAIL_EXTENSION;

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = username.getText().toString();

                if (TextUtils.isEmpty(name)) {


                    username.setError("Please fill this field");


                } else {

                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Logging you in as a guest");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                    createuser(email, android_id, name);

                }
            }
        });


        tryTosignIn(email, android_id);



        return  view;
    }
    private void tryTosignIn(String useremail, String userpassword) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            sharedPref.setIsGuest(true);

                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                            DocumentReference documentReference = firebaseFirestore.collection("Users").document(mAuth.getCurrentUser().getUid());
                            documentReference.get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        if (task.getResult().exists() && task.getResult().exists()) {
                                            String nameDB = task.getResult().get("name").toString();
                                            String referralCode = task.getResult().get("referralCode").toString();

                                            sharedPref.setIsGuest(true);
                                            sharedPref.setGuestUsername(nameDB);
                                            sharedPref.setReferralCode(referralCode);



                                            FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                            });


                                        }
                                    }
                                }
                            });





                        } else {

                            progressDialog.dismiss();

                        }
                    }
                });


    }


    private void createuser(final String email, final String password, final String name) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            saveUserDetails(name);


                        } else {
                            if (task.getException().toString().contains("already in use")) {
                                signin(email, password, name);
                            } else {


                                progressDialog.dismiss();

                                Toast.makeText(getActivity(), "Something wrong happened :(", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });

    }

    private void signin(String email, String password, final String name) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    sharedPref.setGuestUsername(name);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String generateRandomString(int lenght) {
        if (lenght > 0) {
            StringBuilder stringBuilder = new StringBuilder(lenght);
            String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
            String CHAR_UPPER = CHAR_LOWER.toUpperCase();
            String NUMBER = "0123456789";

            String all_characters = CHAR_LOWER + CHAR_UPPER + NUMBER;
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0; i < lenght; i++) {
                int randomCharAt = secureRandom.nextInt(all_characters.length());
                char randomChar = all_characters.charAt(randomCharAt);

                stringBuilder.append(randomChar);
            }

            return stringBuilder.toString();
        } else {
            return "0";
        }
    }

    private void saveUserDetails(final String name) {

        final CollectionReference referralCodeReferences = FirebaseFirestore.getInstance().collection("referralCodes");

        final String key = generateRandomString(8);

        referralCodeReferences.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        saveUserDetails(name);
                    } else {
                        Map<String, Object> details = new HashMap<>();
                        details.put("uid", mAuth.getCurrentUser().getUid());

                        referralCodeReferences.document(key).set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("referralCode", key);

                                FirebaseFirestore.getInstance().collection("Users").document(mAuth.getCurrentUser().getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                sharedPref.setIsGuest(true);
                                                sharedPref.setGuestUsername(name);
                                                sharedPref.setReferralCode(key);


                                                FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        progressDialog.dismiss();

                                                        startActivity(new Intent(getActivity(), HomeActivity.class));

                                                    }
                                                });


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                                            }
                                        });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
