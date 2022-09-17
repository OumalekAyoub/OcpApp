package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        CardView mycard2 = findViewById(R.id.chef_secteur);
        mycard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), list_occurence_cons.class);

                Bundle b2 = new Bundle();
                //start Activity2
                startActivity(myintent);
            }
        });

        CardView mycard3 = findViewById(R.id.espace_admine);
        mycard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), login_admin.class);

                Bundle b3 = new Bundle();
                //start Activity2
                startActivity(myintent);
            }
        });

        CardView mycard4 = findViewById(R.id.personnel);
        mycard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), MainActivity.class);

                Bundle b3 = new Bundle();
                //start Activity2
                startActivity(myintent);
            }
        });
    }
}
