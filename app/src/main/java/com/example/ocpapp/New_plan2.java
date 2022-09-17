package com.example.ocpapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.module.Equipement;
import com.example.ocpapp.module.Etat;
import com.example.ocpapp.module.Fonction;
import com.example.ocpapp.module.Intervention;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.Point;
import com.example.ocpapp.module.Secteur;
import com.example.ocpapp.module.dispositif;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class New_plan2 extends AppCompatActivity{
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;
    private String[] mylist;
    private Intervention in=new Intervention();
    private int id_user;
    private int id_fonction;
    private String name_user;
    private int id_equip;

    EditText libelle_intervention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan2);
        Bundle b=getIntent().getExtras();
        id_user=b.getInt("id_user");
        name_user=b.getString("name_user");
        id_equip=b.getInt("id_equip");
        id_fonction=b.getInt("id_fonction");

        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        /***looking for Data*/
        libelle_intervention=findViewById(R.id.libelle_intervention);

        Button btAjouter=findViewById(R.id.btAjouter);
        Button btTerminer=findViewById(R.id.btTerminer);

        btAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in.setLibelle(libelle_intervention.getText().toString());

                if(in.getLibelle().equals("")){
                    Toast.makeText(getApplicationContext(),"Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    InsertIntervention(in);
                    showDialog();
                }
            }
        });
        btTerminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInformations();
            }
        });

    }




    /*********Insert intervention********/
    private void InsertIntervention(final Intervention i){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Insert_interventions,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                parameters.put("libelle",String.valueOf(in.getLibelle()));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }


    /********send information**********/
    public void goBack() {
        Intent myintent=new Intent(this,admin_panel.class);

        startActivity(myintent);
    }

    public void sendInformations() {
        Intent myintent=new Intent(this,New_plan1.class);

        //send Information
        Bundle b=new Bundle();
        b.putInt("id_user",id_user);
        b.putInt("id_fonction",id_fonction);
        b.putString("name_user",name_user);
        b.putInt("id_equip",id_equip);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
        finish();
    }

    /**********show Alert************/
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_bien3, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);
        // cancelButton=view.findViewById(R.id.cancelButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libelle_intervention.setText("");
                alertDialog.hide();

            }
        });

        /*cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });*/
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


}
