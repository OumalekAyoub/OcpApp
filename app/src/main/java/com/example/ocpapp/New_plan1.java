package com.example.ocpapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
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
import com.example.ocpapp.module.Intervention;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.Secteur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class New_plan1 extends AppCompatActivity implements MultipleChoiceDialogFragment.onMultiChoiceListener {
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;
    EditText email, username;
    Button next;
    private int id_equip;
    private Plan p = new Plan();
    private String name_secteur;
    private String name_equipement;
    private String[] mylist;
    private String name_user;
    private int id_fonction;

    public interface VolleyCallBack {
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan1);

        Bundle b = getIntent().getExtras();
        p.setId_personnel_etablir(b.getInt("id_user"));
        name_user = b.getString("name_user");
        p.setId_equip(b.getInt("id_equip"));

        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        /***looking for Data*/
        final TextView spinner_secteur = findViewById(R.id.spinner_secteur);
        final TextView spinner_equipement = findViewById(R.id.spinner_Equipement);
        final EditText etnum_order = findViewById(R.id.num_ordre);
        final Button spinner_intervention = findViewById(R.id.spinner_intervention);
        final Button ajouter_intervention = findViewById(R.id.ajouter_intervention);

        next = findViewById(R.id.btNext);

        getSecteurs(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                spinner_secteur.setText(name_secteur);
            }
        });
        getEquipements(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                spinner_equipement.setText(name_equipement);
            }
        });


        /***********Adapter pour intervention list***********/

        final List<Intervention> InterventionsList = getInterventions();
        spinner_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment multiChoiceDialog = new MultipleChoiceDialogFragment(InterventionsList);
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
            }
        });

        ajouter_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInformations2();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setNum_ordre(etnum_order.getText().toString());
                if (p.getId_equip() == 0 || mylist == null || p.getNum_ordre().equals("")) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    InsertPlan(p, new VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            for (String intrv : mylist) {
                                Insert_plan_intervantion(p.getId_plan(), intrv);
                            }
                            showDialog();
                        }
                    });
                }
            }
        });

    }


    public void setId_equip(int id) {
        this.id_equip = id;
    }

    public void Edit_spinner_equip(final List<Equipement> EquipementList, final Spinner spinner_equipement) {
        ArrayAdapter<Equipement> adapter2 = new ArrayAdapter<Equipement>(this, android.R.layout.simple_spinner_item, EquipementList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_equipement.setAdapter(adapter2);

        spinner_equipement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setId_equip(EquipementList.get(position).getId_equipement());
                p.setId_equip(EquipementList.get(position).getId_equipement());
                // txv.setText(String.valueOf(SecteursList.get(position).getId_secteur()));
                //Toast.makeText(getApplicationContext(),String.valueOf(EquipementList.get(position).getId_equipement()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_equipement.setSelection(0);
            }
        });
    }

    /*************get secteurs*************/
    public void getSecteurs(final VolleyCallBack callBack) {

        final List<Secteur> SecteursList = new ArrayList<Secteur>();
        final Secteur s = new Secteur();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Secteur_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            name_secteur = jsonObject.getString("nom_secteur");

                            callBack.onSuccess();
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
                parameters.put("id_equip", String.valueOf(p.getId_equip()));
                return parameters;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /******get equipement*****/
    private void getEquipements(final VolleyCallBack callBack) {

        final List<Equipement> EquipementsList = new ArrayList<Equipement>();
        final Equipement e = new Equipement();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Equipement_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            name_equipement = jsonObject.getString("nom_equipement");
                            callBack.onSuccess();

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
                parameters.put("id_equip", String.valueOf(p.getId_equip()));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*************get interventions*************/
    public List<Intervention> getInterventions() {

        final List<Intervention> InterventionsList = new ArrayList<Intervention>();
        /*Intervention i=new Intervention();
        i.setId_intervention(0);
        i.setLibelle("Choisir une intervention");
        InterventionsList.add(i);*/

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.Interventions, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray interventions = response.getJSONArray("interventions");
                    for (int i = 0; i < interventions.length(); i++) {
                        JSONObject interventionJSON = interventions.getJSONObject(i);

                        Intervention it = new Intervention();
                        it.setId_intervention(Integer.parseInt(interventionJSON.getString("id_intervention")));
                        it.setLibelle(interventionJSON.getString("libelle"));
                        InterventionsList.add(it);

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
        return InterventionsList;
    }

    /********send information**********/
    public void sendInformations(int id) {
        Intent myintent = new Intent(this, New_point.class);

        //send Information
        Bundle b = new Bundle();
        b.putInt("id_user", p.getId_personnel_etablir());

        b.putString("name_user", name_user);
        b.putInt("id_equip", p.getId_equip());
        b.putInt("id_plan", id);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
        finish();
    }

    public void sendInformations2() {
        Intent myintent = new Intent(this, New_plan2.class);

        //send Information
        Bundle b = new Bundle();
        b.putInt("id_user", p.getId_personnel_etablir());
        b.putInt("id_fonction",id_fonction);
        b.putString("name_user", name_user);
        b.putInt("id_equip", p.getId_equip());

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
        finish();
    }
    /*********Insert plan********/
    private void InsertPlan(final Plan p, final VolleyCallBack callBack) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Insert_plan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            p.setId_plan(Integer.parseInt(jsonObject.getString("id_plan")));
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

                parameters.put("num_ordre", p.getNum_ordre());
                parameters.put("id_equip", String.valueOf(p.getId_equip()));
                parameters.put("id_user", String.valueOf(p.getId_personnel_etablir()));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*********Insert plan_intervention********/
    public void Insert_plan_intervantion(final int id_plan, final String intervention) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Insert_plan_intervention,
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

                parameters.put("id_plan", String.valueOf(id_plan));
                parameters.put("libelle_intervention", intervention);
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /**********show Alert************/
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_bien, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInformations(p.getId_plan());
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

    }

    @Override
    public void onNegativeButtonClicked() {
        //tvSelectedChoices.setText("Dialog Cancel");
    }
}
