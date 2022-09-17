package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.Adapter.CustomAdapter;
import com.example.ocpapp.Adapter.CustomAdapter2;
import com.example.ocpapp.Adapter.CustomAdapter4;
import com.example.ocpapp.module.Equipement;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Point_verification extends AppCompatActivity {
    private int id_user;
    private int id_fonction;
    private int id_plan;
    private int id_occurence_plan;

    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter4 adapter;
    private List<Point> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_verification);
        Bundle b=getIntent().getExtras();

        id_plan=b.getInt("id_plan");
        id_user=b.getInt("id_user");
        id_fonction=b.getInt("id_fonction");
        id_occurence_plan=b.getInt("id_occurence_plan");


        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/

        recyclerView = findViewById(R.id.recycler_view);
        data_list  = new ArrayList<>();
        data_list=getPoints(id_plan);

        gridLayoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter=new CustomAdapter4(this,data_list,id_occurence_plan);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CustomAdapter4.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });


    }

    /*************get points*************/
    public List<Point> getPoints(final int id_plan){
        final String id=String.valueOf(id_plan);

        final List<Point> PointsList=new ArrayList<Point>();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.Points_verification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray points = jsonObject.getJSONArray("points");
                            for (int i = 0; i < points.length(); i++) {
                                JSONObject pointJSON = points.getJSONObject(i);

                                Point p=new Point();
                                p.setId_plan(id_plan);
                                p.setId_user_consigne(id_user);
                                p.setId_point(Integer.parseInt(pointJSON.getString("id_point")));
                                p.setRepere(pointJSON.getString("repere"));
                                p.setLocalisation(pointJSON.getString("localisation"));
                                p.setEtat_point(pointJSON.getString("etat_point"));
                                p.setCharge_consignation(pointJSON.getString("nom_fonction"));

                                List<String> cadenas_list = new ArrayList<String>();
                                JSONArray cadenas_list_JSON = pointJSON.getJSONArray("nom_dispositif");
                                for (int j = 0; j < cadenas_list_JSON.length(); j++) {
                                    JSONObject cadenasJSON = cadenas_list_JSON.getJSONObject(j);
                                    cadenas_list.add(cadenasJSON.getString("libelle"));
                                }
                                p.setCadenas(cadenas_list);

                                /***consigner*******/
                                p.setConsigne(pointJSON.getBoolean("is_consigne"));
                                if(pointJSON.getBoolean("is_consigne")){
                                    p.setName_user_consigne(pointJSON.getString("id_personnel_consigner"));
                                    p.setDate_cons(pointJSON.getString("date_consignation"));
                                }
                                /***Verifier*******/
                                p.setVerifier(pointJSON.getBoolean("is_verifier"));
                                if(pointJSON.getBoolean("is_verifier")){
                                    p.setId_user_verifier(Integer.parseInt(pointJSON.getString("id_personnel_verifier2")));
                                    p.setName_user_verifier(pointJSON.getString("id_personnel_verifier"));
                                    p.setDate_verification(pointJSON.getString("date_verification"));
                                }
                                /***deconsigner*******/
                                p.setDeconsigner(pointJSON.getBoolean("is_decons"));
                                if(pointJSON.getBoolean("is_decons")){
                                    p.setName_user_deconsigne(pointJSON.getString("id_personnel_deconsigner"));
                                    p.setDate_deconsigne(pointJSON.getString("date_deconsignation"));
                                }
                                PointsList.add(p);
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
                parameters.put("id_plan",id);
                parameters.put("id_occurence_plan",String.valueOf(id_occurence_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

        return PointsList;
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
