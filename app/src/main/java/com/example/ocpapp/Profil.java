package com.example.ocpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.Adapter.CustomAdapter7;
import com.example.ocpapp.module.Equipement;
import com.example.ocpapp.module.Intervention;
import com.example.ocpapp.module.Plan;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int id_user;
    private String name_user;
    private int id_equip;
    private int id_fonction;

    private DrawerLayout rlayout;
    private Animation animation;
    private Menu menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter7 adapter;
    private List<Plan> data_list;
    Button cree;
    TextView nom, fonction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Bundle b = getIntent().getExtras();
        id_user = b.getInt("id_user");
        name_user = b.getString("name_user");
        id_fonction=b.getInt("id_fonction");
        nom = findViewById(R.id.nom);
        fonction = findViewById(R.id.fonction);
        getser();

        /*****foor design*************/
        rlayout = findViewById(R.id.drawer_layout);
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
        navigationView.setCheckedItem(R.id.nav_profile);
        /******************************/

        TextView nom_user = findViewById(R.id.user_name);
        nom_user.setText(name_user);

    }

    /*************get plans*************/
    public void getser() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Profil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nom.setText(jsonObject.getString("nom"));
                            fonction.setText(jsonObject.getString("fonction_name"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                parameters.put("id_user", String.valueOf(id_user));
                return parameters;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

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
            case R.id.nav_home:
                Intent myintent1 = new Intent(this, Choose_Equipement.class);
                Bundle b = new Bundle();
                b.putString("name_user", name_user);
                b.putInt("id_user", id_user);
                b.putInt("id_fonction",id_fonction);
                myintent1.putExtras(b);
                startActivity(myintent1);
                break;

            case R.id.nav_logout:
                Intent myintent2 = new Intent(this, MainActivity.class);
                startActivity(myintent2);
                break;

            case R.id.nav_plans:
                Intent myintent3 = new Intent(this, Mes_plans.class);
                Bundle b2 = new Bundle();
                b2.putString("name_user", name_user);
                b2.putInt("id_user", id_user);
                b2.putInt("id_fonction",id_fonction);
                myintent3.putExtras(b2);
                startActivity(myintent3);
                break;

            case R.id.nav_help:
                Intent myintent4 = new Intent(this, Help.class);
                Bundle b3 = new Bundle();
                b3.putString("name_user", name_user);
                b3.putInt("id_user", id_user);
                b3.putInt("id_fonction",id_fonction);
                myintent4.putExtras(b3);
                startActivity(myintent4);
                break;

            case R.id.nav_profile:
                Intent myintent5 = new Intent(this, Profil.class);
                Bundle b4 = new Bundle();
                b4.putString("name_user", name_user);
                b4.putInt("id_user", id_user);
                b4.putInt("id_fonction",id_fonction);
                myintent5.putExtras(b4);
                startActivity(myintent5);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
