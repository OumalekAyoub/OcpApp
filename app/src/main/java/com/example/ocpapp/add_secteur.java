package com.example.ocpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.module.Equipement;
import com.example.ocpapp.module.Secteur;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_secteur extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout rlayout;
    private Animation animation;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private Menu menu;
    private int id_user;
    private int id_fonction;
    private String name_user;
    private int id_equip;
    EditText spinner_secteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_secteur);

        Bundle b = getIntent().getExtras();
        id_user = b.getInt("id_user");
        name_user = b.getString("name_user");

        /*****foor design*************/
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        /*********for menu*********/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        /**************************/

        /*****navigation drawer menu**********/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_add_secteur);
        /******************************/

        /***looking for Data*/
        TextView nom_user = findViewById(R.id.user_name);
        nom_user.setText(name_user);
        spinner_secteur = findViewById(R.id.spinner_secteur);
        final Button next = findViewById(R.id.btNext);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_secteur.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    add_secteur(spinner_secteur.getText().toString());
                    showDialog();
                }

            }
        });

    }


    /******add secteur*****/
    private void add_secteur(final String libelle_secteur) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.insert_secteur,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("secteur", libelle_secteur);
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /**********show Alert************/
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_bien_ajoute, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_secteur.setText("");
                alertDialog.hide();

            }
        });

    }

    /********Animation*******************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /********menu************/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_elaborer:
                Intent myintent1 = new Intent(this, Choose_equip_admin.class);
                Bundle b = new Bundle();
                b.putString("name_user", name_user);
                b.putInt("id_user", id_user);
                myintent1.putExtras(b);
                startActivity(myintent1);
                break;

            case R.id.nav_logout:
                Intent myintent2 = new Intent(this, login_admin.class);
                startActivity(myintent2);
                break;

            case R.id.nav_add_secteur:
                Intent myintent6 = new Intent(this, add_secteur.class);
                Bundle b6 = new Bundle();
                b6.putString("name_user", name_user);
                b6.putInt("id_user", id_user);
                startActivity(myintent6);
                break;

            case R.id.nav_add_equip:
                Intent myintent7 = new Intent(this, add_equip.class);
                Bundle b7 = new Bundle();
                b7.putString("name_user", name_user);
                b7.putInt("id_user", id_user);
                myintent7.putExtras(b7);
                startActivity(myintent7);
                break;


            case R.id.nav_plans:
                Intent myintent3 = new Intent(this, Mes_plans.class);
                Bundle b2 = new Bundle();
                b2.putString("name_user", name_user);
                b2.putInt("id_user", id_user);
                b2.putInt("id_fonction", id_fonction);
                myintent3.putExtras(b2);
                startActivity(myintent3);
                break;

            case R.id.nav_help:
                Intent myintent4 = new Intent(this, Help.class);
                Bundle b3 = new Bundle();
                b3.putString("name_user", name_user);
                b3.putInt("id_user", id_user);
                b3.putInt("id_fonction", id_fonction);
                myintent4.putExtras(b3);
                startActivity(myintent4);
                break;

            case R.id.nav_profile:
                Intent myintent5 = new Intent(this, Profil.class);
                Bundle b4 = new Bundle();
                b4.putString("name_user", name_user);
                b4.putInt("id_user", id_user);
                b4.putInt("id_fonction", id_fonction);
                myintent5.putExtras(b4);
                startActivity(myintent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
