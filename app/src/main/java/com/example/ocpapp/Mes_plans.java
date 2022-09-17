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

public class Mes_plans extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private int id_user;
    private String name_user;
    private int id_fonction;
    private int id_equip;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_plans);

        Bundle b=getIntent().getExtras();
        id_user=b.getInt("id_user");
        name_user=b.getString("name_user");
        id_fonction=b.getInt("id_fonction");

        /*****foor design*************/
        rlayout     = findViewById(R.id.drawer_layout);
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
        navigationView.setCheckedItem(R.id.nav_plans);
        /******************************/

        TextView nom_user=findViewById(R.id.user_name);
        nom_user.setText(name_user);

        recyclerView = findViewById(R.id.recycler_view);
        data_list  = new ArrayList<>();
        data_list=getPlans();

        gridLayoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter=new CustomAdapter7(this,data_list);
        recyclerView.setAdapter(adapter);


    }

    /*************get plans*************/
    public List<Plan> getPlans(){

        final List<Plan> PlansList=new ArrayList<Plan>();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Plans_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray plans = jsonObject.getJSONArray("plans");
                            for (int i = 0; i < plans.length(); i++) {
                                JSONObject planJSON = plans.getJSONObject(i);

                                Plan p=new Plan();
                                p.setId_plan(Integer.parseInt(planJSON.getString("id_plan")));
                                p.setNum_ordre(planJSON.getString("num_ordre"));
                                p.setNom_equip(planJSON.getString("nom_equip"));
                                p.setNom_personnel_etablir(planJSON.getString("nom_personnel_etablir"));
                                p.setDate_etablissement(planJSON.getString("date_etablissement"));
                                p.setApprouve(planJSON.getBoolean("approuve"));
                                p.setDate_approuvation(planJSON.getString("date_approuvation"));

                                if(planJSON.getBoolean("approuve")){
                                    p.setNom_personnel_approuve(planJSON.getString("nom_personnel_approuver"));
                                }

                                List<String> interventions_list = new ArrayList<String>();
                                JSONArray intervention_list_JSON = planJSON.getJSONArray("interventions");
                                for (int j = 0; j < intervention_list_JSON.length(); j++) {
                                    JSONObject interventionJSON = intervention_list_JSON.getJSONObject(j);
                                    interventions_list.add(interventionJSON.getString("libelle"));
                                }
                                p.setInterventions(interventions_list);
                                PlansList.add(p);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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
                parameters.put("id_user",String.valueOf(id_user));
                return parameters;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return PlansList;
    }

    //send Plan ID
    public void sendplanID_information(int id,String name_plan) {
        Intent myintent=new Intent(this,admin_panel.class);
        //send Information
        Bundle b=new Bundle();
        b.putInt("id_user",id_user);
        b.putString("name_user",name_user);
        b.putInt("id_fonction",id_fonction);
        b.putInt("id_plan",id);
        b.putString("name_plan",name_plan);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
    }
    //send Plan ID
    public void sendInformation() {
        Intent myintent=new Intent(this,New_plan1.class);
        //send Information
        Bundle b=new Bundle();
        b.putInt("id_user",id_user);
        b.putString("name_user",name_user);
        b.putInt("id_fonction",id_fonction);
        b.putInt("id_equip",id_equip);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
        finish();
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
