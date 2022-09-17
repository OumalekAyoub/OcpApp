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

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {
    private static Context context;
    private static List<Point> my_data;
    private OnItemClickListener mListener;
    private static int id_occurence_plan;
    private static int id_fonction;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CustomAdapter2(Context context, List<Point> my_data, int id_occurence_plan,int id_fonction) {
        this.context = context;
        this.my_data = my_data;
        this.id_occurence_plan = id_occurence_plan;
        this.id_fonction=id_fonction;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView num, repere, localisation, disposition, etat_point, charge_cons;
        public TextView user_cons, date_cons, user_verifier, date_verifier, user_decons, date_decons;
        public Spinner num_cadenas;
        public Button but;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            repere = itemView.findViewById(R.id.repere);
            localisation = itemView.findViewById(R.id.localisation);
            disposition = itemView.findViewById(R.id.disposition);
            etat_point = itemView.findViewById(R.id.etat_point);
            charge_cons = itemView.findViewById(R.id.charge_cons);
            but = itemView.findViewById(R.id.mybutton);
            num_cadenas = itemView.findViewById(R.id.num_cadenas);
            user_cons = itemView.findViewById(R.id.user_cons);
            date_cons = itemView.findViewById(R.id.date_consignation);
            user_verifier = itemView.findViewById(R.id.user_verifier);
            date_verifier = itemView.findViewById(R.id.date_verification);
            user_decons = itemView.findViewById(R.id.user_decons);
            date_decons = itemView.findViewById(R.id.date_deconsignation);

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        if(my_data.get(position).getId_fonction()!=id_fonction){
                            Toast.makeText(context, "Vous n'avez pas la permission pour faire cette action", Toast.LENGTH_SHORT).show();
                        }else{
                            if (but.getText().toString().equals("Consigner")) {
                                if (num_cadenas.getSelectedItem().toString().equals("N° du cadenas")) {
                                    Toast.makeText(context, "Veuillez choisir un N° du cadenas", Toast.LENGTH_SHORT).show();

                                } else {
                                    Consigner(my_data.get(position), num_cadenas.getSelectedItem().toString());
                                    num_cadenas.setEnabled(false);
                                    but.setText("Annuler");
                                    but.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorRed));
                                }
                            } else {
                                annuler_Consigner(my_data.get(position));
                                num_cadenas.setEnabled(true);
                                but.setText("Consigner");
                                but.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorGreen));
                            }
                        }

                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_point, parent, false);

        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num.setText("N°: " + (position + 1));
        holder.repere.setText(my_data.get(position).getRepere());
        holder.localisation.setText(my_data.get(position).getLocalisation());
        holder.etat_point.setText(my_data.get(position).getEtat_point());
        holder.charge_cons.setText(my_data.get(position).getCharge_consignation());

        for (int i = 0; i < my_data.get(position).getCadenas().size(); i++) {
            if (i == my_data.get(position).getCadenas().size() - 1) {
                holder.disposition.append(my_data.get(position).getCadenas().get(i) + ".");
            } else {
                holder.disposition.append(my_data.get(position).getCadenas().get(i) + " + ");
            }

        }

        if (my_data.get(position).isConsigne()) {
            holder.user_cons.setText(my_data.get(position).getName_user_consigne());
            holder.date_cons.setText(my_data.get(position).getDate_cons());
        }
        if (my_data.get(position).isVerifier()) {
            holder.user_verifier.setText(my_data.get(position).getName_user_verifier());
            holder.date_verifier.setText(my_data.get(position).getDate_verification());
        }
        if (my_data.get(position).isDeconsigner()) {
            holder.user_decons.setText(my_data.get(position).getName_user_deconsigne());
            holder.date_decons.setText(my_data.get(position).getDate_deconsigne());
        }

        /***********Adapter pour cadenas list***********/
        final List<Cadenas> cadenasList = getCadenas();

        ArrayAdapter<Cadenas> adapter3 = new ArrayAdapter<Cadenas>(context, android.R.layout.simple_spinner_item, cadenasList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.num_cadenas.setAdapter(adapter3);


        if (my_data.get(position).isConsigne()) {
            if ((my_data.get(position).getId_user_consigne() == my_data.get(position).getId_user_cons2()) && !my_data.get(position).isVerifier()) {
                holder.but.setText("Annuler");
                ;
                holder.but.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorRed));
            } else {
                holder.num_cadenas.setEnabled(false);
                holder.but.setText("Consigné");
                holder.but.setEnabled(false);
                holder.but.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorGray));
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

    /*********Insert Consigner********/
    public static void Consigner(final Point p, final String num_cadenas) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Consigner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
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

                parameters.put("id_user_cons", String.valueOf(p.getId_user_consigne()));
                parameters.put("id_plan", String.valueOf(p.getId_plan()));
                parameters.put("id_point", String.valueOf(p.getId_point()));
                parameters.put("num_cadenas", num_cadenas);
                parameters.put("id_occurence_plan", String.valueOf(id_occurence_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }

    /*********annuler Consigner********/
    public static void annuler_Consigner(final Point p) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Annuler_Consigner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
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

                parameters.put("id_user_cons", String.valueOf(p.getId_user_consigne()));
                parameters.put("id_plan", String.valueOf(p.getId_plan()));
                parameters.put("id_point", String.valueOf(p.getId_point()));
                parameters.put("id_occurence_plan", String.valueOf(id_occurence_plan));
                return parameters;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }


}
