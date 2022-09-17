package com.example.ocpapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ocpapp.R;
import com.example.ocpapp.module.Plan;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;
    private List<Plan> my_data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public CustomAdapter(Context context, List<Plan> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView num_ordre,nom_equip,user_etablir,date_etablissement,nom_intervention;
        public TextView user_approve,date_approvation;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            num_ordre=itemView.findViewById(R.id.num_ordre);
            nom_equip=itemView.findViewById(R.id.nom_equip);
            user_etablir=itemView.findViewById(R.id.user_etablir);
            date_etablissement=itemView.findViewById(R.id.date_etablissement);
            nom_intervention=itemView.findViewById(R.id.nom_intervention);
            user_approve=itemView.findViewById(R.id.user_approve);
            date_approvation=itemView.findViewById(R.id.date_approvation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);

        return new ViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num_ordre.setText(my_data.get(position).getNum_ordre());
        holder.nom_equip.setText(my_data.get(position).getNom_equip());
        holder.user_etablir.setText(my_data.get(position).getNom_personnel_etablir());
        holder.date_etablissement.setText(my_data.get(position).getDate_etablissement());
        holder.user_approve.setText(my_data.get(position).getNom_personnel_approuve());
        holder.date_approvation.setText(my_data.get(position).getDate_approuvation());

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
}
