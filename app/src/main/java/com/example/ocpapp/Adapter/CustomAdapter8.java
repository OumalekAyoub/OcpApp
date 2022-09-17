package com.example.ocpapp.Adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.Constants;
import com.example.ocpapp.R;
import com.example.ocpapp.RequestHandler;
import com.example.ocpapp.module.Cadenas;
import com.example.ocpapp.module.Intervention;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter8 extends RecyclerView.Adapter<CustomAdapter8.ViewHolder> {
    private static Context context;
    private static List<Point> my_data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CustomAdapter8(Context context, List<Point> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView num, repere, localisation, disposition, etat_point, charge_cons;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            repere = itemView.findViewById(R.id.repere);
            localisation = itemView.findViewById(R.id.localisation);
            disposition = itemView.findViewById(R.id.disposition);
            etat_point = itemView.findViewById(R.id.etat_point);
            charge_cons = itemView.findViewById(R.id.charge_cons);



        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_point_plan, parent, false);

        return new ViewHolder(itemView, mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num.setText("N°: " + (position + 1));
        holder.repere.setText(my_data.get(position).getRepere());
        holder.localisation.setText(my_data.get(position).getLocalisation());
        holder.etat_point.setText(my_data.get(position).getEtat_point());
        holder.charge_cons.setText(my_data.get(position).getCharge_consignation());

        for(int i=0; i<my_data.get(position).getCadenas().size(); i++){
            if(i==my_data.get(position).getCadenas().size()-1){
                holder.disposition.append(my_data.get(position).getCadenas().get(i)+".");
            }else{
                holder.disposition.append(my_data.get(position).getCadenas().get(i)+" + ");
            }

        }




    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    /*************get cadenas*************/
    public static List<Cadenas> getCadenas() {

        final List<Cadenas> CadenasList = new ArrayList<Cadenas>();
        Cadenas c = new Cadenas();
        c.setId_cadenas(0);
        c.setNum_cadenas("N° du cadenas");
        CadenasList.add(c);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constants.Cadenas, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray cadenas = response.getJSONArray("cadenas");
                    for (int i = 0; i < cadenas.length(); i++) {
                        JSONObject cadenasJSON = cadenas.getJSONObject(i);

                        Cadenas c = new Cadenas();
                        c.setId_cadenas(Integer.parseInt(cadenasJSON.getString("id_cadenas")));
                        c.setNum_cadenas(cadenasJSON.getString("num_cadenas"));
                        CadenasList.add(c);

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
        RequestHandler.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return CadenasList;
    }


}
