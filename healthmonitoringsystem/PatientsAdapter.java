package com.harshdi.healthmonitoringsystem;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {

    private final List<Patient> patientsList;
    private final OnPatientActionListener listener; // * FIX: Re-added the listener

    // * FIX: This interface is the communication channel to the Activity *
    public interface OnPatientActionListener {
        void onPatientClick(Patient patient);
        void onEditClick(Patient patient);
        void onDeleteClick(Patient patient);
    }

    // * FIX: The constructor now requires the listener again *
    public PatientsAdapter(List<Patient> patientsList, OnPatientActionListener listener) {
        this.patientsList = patientsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientsList.get(position);
        // Pass the listener to the ViewHolder
        holder.bind(patient, listener);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPatientName, tvPatientEmail;
        private final LinearLayout patientInfoLayout;
        private final ImageView ivEditPatient, ivDeletePatient; // * FIX: Added action icons

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvPatientEmail = itemView.findViewById(R.id.tvPatientEmail);
            patientInfoLayout = itemView.findViewById(R.id.patientInfoLayout);
            // * FIX: Initialize the action icons from the XML layout *
            ivEditPatient = itemView.findViewById(R.id.ivEditPatient);
            ivDeletePatient = itemView.findViewById(R.id.ivDeletePatient);
        }

        // * FIX: The bind method now accepts the listener *
        public void bind(final Patient patient, final OnPatientActionListener listener) {
            tvPatientName.setText(patient.getName());
            tvPatientEmail.setText(patient.getEmail());

            if (listener != null) {
                // When the doctor clicks on the main patient info area...
                patientInfoLayout.setOnClickListener(v -> listener.onPatientClick(patient));

                // When the doctor clicks the edit icon...
                ivEditPatient.setOnClickListener(v -> listener.onEditClick(patient));

                // When the doctor clicks the delete icon...
                ivDeletePatient.setOnClickListener(v -> listener.onDeleteClick(patient));
            }
        }
    }
}