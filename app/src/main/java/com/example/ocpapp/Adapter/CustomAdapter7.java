package com.example.ocpapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ocpapp.Constants;
import com.example.ocpapp.R;
import com.example.ocpapp.RequestHandler;
import com.example.ocpapp.module.Plan;
import com.example.ocpapp.module.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter7 extends RecyclerView.Adapter<CustomAdapter7.ViewHolder> {
    private static Context context;
    private static List<Plan> my_data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public CustomAdapter7(Context context, List<Plan> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView num_ordre,nom_equip,user_etablir,date_etablissement,nom_intervention,execution;
        public TextView user_approve,date_approvation;
        ImageView imageView;
        public Button supprimer;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            num_ordre=itemView.findViewById(R.id.num_ordre);
            nom_equip=itemView.findViewById(R.id.nom_equip);
            date_etablissement=itemView.findViewById(R.id.date_etablissement);
            nom_intervention=itemView.findViewById(R.id.nom_intervention);
            supprimer=itemView.findViewById(R.id.mybutton);
            execution=itemView.findViewById(R.id.execution);
            imageView=itemView.findViewById(R.id.imexecution);
            user_approve=itemView.findViewById(R.id.user_approve);
            date_approvation=itemView.findViewById(R.id.date_approvation);

           supprimer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position = getAdapterPosition();
                   if (position != RecyclerView.NO_POSITION) {
                       supprimer(my_data.get(position).getId_plan());
                       my_data.remove(position);
                       notifyDataSetChanged();
                   }
               }
           });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_mes_plans,parent,false);

        return new ViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num_ordre.setText(my_data.get(position).getNum_ordre());
        holder.nom_equip.setText(my_data.get(position).getNom_equip());
        holder.date_etablissement.setText(my_data.get(position).getDate_etablissement());


        if(my_data.get(position).isApprouve()){
            holder.user_approve.setText(my_data.get(position).getNom_personnel_approuve());
            holder.date_approvation.setText(my_data.get(position).getDate_approuvation());

            holder.imageView.setBackgroundResource(R.drawable.ic_action_check);
            holder.execution.setText("Approuvé");
            holder.execution.setTextColor(context.getResources().getColorStateList(R.color.colorGreen));
        }else {
            holder.imageView.setBackgroundResource(R.mipmap.ic_launcher_time);
            holder.execution.setText("En attendant");
            holder.execution.setTextColor(context.getResources().getColorStateList(R.color.colorRed));
        }

        for(int i=0; i<my_data.get(position).getInterventions().size(); i++){
            if(i==my_data.get(position).getInterventions().size()-1){
                holder.nom_intervention.append(my_data.get(position).getInterventions().get(i)+".");
            }else{
                holder.nom_intervention.append(my_data.get(position).getInterventions().get(i)+",  ");
            }

        }

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    /*****delete***************/
    public static void supprimer(final int idd) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.Delete_plan,
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

                parameters.put("id_plan", String.valueOf(idd));
                return parameters;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

    }
}
