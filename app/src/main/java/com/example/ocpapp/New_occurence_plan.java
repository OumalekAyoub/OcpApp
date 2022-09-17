package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
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
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.Adapter.CustomAdapter3;
import com.example.ocpapp.module.Intervention;
import com.example.ocpapp.module.occurence_plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class New_occurence_plan extends AppCompatActivity implements MultipleChoiceDialogFragment.onMultiChoiceListener {
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter3 adapter;
    private List<occurence_plan> data_list;

    private int id_user;
    private int id_fonction;
    private int id_plan;
    private String name_plan;
    private int id_occurence_plan;
    private String[] mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_occurence_plan);
        Bundle b = getIntent().getExtras();
        id_user = b.getInt("id_user");
        id_plan = b.getInt("id_plan");
        name_plan=b.getString("name_plan");
        id_fonction=b.getInt("id_fonction");

        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        /*******looking for data*********/
        TextView num_ordre=findViewById(R.id.num_ordre);
        num_ordre.setText(name_plan);
        final Button spinner_intervention=findViewById(R.id.spinner_intervention);
        Button next=findViewById(R.id.btNext);
        /***********Adapter pour intervention list***********/
        final List<Intervention> InterventionsList=getInterventions();
        spinner_intervention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment multiChoiceDialog = new MultipleChoiceDialogFragment(InterventionsList);
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "Multichoice Dialog");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mylist.length==0){
                    Toast.makeText(getApplicationContext(), "Erreur : veuillez choisir au minimum une intervention", Toast.LENGTH_SHORT).show();
                }else{
                    Insert_occurence_plan(new New_plan1.VolleyCallBack() {
                        @Override
                        public void onSuccess() {
                            for(String intrv:mylist){
                                est_objet_de(intrv);
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            send_information_me();
                        }
                    });
                }
            }
        });
    }

    /**************get Intervention**********/
    public List<Intervention> getInterventions(){

        final List<Intervention> InterventionsList=new ArrayList<Intervention>();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Interventions_plan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray plans = jsonObject.getJSONArray("interventions");
                            for (int i = 0; i < plans.length(); i++) {
                                JSONObject interventionJSON = plans.getJSONObject(i);

                                Intervention it=new Intervention();
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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters  = new HashMap<String, String>();
                parameters.put("id_plan",String.valueOf(id_plan));
                return parameters;
            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return InterventionsList;
    }

    /***send infomation****/
    public void send_information_me(){
        Intent myintent=new Intent(getApplicationContext(),list_occurence_cons.class);

        Bundle b2=new Bundle();
        b2.putInt("id_user",id_user);
        b2.putInt("id_fonction",id_fonction);
        b2.putInt("id_plan",id_plan);
        b2.putString("name_plan",name_plan);

        //add bundle
        myintent.putExtras(b2);
        finish();

        //start Activity2
        startActivity(myintent);
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

    /********new occurenc plan***********/
    private void Insert_occurence_plan(final New_plan1.VolleyCallBack callBack){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Insert_occurence_plan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            id_occurence_plan=Integer.parseInt( jsonObject.getString("id_occurence_plan"));
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
                parameters.put("id_plan",String.valueOf(id_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    /*********add est_objet_de********/
    public void est_objet_de(final String intervention){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.add_est_objet_de,
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

                parameters.put("id_occurence_plan",String.valueOf(id_occurence_plan));
                parameters.put("libelle_intervention",intervention);
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
