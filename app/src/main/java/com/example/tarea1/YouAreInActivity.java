package com.example.tarea1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YouAreInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_are_in);
        Button btGoOutside=findViewById(R.id.btOutside);
        btGoOutside.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retornar();
                    }
                }
        );
    }

    private void retornar(){
        SharedPreferences sharedPref = this.getSharedPreferences("com.tarea1.preference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("AUTENTICADO",0);
        editor.apply();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
