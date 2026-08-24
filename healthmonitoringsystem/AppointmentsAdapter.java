package com.harshdi.healthmonitoringsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private final List<Appointment> appointmentsList;
    private final OnAppointmentActionListener listener;

    // This interface is the crucial communication link to the Activity.
    public interface OnAppointmentActionListener {
        void onAppointmentAction(Appointment appointment, String action);
    }

    public AppointmentsAdapter(List<Appointment> appointmentsList, OnAppointmentActionListener listener) {
        this.appointmentsList = appointmentsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.bind(appointmentsList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPatientName, tvStatus, tvPatientAge, tvPatientPhone, tvAppointmentDate, tvProblem;
        private final Button btnApprove, btnReject, btnComplete, btnDelete, btnEdit, btnSetPending;
        private final LinearLayout actionButtonsLayout;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPatientAge = itemView.findViewById(R.id.tvPatientAge);
            tvPatientPhone = itemView.findViewById(R.id.tvPatientPhone);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvProblem = itemView.findViewById(R.id.tvProblem);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnSetPending = itemView.findViewById(R.id.btnSetPending);
            actionButtonsLayout = itemView.findViewById(R.id.actionButtonsLayout);
        }

        public void bind(final Appointment appointment, final OnAppointmentActionListener listener) {
            // 1. Populate the data fields
            tvPatientName.setText(appointment.getPatientName());
            tvPatientAge.setText(appointment.getPatientAge() != null ? appointment.getPatientAge() + " years" : "N/A");
            tvPatientPhone.setText(appointment.getPatientPhone());
            tvAppointmentDate.setText(appointment.getAppointmentDate());
            tvProblem.setText(appointment.getProblem());

            String status = appointment.getStatus() != null ? appointment.getStatus() : "Pending";
            tvStatus.setText(status.toUpperCase());

            // 2. Set the status background color
            switch (status.toLowerCase()) {
                case "approved":
                    tvStatus.setBackgroundResource(R.drawable.status_approved_background);
                    break;
                case "rejected":
                    tvStatus.setBackgroundResource(R.drawable.status_rejected_background);
                    break;
                case "completed":
                    tvStatus.setBackgroundResource(R.drawable.status_completed_background);
                    break;
                default: // "pending"
                    tvStatus.setBackgroundResource(R.drawable.status_pending_background);
                    break;
            }

            // 3. Set up the click listeners for the doctor
            if (listener != null) {
                actionButtonsLayout.setVisibility(View.VISIBLE);

                // **CRITICAL**: This section tells the activity what button was pressed.
                btnApprove.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Approved"));
                btnReject.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Rejected"));
                btnComplete.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Completed"));
                btnSetPending.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Pending"));
                btnEdit.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Edit"));
                btnDelete.setOnClickListener(v -> listener.onAppointmentAction(appointment, "Delete"));
            } else {
                // If there's no listener (patient view), hide the buttons.
                actionButtonsLayout.setVisibility(View.GONE);
            }
        }
    }
}
