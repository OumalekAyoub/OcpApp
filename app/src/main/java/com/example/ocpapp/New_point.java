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

public class New_point extends AppCompatActivity implements MultipleChoiceDialogFragment2.onMultiChoiceListener{
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;
    private String[] mylist;
    private Point point=new Point();
    private int id_user;
    private int id_fonction;
    private String name_user;
    private int id_equip;

    EditText repere;
    EditText localisation;
    Button spinner_dispositif;
    Spinner spinner_etat;
    Spinner spinner_fonction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_point);
        Bundle b=getIntent().getExtras();
        id_user=b.getInt("id_user");
        name_user=b.getString("name_user");
        id_equip=b.getInt("id_equip");
        point.setId_plan(b.getInt("id_plan"));

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
        repere=findViewById(R.id.repere);
        localisation=findViewById(R.id.localisation);
        spinner_dispositif=findViewById(R.id.spinner_dispositif);
        spinner_etat=findViewById(R.id.spinner_etat);
        spinner_fonction=findViewById(R.id.spinner_fonction);

        Button btAjouter=findViewById(R.id.btAjouter);
        Button btTerminer=findViewById(R.id.btTerminer);

        final List<dispositif> DispositifList=getDispositif();
        spinner_dispositif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment multiChoiceDialog = new MultipleChoiceDialogFragment2(DispositifList);
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
            }
        });

        /***********Adapter pour fonction list***********/
        final List<Fonction> FonctionsList=getFonctions();

        ArrayAdapter<Fonction > adapter=new ArrayAdapter<Fonction>(this,android.R.layout.simple_spinner_item,FonctionsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fonction.setAdapter(adapter);

        spinner_fonction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                  point.setId_fonction(FonctionsList.get(position).getId_fonction());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_fonction.setSelection(0);
            }
        });

        /***********Adapter pour etat list***********/
        final List<Etat> EtatList=getEtat();

        ArrayAdapter<Etat > adapter2=new ArrayAdapter<Etat>(this,android.R.layout.simple_spinner_item,EtatList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_etat.setAdapter(adapter2);

        spinner_etat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                point.setEtat_point(EtatList.get(position).getLibelle());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner_fonction.setSelection(0);
            }
        });

        btAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point.setRepere(repere.getText().toString());
                point.setLocalisation(localisation.getText().toString());

                if(point.getId_fonction()==0 || mylist==null || point.getRepere().equals("") || point.getLocalisation().equals("") || point.getEtat_point().equals("")){
                    Toast.makeText(getApplicationContext(),"Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                }else{
                    InsertPoint(point, new New_plan1.VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            for(String intrv:mylist){
                                Insert_Condamne_par(point.getId_point(),intrv);
                            }
                            showDialog();
                        }
                    });
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

    /*************get dispositif*************/
    public List<dispositif> getDispositif(){

        final List<dispositif> DispositifsList=new ArrayList<dispositif>();
        /*Intervention i=new Intervention();
        i.setId_intervention(0);
        i.setLibelle("Choisir une intervention");
        InterventionsList.add(i);*/

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.Dispositif,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray dispositifs = response.getJSONArray("dispositifs");
                    for (int i = 0; i < dispositifs.length(); i++) {
                        JSONObject dispositifJSON = dispositifs.getJSONObject(i);

                        dispositif d=new dispositif();
                        d.setId_disposotif(Integer.parseInt(dispositifJSON.getString("id_dispositif")));
                        d.setLibelle_dispositif(dispositifJSON.getString("libelle"));
                        DispositifsList.add(d);

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
        return DispositifsList;
    }

    /*************get fonctions*************/
    public List<Fonction> getFonctions(){

        final List<Fonction> FonctionsList=new ArrayList<Fonction>();
        Fonction f=new Fonction();
        f.setId_fonction(0);
        f.setLibelle_fonction("Choisir une fonction");
        FonctionsList.add(f);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.Fonctions,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray Fonctions = response.getJSONArray("fonctions");
                    for (int i = 0; i < Fonctions.length(); i++) {
                        JSONObject FonctionJSON = Fonctions.getJSONObject(i);

                        Fonction f=new Fonction();
                        f.setId_fonction(Integer.parseInt(FonctionJSON.getString("id_fonction")));
                        f.setLibelle_fonction(FonctionJSON.getString("libelle"));
                        FonctionsList.add(f);
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
        return FonctionsList;
    }

    /*************get etat*************/
    public List<Etat> getEtat(){

        final List<Etat> EtatssList=new ArrayList<Etat>();

        Etat e=new Etat();
        e.setId_etat(0);
        e.setLibelle("Choisir l'etat");
        EtatssList.add(e);

        Etat e1=new Etat();
        e1.setId_etat(1);
        e1.setLibelle("Ouverte");
        EtatssList.add(e1);

        Etat e2=new Etat();
        e2.setId_etat(2);
        e2.setLibelle("Fermer");
        EtatssList.add(e2);

        return EtatssList;
    }

    /*********Insert point********/
    private void InsertPoint(final Point p, final New_plan1.VolleyCallBack callBack){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Insert_point,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            p.setId_point(Integer.parseInt( jsonObject.getString("id_point")));
                            callBack.onSuccess();

                        }catch (Exception e){

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

                parameters.put("id_plan",String.valueOf(p.getId_plan()));
                parameters.put("repere",p.getRepere());
                parameters.put("localisation",p.getLocalisation());
                parameters.put("etat_point",p.getEtat_point());
                parameters.put("id_fonction",String.valueOf(p.getId_fonction()));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*********Insert Condamne_par********/
    public void Insert_Condamne_par(final int id_point, final String dispositif){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Insert_insertCondamne_par,
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

                parameters.put("id_point",String.valueOf(id_point));
                parameters.put("libelle_dispositif",dispositif);
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
        Intent myintent=new Intent(this,Plan_list_cons.class);

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
        View view = inflater.inflate(R.layout.alert_bien2, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);
        // cancelButton=view.findViewById(R.id.cancelButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repere.setText("");
                localisation.setText("");
                spinner_etat.setSelection(0);
                spinner_fonction.setSelection(0);
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

    /*********pour Alert Multi*******/
    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Selected Choices = ");

        String[] listt=new String[selectedItemList.size()];

        for (int i=0 ; i<selectedItemList.size();i++) {
            listt[i]=selectedItemList.get(i);
        }
        mylist=listt;

    }

    @Override
    public void onNegativeButtonClicked() {
        //tvSelectedChoices.setText("Dialog Cancel");
    }
}
