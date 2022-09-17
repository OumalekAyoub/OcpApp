package com.example.ocpapp.Adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ocpapp.R;
import com.example.ocpapp.module.occurence_plan;

import java.util.List;

public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.ViewHolder> {
    private Context context;
    private List<occurence_plan> my_data;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public CustomAdapter3(Context context, List<occurence_plan> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView num_ordre,date_etablissement,nom_intervention,execution;
        public ImageView imageView;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            num_ordre=itemView.findViewById(R.id.num_ordre);
            nom_intervention=itemView.findViewById(R.id.nom_intervention);
            date_etablissement=itemView.findViewById(R.id.date_etablissement);
            execution=itemView.findViewById(R.id.execution);
            imageView=itemView.findViewById(R.id.imexecution);

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

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_occurence_plan,parent,false);

        return new ViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num_ordre.setText(String.valueOf(my_data.get(position).getId_occurence_plan()));

        if(my_data.get(position).isFinish()){
            holder.imageView.setBackgroundResource(R.drawable.ic_action_check);
            holder.execution.setText("Terminer");
            holder.execution.setTextColor(context.getResources().getColorStateList(R.color.colorGreen));
        }else {
            holder.imageView.setBackgroundResource(R.mipmap.ic_launcher_time);
            holder.execution.setText("En execution");
            holder.execution.setTextColor(context.getResources().getColorStateList(R.color.colorRed));
        }

        for(int i=0; i<my_data.get(position).getInterventions().size(); i++){
            if(i==my_data.get(position).getInterventions().size()-1){
                holder.nom_intervention.append(my_data.get(position).getInterventions().get(i)+".");
            }else{
                holder.nom_intervention.append(my_data.get(position).getInterventions().get(i)+",  ");
            }

        }
        holder.date_etablissement.setText(my_data.get(position).getDate_occurence_plan());

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }
}
