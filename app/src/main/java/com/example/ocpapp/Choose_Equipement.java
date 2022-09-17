package com.example.ocpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
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

public class Choose_Equipement extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__equipement);

        Bundle b=getIntent().getExtras();
        id_user=b.getInt("id_user");
        name_user=b.getString("name_user");
        id_fonction=b.getInt("id_fonction");

        /*****foor design*************/
        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
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
        navigationView.setCheckedItem(R.id.nav_home);
        /******************************/

        /***looking for Data*/
        TextView nom_user=findViewById(R.id.user_name);
        nom_user.setText(name_user);
        final Spinner spinner_secteur=findViewById(R.id.spinner_secteur);
        final Spinner spinner_equipement=findViewById(R.id.spinner_Equipement);
        final Button next=findViewById(R.id.btNext);

        /***********Adapter pour secteur list***********/
        final List<Secteur> SecteursList=getSecteurs();

        ArrayAdapter<Secteur > adapter=new ArrayAdapter<Secteur>(this,android.R.layout.simple_spinner_item,SecteursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_secteur.setAdapter(adapter);

        spinner_secteur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(SecteursList.get(position).getId_secteur()==0){
                    spinner_equipement.setEnabled(false);
                }else{
                    spinner_equipement.setEnabled(true);
                }

                List<Equipement> EquipementList=getEquipements(SecteursList.get(position).getId_secteur());
                Edit_spinner_equip(EquipementList,spinner_equipement);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_secteur.setSelection(0);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="data "+name_user+"  "+id_user+"  "+id_equip;
                if(id_equip==0 ) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir un equipement", Toast.LENGTH_SHORT).show();
                }else{
                    sendInformations();
                }
            }
        });

    }

    public void Edit_spinner_equip(final List<Equipement> EquipementList, final Spinner spinner_equipement){
        ArrayAdapter<Equipement > adapter2=new ArrayAdapter<Equipement>(this,android.R.layout.simple_spinner_item,EquipementList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_equipement.setAdapter(adapter2);

        spinner_equipement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_equip=EquipementList.get(position).getId_equipement();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_equipement.setSelection(0);
            }
        });
    }

    /*************get secteurs*************/
    public List<Secteur> getSecteurs(){

        final List<Secteur> SecteursList=new ArrayList<Secteur>();
        Secteur s=new Secteur();
        s.setId_secteur(0);
        s.setSecteur("Choisir un secteur");
        SecteursList.add(s);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.Secteurs,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray secteurs = response.getJSONArray("secteurs");
                    for (int i = 0; i < secteurs.length(); i++) {
                        JSONObject secteurJSON = secteurs.getJSONObject(i);

                        Secteur s=new Secteur();
                        s.setId_secteur(Integer.parseInt(secteurJSON.getString("id_secteur")));
                        s.setSecteur(secteurJSON.getString("secteur"));
                        SecteursList.add(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return SecteursList;
    }

    /******get equipement*****/
    private List<Equipement> getEquipements(int id_secteur){
        final String id=String.valueOf(id_secteur);

        final List<Equipement> EquipementsList=new ArrayList<Equipement>();
        Equipement e=new Equipement();
        e.setId_equipement(0);
        e.setNom("Choisir un equipement");
        EquipementsList.add(e);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Equipement,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray equipements = jsonObject.getJSONArray("equipements");
                            for (int i = 0; i < equipements.length(); i++) {
                                JSONObject equipementJSON = equipements.getJSONObject(i);

                                Equipement e=new Equipement();
                                e.setId_equipement(Integer.parseInt(equipementJSON.getString("id_equipement")));
                                e.setNom(equipementJSON.getString("nom"));
                                EquipementsList.add(e);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters  = new HashMap<String, String>();
                parameters.put("id_secteur",id);
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return EquipementsList;
    }

    /********send information**********/
    public void sendInformations() {
        Intent myintent=new Intent(this,Plan_list_cons.class);

        //send Information
        Bundle b=new Bundle();
        b.putInt("id_user",id_user);
        b.putString("name_user",name_user);
        b.putInt("id_equip",id_equip);
        b.putInt("id_fonction",id_fonction);
        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
    }

    /********Animation*******************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
