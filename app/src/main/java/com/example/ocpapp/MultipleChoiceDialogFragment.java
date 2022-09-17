package com.example.ocpapp;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.ocpapp.module.Intervention;

import java.util.ArrayList;
import java.util.List;


public class MultipleChoiceDialogFragment extends DialogFragment {
    private List<Intervention> listIntervention;

    public MultipleChoiceDialogFragment(List<Intervention> listIntervention){
        this.listIntervention=listIntervention;
    }

    public interface onMultiChoiceListener {
        void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList);

        void onNegativeButtonClicked();
    }

    onMultiChoiceListener mListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (onMultiChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + " onMultiChoiceListener must implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ArrayList<String> selectedItemList = new ArrayList<>();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = new String[listIntervention.size()];
        for(int i=0 ; i<listIntervention.size();i++){
            list[i] = listIntervention.get(i).getLibelle();

        }

        builder.setTitle("Changement ou intervention")
                .setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            selectedItemList.add(list[i]);
                        } else {
                            selectedItemList.remove(list[i]);
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onPositiveButtonClicked(list, selectedItemList);
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onNegativeButtonClicked();
                    }
                });

        return builder.create();

    }
}
