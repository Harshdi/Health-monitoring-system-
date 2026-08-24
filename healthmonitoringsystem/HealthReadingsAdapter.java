package com.harshdi.healthmonitoringsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HealthReadingsAdapter extends RecyclerView.Adapter<HealthReadingsAdapter.ReadingViewHolder> {

    private List<HealthReading> readingsList;
    private OnReadingActionListener listener;

    public interface OnReadingActionListener {
        void onEditClick(HealthReading reading);
        void onDeleteClick(HealthReading reading);
    }

    public HealthReadingsAdapter(List<HealthReading> readingsList) {
        this.readingsList = readingsList;
    }

    public void setOnReadingActionListener(OnReadingActionListener listener) {
        this.listener = listener;
    }

    // Add this missing method
    public void updateList(List<HealthReading> newList) {
        this.readingsList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_reading, parent, false);
        return new ReadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingViewHolder holder, int position) {
        HealthReading reading = readingsList.get(position);
        holder.bind(reading, listener);
    }

    @Override
    public int getItemCount() {
        return readingsList != null ? readingsList.size() : 0;
    }

    static class ReadingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTimestamp, tvHeartRate, tvBloodPressure, tvTemperature, tvOxygenLevel;
        private final ImageView btnEdit, btnDelete;

        public ReadingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvHeartRate = itemView.findViewById(R.id.tvHeartRate);
            tvBloodPressure = itemView.findViewById(R.id.tvBloodPressure);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvOxygenLevel = itemView.findViewById(R.id.tvOxygenLevel);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final HealthReading reading, OnReadingActionListener listener) {
            if (reading.getTimestamp() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy\nhh:mm a", Locale.getDefault());
                tvTimestamp.setText(sdf.format(reading.getTimestamp()));
            } else {
                tvTimestamp.setText("N/A");
            }

            tvHeartRate.setText(reading.getHeartRate() != null ? reading.getHeartRate() + " BPM" : "--");
            if (reading.getBloodPressureSystolic() != null && reading.getBloodPressureDiastolic() != null) {
                tvBloodPressure.setText(reading.getBloodPressureSystolic() + "/" + reading.getBloodPressureDiastolic());
            } else {
                tvBloodPressure.setText("--/--");
            }
            tvTemperature.setText(reading.getTemperature() != null ? String.format(Locale.US, "%.1f°F", reading.getTemperature()) : "--");
            tvOxygenLevel.setText(reading.getOxygenLevel() != null ? reading.getOxygenLevel() + "%" : "--");

            if (listener != null) {
                if (btnEdit != null) {
                    btnEdit.setVisibility(View.VISIBLE);
                    btnEdit.setOnClickListener(v -> listener.onEditClick(reading));
                }
                if (btnDelete != null) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnDelete.setOnClickListener(v -> listener.onDeleteClick(reading));
                }
            }
        }
    }
}