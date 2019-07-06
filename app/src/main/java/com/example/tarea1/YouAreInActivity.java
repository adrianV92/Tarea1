package com.example.tarea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class YouAreInActivity extends AppCompatActivity {

    FloatingActionButton btSave;
    FloatingActionButton btClose;
    TextView tvSecretMessage;

    final String secretMessKey="SECRET_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_in);
        tvSecretMessage=findViewById(R.id.tvSecretMessage);
        btClose=findViewById(R.id.btClose);
        btSave=findViewById(R.id.btSave);
        btClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                }
        );
        btSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveMessage();
                    }
                }
        );
        tvSecretMessage.setText(getSecretMessage());
    }

    private String getSecretMessage(){
        SharedPreferences sharedPref = this.getSharedPreferences("com.tarea1.preference",Context.MODE_PRIVATE);
        String mensaje= sharedPref.getString(secretMessKey,"");
        return mensaje;
    }

    private void logout(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveMessage(){
        SharedPreferences sharedPref = this.getSharedPreferences("com.tarea1.preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String mensaje=tvSecretMessage.getText().toString();
        editor.putString(secretMessKey, mensaje);
        editor.apply();
        logout();
    }
}
