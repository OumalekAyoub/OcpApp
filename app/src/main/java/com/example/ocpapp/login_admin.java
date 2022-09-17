package com.example.ocpapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_admin extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextlogin, editTextPassword;
    private Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        editTextlogin = (EditText) findViewById(R.id.etUsername);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        buttonLogin = (Button) findViewById(R.id.btLogin);

        buttonLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v==buttonLogin)
            userlogin();
    }
    /**********User Login************/
    private void userlogin(){
        final String login=editTextlogin.getText().toString();
        final String password=editTextPassword.getText().toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.ADMIN_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                sendName(obj.getString("nom"),Integer.parseInt(obj.getString("id_admin")),Integer.parseInt("4"));
                                //Toast.makeText(getApplicationContext(),"user"+obj.getString("nom")+" login successful",Toast.LENGTH_LONG).show();
                            }else{
                                showDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showDialog2();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("login",login);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    /**********show Alert************/
    void showDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialoge, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPassword.setText("");
                alertDialog.hide();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //moveTaskToBack(true);
                //System.exit(1);
                //finishAffinity();
                System.exit(0);
            }
        });
    }
    void showDialog2() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialoge_connexion, null);

        Button acceptButton = view.findViewById(R.id.acceptButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPassword.setText("");
                alertDialog.hide();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //moveTaskToBack(true);
                //System.exit(1);
                //finishAffinity();
                System.exit(0);
            }
        });
    }
    //send Name
    public void sendName(String name,int id,int id_fonction) {
        Intent myintent=new Intent(this,Choose_equip_admin.class);
        //send Information
        Bundle b=new Bundle();
        b.putString("name_user",name);
        b.putInt("id_user",id);
        b.putInt("id_fonction",id_fonction);

        //add bundle
        myintent.putExtras(b);

        //start Activity2
        startActivity(myintent);
    }

    /******back*******/
    @Override
    public void onBackPressed() {
        Intent myintent=new Intent(this,Accueil.class);
        startActivity(myintent);
    }
}
