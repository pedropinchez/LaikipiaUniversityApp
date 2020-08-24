package com.example.laikipiauniversityapp.Registration.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AlertDialog;


import com.example.laikipiauniversityapp.Utils.Constants;
import com.example.laikipiauniversityapp.Utils.SharedPref;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;



public class GetFullNameFirst extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    protected Context context;
    private String username;
    private String password;
    static ProgressDialog progress_dialog;
    private String FULLNAME;
    @SuppressLint("StaticFieldLeak")
    private View view;


    public GetFullNameFirst(Context context, String username, String password, View view) {
        this.context = context;
        this.username = username;
        this.password = password;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress_dialog = new ProgressDialog(context);
        progress_dialog.setMessage("Trying to authenticate you. This may take time depending on the strength of your network.");
        progress_dialog.setTitle("Please wait...");
        progress_dialog.setCancelable(false);
        progress_dialog.setCanceledOnTouchOutside(false);
        progress_dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        username = username.toUpperCase();

        Connection.Response loginFormResponse;
        try {
            String login_url = Constants.PORTAL_URL;
            String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246";

            loginFormResponse = Jsoup.connect(login_url)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT)
                    .timeout(200 * 1000)
                    .execute();

            FormElement loginForm = (FormElement) loginFormResponse.parse()
                    .select("form").first();

// ## ... then "type" the username ...
            Element loginField = loginForm.select("#UserName").first();
            loginField.val(username);

// ## ... and "type" the password
            Element passwordField = loginForm.select("#Password").first();
            passwordField.val(password);


// # Now send the form for login
            Connection.Response loginActionResponse = loginForm.submit()
                    .cookies(loginFormResponse.cookies())
                    .userAgent(USER_AGENT)
                    .timeout(200 * 1000)
                    .execute();

            Document document = loginActionResponse.parse();
            FULLNAME = document.getElementsByClass("username").first().html();


            if (FULLNAME != null) {

                SharedPref sharedPref = new SharedPref(context);
                sharedPref.setPortalFullName(FULLNAME);
                sharedPref.setPortalPassword(password);
                sharedPref.setRegNumber(username);

            }


        } catch (final Exception e) {
            e.printStackTrace();

            if (e.getMessage().contains("timeout")) {


                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogTimedOut("Timed out", "Make sure you have a strong network connection and try again");

                    }
                });


            } else if (e.getMessage().contains("Connection timed out")) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogTimedOut("Something went wrong.", "University's servers took so long to respond. Please try again later.");

                    }
                });

            } else {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogTimedOut("Authentication Failed", "Are you sure that you provided correct details?");

                    }
                });

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress_dialog.dismiss();

        if (FULLNAME != null) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    context.startActivity(new Intent(context, photoselection.class));

                }
            });


        }


    }


    void showDialogTimedOut(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new GetFullNameFirst(context, username, password, view).execute();

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


}
