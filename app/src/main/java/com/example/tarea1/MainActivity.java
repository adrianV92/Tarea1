package com.example.tarea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FingerPrintUiHelper.Callback {

    private FingerPrintUiHelper fingerPrintUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
        SecurityManager securityManager= SecurityManager.initSecurityManager(fingerprintManager);



        SharedPreferences sharedPref = this.getSharedPreferences("com.tarea1.preference",Context.MODE_PRIVATE);
        ImageView ivFinger = findViewById(R.id.ivFingerPrint);
        TextView tvError = findViewById(R.id.tvError);
        fingerPrintUiHelper = new FingerPrintUiHelper(ivFinger, tvError, this);
        int isIn=sharedPref.getInt("AUTENTICADO",0);

        if (isIn==1){
            goInside();
        }else {
            boolean isEnabletoUse = securityManager.prepareSecurityFinger();
            if (!isEnabletoUse) {

            } else {
                fingerPrintUiHelper.startListening(null);
            }
        }
    }

    public void goInside(){
        Intent intent= new Intent(this, YouAreInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        fingerPrintUiHelper.onResumeListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        fingerPrintUiHelper.stopListening();
    }

    @Override
    public void onAuthenticated() {
        SharedPreferences sharedPref = this.getSharedPreferences("com.tarea1.preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("AUTENTICADO", 1);
        editor.apply();
        goInside();
    }

    @Override
    public void onError() {
        fingerPrintUiHelper.stopListening();
    }
}
