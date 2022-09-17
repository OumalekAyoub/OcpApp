package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.ocpapp.Adapter.CustomAdapter;
import com.example.ocpapp.Adapter.CustomAdapter3;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.occurence_plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class list_occurence_deconsigner extends AppCompatActivity {
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    public CustomAdapter3 adapter;
    private List<occurence_plan> data_list;

    private int id_user;
    private int id_fonction;
    private int id_plan;
    private String name_plan;
    private int id_occurence_plan;

    public interface VolleyCallBack {
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_occurence_verifier);
        Bundle b = getIntent().getExtras();
        id_user = b.getInt("id_user");
        id_plan = b.getInt("id_plan");
        name_plan = b.getString("name_plan");
        id_fonction=b.getInt("id_fonction");

        TextView txvv=findViewById(R.id.tvSignUp);
        txvv.setText("Plan N° :"+name_plan);
        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        recyclerView = findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        data_list = getOccurence_plan();

        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter3(this, data_list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomAdapter3.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // sendplanID(data_list.get(position).getId_plan());
                sendOccurence_plan_information(data_list.get(position).getId_occurence_plan());

            }
        });

    }

    /*************get occurence_plan*************/
    public List<occurence_plan> getOccurence_plan() {

        final List<occurence_plan> Occurence_planList = new ArrayList<occurence_plan>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.occurenc_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray plans = jsonObject.getJSONArray("occurence_plan");
                            for (int i = 0; i < plans.length(); i++) {
                                JSONObject planJSON = plans.getJSONObject(i);

                                occurence_plan op = new occurence_plan();
                                op.setId_occurence_plan(Integer.parseInt(planJSON.getString("id_occurence_plan")));
                                op.setDate_occurence_plan(planJSON.getString("date"));
                                op.setId_plan(Integer.parseInt(planJSON.getString("id_plan")));
                                op.setFinish(planJSON.getBoolean("finish"));

                                List<String> interventions_list = new ArrayList<String>();

                                JSONArray intervention_list_JSON = planJSON.getJSONArray("interventions");
                                for (int j = 0; j < intervention_list_JSON.length(); j++) {
                                    JSONObject interventionJSON = intervention_list_JSON.getJSONObject(j);
                                    interventions_list.add(interventionJSON.getString("libelle"));
                                }
                                op.setInterventions(interventions_list);
                                Occurence_planList.add(op);
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_plan", String.valueOf(id_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return Occurence_planList;
    }


    //send Plan ID
    public void sendOccurence_plan_information(int id) {
        Intent myintent = new Intent(this, Point_deconsigner.class);
        //send Information
        Bundle b = new Bundle();
        b.putInt("id_user", id_user);
        b.putInt("id_fonction",id_fonction);
        b.putInt("id_plan", id_plan);
        b.putInt("id_occurence_plan", id);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
    }

    /***send infomation****/
    public void send_information_me() {
        Intent myintent = new Intent(getApplicationContext(), New_occurence_plan.class);

        Bundle b2 = new Bundle();
        b2.putInt("id_user", id_user);
        b2.putInt("id_fonction",id_fonction);
        b2.putInt("id_plan", id_plan);
        b2.putString("name_plan", name_plan);
        finish();
        //add bundle
        myintent.putExtras(b2);

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
}
