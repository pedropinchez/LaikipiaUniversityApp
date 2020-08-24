package com.example.laikipiauniversityapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context ctx;
    private SharedPreferences default_prefence;

    public SharedPref(Context context) {
        this.ctx = context;
        default_prefence = context.getSharedPreferences("kibabii", Context.MODE_PRIVATE);
    }

    public void setRegNumber(String regno) {
        default_prefence.edit().putString("regno", regno).apply();
    }

    public String getRegNumber() {
        return default_prefence.getString("regno", null);
    }


    public void setPortalPassword(String regno) {
        default_prefence.edit().putString("password", regno).apply();
    }

    public void clearRegno(){
        default_prefence.edit().remove("regno").apply();

    }

    public void clearPassword(){

        default_prefence.edit().remove("password").apply();

    }
    public String getPortalPassword() {
        return default_prefence.getString("password", null);
    }


    public void setPortalFullName(String fullname) {
        default_prefence.edit().putString("fullName", fullname).apply();
    }

    public String getPortalFullName() {
        return default_prefence.getString("fullName", null);
    }


    public void setRatings(int ratings) {
        default_prefence.edit().putInt("ratings", ratings).apply();
    }

    public int getRatings() {
        return default_prefence.getInt("ratings", 0);
    }


    public void setFirstTimePortal(String firstTimePortal) {
        default_prefence.edit().putString("FirstTime", firstTimePortal).apply();
    }

    public String getFirstTimePortal() {
        return default_prefence.getString("FirstTime", null);
    }


    public void setNotificationToken(String token) {
        default_prefence.edit().putString("token", token).apply();
    }

    public String getNotificationToken() {
        return default_prefence.getString("token", null);
    }


    public void setHighScores_Game1(int highScores_game1) {
        default_prefence.edit().putInt("highScoresGame1", highScores_game1).apply();
    }

    public int getHighScores_Game1() {
        return default_prefence.getInt("highScoresGame1", 0);
    }


    public void setHighScores_FlappyBird(int highScores_game1) {
        default_prefence.edit().putInt("highScoresFlappyBird", highScores_game1).apply();
    }

    public int getHighScores_FlappyBird() {
        return default_prefence.getInt("highScoresFlappyBird", 0);
    }


    public void setGuestUsername(String username) {
        default_prefence.edit().putString("guestUsername", username).apply();
    }

    public String getGuestUsername() {
        return default_prefence.getString("guestUsername", null);
    }

    public void setReferralCode(String code) {
        default_prefence.edit().putString("ReferralCode", code).apply();
    }

    public String getReferralCode() {
        return default_prefence.getString("ReferralCode", null);
    }

    public void setIsGuest(boolean isGuest){

        default_prefence.edit().putBoolean("isGuest", isGuest).apply();
    }

    public boolean getIsGuest(){
        return default_prefence.getBoolean("isGuest", true);

    }


}
