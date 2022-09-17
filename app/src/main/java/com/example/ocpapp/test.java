package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ocpapp.module.Secteur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /***looking for Spinner*/
        final Spinner spinner=findViewById(R.id.spinner);
        final TextView txv=findViewById(R.id.textview);
        final EditText edtx=findViewById(R.id.editetext);

        /*final List<String> spinnerArray=new ArrayList<String>();
        final List<String> spinnerArray2=new ArrayList<String>();
        spinnerArray.add("software");
        spinnerArray2.add("99");*/

        /*****************************/
        final List<Secteur> SecteursList=new ArrayList<Secteur>();
        Secteur s=new Secteur();
        s.setId_secteur(404);
        s.setSecteur("Not Found");
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
        /**************************/

        ArrayAdapter<Secteur > adapter=new ArrayAdapter<Secteur>(this,android.R.layout.simple_spinner_item,SecteursList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txv.setText(String.valueOf(SecteursList.get(position).getId_secteur()));
                if(SecteursList.get(position).getId_secteur()==17){
                    //edtx.setText("hhhhh");
                    //edtx.setKeyListener(null);
                    edtx.setEnabled(false);
                }else{
                    edtx.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });
    }
}
