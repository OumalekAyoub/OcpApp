package com.example.ocpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.ocpapp.module.DatePickerFragment;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.occurence_plan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Choose_date extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RelativeLayout rlayout;
    private Animation animation;
    private Menu menu;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter3 adapter;
    private List<occurence_plan> data_list;

    private int id_user;
    private int id_plan;
    private String name_plan;
    private int id_occurence_plan;
    public static String Date_debut;
    public static String Date_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        Bundle b=getIntent().getExtras();
        id_user=b.getInt("id_user");
        id_plan=b.getInt("id_plan");
        name_plan=b.getString("name_plan");

        TextView txvv=findViewById(R.id.tvSignUp);
        txvv.setText("Plan N° :"+name_plan);

        /*****foor design*************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        rlayout.setAnimation(animation);
        /****************************/
        Button btn=findViewById(R.id.btNext);
        Button date_debut=findViewById(R.id.date_debut);
        Button date_fin=findViewById(R.id.date_fin);


        date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date pickerr");
            }
        });

        date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date pickerr");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Date_debut==null || Date_fin==null){
                    Toast.makeText(getApplicationContext(),"s'il vous plait choisir une date de début et une de fin",Toast.LENGTH_SHORT).show();
                }else{
                    send_information();
                }
            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance().format(c.getTime());

        int y=c.get(Calendar.YEAR);
        int m=c.get(Calendar.MONTH);
        int d=c.get(Calendar.DAY_OF_MONTH);

        if(Date_debut==null){
            Date_debut=y+"-"+(m+1)+"-"+d+" 00:00:00";
        }else{
            Date_fin=y+"-"+(m+1)+"-"+d+" 23:59:59";
        }


    }



    /***send infomation****/
    public void send_information() {
        Intent myintent = new Intent(getApplicationContext(), Historique_plan.class);

        Bundle b2 = new Bundle();
        b2.putInt("id_user", id_user);
        b2.putInt("id_plan", id_plan);
        b2.putString("name_plan",name_plan);
        b2.putString("date_debut",Date_debut);
        b2.putString("date_fin",Date_fin);

        //add bundle
        myintent.putExtras(b2);

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
}
