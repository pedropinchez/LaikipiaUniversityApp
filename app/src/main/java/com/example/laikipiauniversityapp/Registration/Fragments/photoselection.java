package com.example.laikipiauniversityapp.Registration.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.laikipiauniversityapp.HomeActivity;
import com.example.laikipiauniversityapp.R;
import com.example.laikipiauniversityapp.Utils.Constants;
import com.example.laikipiauniversityapp.Utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class photoselection extends Fragment {

    private Button signup;
    private ImageView avatar;
    private Uri mainImageUri;
    private FirebaseAuth mAuth;
    private ProgressDialog pdialog;
    private StorageReference storageReference;
    public static final int PICK_IMAGE = 1;

    private SharedPref sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photoselection, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_photoselection, null);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        avatar = root.findViewById(R.id.avatar);
        signup = root.findViewById(R.id.registerButton);
        pdialog = new ProgressDialog(getActivity());


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("getPortalFullName", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView fullname = root.findViewById(R.id.full_username);
        String fullnames = sharedPref.getString("getPortalFullName", "Name");



        fullname.setText(fullnames);



        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        pickimage();
                    }
                } else {
                    pickimage();
                }
            }
        });

        String regno = sharedPref.getString("getRegNumber", "regno");
        String passwordString = sharedPref.getString("getPortalPassword", "password");


        tryTosignIn(regno,passwordString );

        final SharedPreferences finalSharedPref = sharedPref;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String regno = finalSharedPref.getString("getRegNumber", "regno");
                String passwordString = finalSharedPref.getString("getPortalPassword", "password");
                String name = finalSharedPref.getString("getPortalFullName", "Name");


                if (mainImageUri == null) {
                    Toast.makeText(getActivity(), "Please tap on the image icon and select your profile picture.", Toast.LENGTH_LONG).show();
                } else {
                    pdialog.setMessage("Signing up...");
                    pdialog.setIndeterminate(true);
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.setCancelable(false);
                    pdialog.show();


                    regno = regno +  Constants.EMAIL_EXTENSION;
                    createuser(regno, passwordString, name);

                }

            }
        });
        return  view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                mainImageUri = data.getData();
                CropImage.activity(mainImageUri).setGuidelines(CropImageView.Guidelines.ON).start(getActivity());

            } else {
                Toast.makeText(getActivity(), "Unable to load Image", Toast.LENGTH_LONG).show();
            }
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mainImageUri = result.getUri();
                avatar.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception er = result.getError();
            }

            super.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void createuser(final String email, final String password, final String name) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signin(email, password);
                        } else {
                            if (task.getException().toString().contains("already in use")) {
                                signin(email, password);
                                Toast.makeText(getActivity(), "You already have an account. Signing you in", Toast.LENGTH_LONG).show();
                                Toast.makeText(getActivity(), "Your profile picture will be updated", Toast.LENGTH_LONG).show();
                            } else {
                                pdialog.dismiss();
                                Toast.makeText(getActivity(), "Your profile picture will be updated", Toast.LENGTH_LONG).show();
                            }
                        }
                        }
                    });
                }












        private void signin(String email, String password){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        uploadimage();
                    } else {
                        pdialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    public void uploadimage() {
        final String userid = mAuth.getCurrentUser().getUid();


        if (mainImageUri != null) {

            final StorageReference filePath = storageReference.child("profile_images/" + userid + ".png");

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mainImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload your profile picture. Please try again", Toast.LENGTH_SHORT).show();

                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            String regno = sharedPref.getRegNumber();
                            String name = sharedPref.getPortalFullName();
                            updateprofile(name, url, regno);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to upload your profile picture. Please try again", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });

        } else {
            Toast.makeText(getActivity(), "no image", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateprofile(final String name, final String url, final String regno) {
        UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(url))
                .build();

        mAuth.getCurrentUser().updateProfile(profileupdates).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            saveUserDetails(name, url, regno);
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                            pdialog.dismiss();
                        }
                    }
                }
        );

    }


    private String generateRandomString(int lenght){
        if (lenght > 0){
            StringBuilder stringBuilder = new StringBuilder(lenght);
            String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
            String CHAR_UPPER = CHAR_LOWER.toUpperCase();
            String NUMBER = "0123456789";

            String all_characters = CHAR_LOWER + CHAR_UPPER+ NUMBER;
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0; i < lenght; i++) {
                int randomCharAt = secureRandom.nextInt(all_characters.length());
                char randomChar = all_characters.charAt(randomCharAt);

                stringBuilder.append(randomChar);
            }

            return stringBuilder.toString();
        }else{
            return "0";
        }
    }

    private void saveUserDetails(final String name, final String url, final String reg_no) {

        final CollectionReference referralCodeReferences = FirebaseFirestore.getInstance().collection("referralCodes");

        final String key = generateRandomString(8);

        referralCodeReferences.document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        saveUserDetails(name,url,reg_no);
                    }else {
                        Map<String, Object> details = new HashMap<>();
                        details.put("uid", mAuth.getCurrentUser().getUid());

                        referralCodeReferences.document(key).set(details ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("url", url);
                                user.put("referralCode", key);
                                user.put("regno", reg_no);


                                FirebaseFirestore.getInstance().collection("Users").document(mAuth.getCurrentUser().getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        pdialog.dismiss();
                                                        sharedPref.setIsGuest(false);
                                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                        startActivity(intent);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                        startActivity(intent);
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

                }else {
                    Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void pickimage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }



    private void tryTosignIn(String useremail, String userpassword) {

        pdialog = new ProgressDialog(getActivity());
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(true);
        pdialog.setCancelable(false);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.show();


        useremail = useremail + Constants.EMAIL_EXTENSION;

        mAuth.signInWithEmailAndPassword(useremail, userpassword)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    pdialog.dismiss();

                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });


                        } else {
                            String message = task.getException().toString();
                            if (message.contains("password is invalid")) {
                                pdialog.dismiss();
                                Toast.makeText(getActivity(), "Email or Password is Incorrect", Toast.LENGTH_LONG).show();
                            } else if (message.contains("There is no user")) {
                                pdialog.dismiss();
                                Toast.makeText(getActivity(), "Select a profile picture", Toast.LENGTH_LONG).show();


                            } else {
                                pdialog.dismiss();
                            }
                        }
                    }
                });


    }

    }