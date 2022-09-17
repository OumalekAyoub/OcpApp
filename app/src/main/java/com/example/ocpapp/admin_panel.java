package com.example.ocpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class admin_panel extends AppCompatActivity implements MultipleChoiceDialogFragment.onMultiChoiceListener, NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    GridLayout mygridlayout;
    private int id_user;
    private int id_fonction;
    private String name_user;
    private int id_plan;
    private String name_plan;
    private int id_occurence_plan;
    private String[] mylist;

    public interface VolleyCallBack {
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

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
        /******************************/

        TextView tv = (TextView) findViewById(R.id.textdash);
        TextView tv2 = findViewById(R.id.plan_name);
        Bundle b = getIntent().getExtras();
        tv.setText(b.getString("name_user"));
        name_user=b.getString("name_user");
        tv2.setText("Plan N° :" + b.getString("name_plan"));
        id_user = b.getInt("id_user");
        id_plan = b.getInt("id_plan");
        name_plan=b.getString("name_plan");
        id_fonction=b.getInt("id_fonction");

        final List<Intervention> InterventionsList = getInterventions();

        CardView mycard1 = findViewById(R.id.cree_plan);
        mycard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), Choose_date.class);

                Bundle b2 = new Bundle();
                b2.putInt("id_user", id_user);
                b2.putInt("id_fonction",id_fonction);
                b2.putInt("id_plan", id_plan);
                b2.putString("name_plan",name_plan);

                //add bundle
                myintent.putExtras(b2);

                //start Activity2
                startActivity(myintent);
            }
        });

        CardView mycard2 = findViewById(R.id.consigner);
        mycard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), list_occurence_cons.class);

                Bundle b2 = new Bundle();
                b2.putInt("id_user", id_user);
                b2.putInt("id_fonction",id_fonction);
                b2.putInt("id_plan", id_plan);
                b2.putString("name_plan",name_plan);

                //add bundle
                myintent.putExtras(b2);

                //start Activity2
                startActivity(myintent);
            }
        });

        CardView mycard3 = findViewById(R.id.verifier);
        mycard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), list_occurence_verifier.class);

                Bundle b3 = new Bundle();
                b3.putInt("id_user", id_user);
                b3.putInt("id_fonction",id_fonction);
                b3.putInt("id_plan", id_plan);
                b3.putString("name_plan",name_plan);

                //add bundle
                myintent.putExtras(b3);

                //start Activity2
                startActivity(myintent);
            }
        });

        CardView mycard4 = findViewById(R.id.deconsigner);
        mycard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(), list_occurence_deconsigner.class);

                Bundle b3 = new Bundle();
                b3.putInt("id_user", id_user);
                b3.putInt("id_fonction",id_fonction);
                b3.putInt("id_plan", id_plan);
                b3.putString("name_plan",name_plan);

                //add bundle
                myintent.putExtras(b3);

                //start Activity2
                startActivity(myintent);
            }
        });

    }

    /**************get Intervention**********/
    public List<Intervention> getInterventions() {

        final List<Intervention> InterventionsList = new ArrayList<Intervention>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Interventions_plan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray plans = jsonObject.getJSONArray("interventions");
                            for (int i = 0; i < plans.length(); i++) {
                                JSONObject interventionJSON = plans.getJSONObject(i);

                                Intervention it = new Intervention();
                                it.setLibelle(interventionJSON.getString("libelle"));
                                InterventionsList.add(it);
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_plan", String.valueOf(id_plan));
                return parameters;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return InterventionsList;
    }

    /********new occurenc plan***********/
    private void Insert_occurence_plan(final New_plan1.VolleyCallBack callBack) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Insert_occurence_plan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            id_occurence_plan = Integer.parseInt(jsonObject.getString("id_occurence_plan"));
                            callBack.onSuccess();

                        } catch (Exception e) {

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
                parameters.put("id_plan", String.valueOf(id_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*********add est_objet_de********/
    public void est_objet_de(final String intervention) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.add_est_objet_de,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                parameters.put("id_occurence_plan", String.valueOf(id_occurence_plan));
                parameters.put("libelle_intervention", intervention);
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*********pour Alert Multi*******/
    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Selected Choices = ");

        String[] listt = new String[selectedItemList.size()];

        for (int i = 0; i < selectedItemList.size(); i++) {
            listt[i] = selectedItemList.get(i);
        }
        mylist = listt;
        if (listt.length == 0) {
            Toast.makeText(getApplicationContext(), "Erreur : veuillez choisir au minimum une intervention", Toast.LENGTH_SHORT).show();
        } else {
            Insert_occurence_plan(new New_plan1.VolleyCallBack() {
                @Override
                public void onSuccess() {
                    for (String intrv : mylist) {
                        est_objet_de(intrv);
                    }
                }
            });
        }

    }

    @Override
    public void onNegativeButtonClicked() {
        //tvSelectedChoices.setText("Dialog Cancel");
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
