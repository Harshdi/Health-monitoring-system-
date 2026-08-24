package com.harshdi.healthmonitoringsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ReadingsAdapter extends RecyclerView.Adapter<ReadingsAdapter.ReadingViewHolder> {

    private List<Reading> readings;

    public ReadingsAdapter(List<Reading> readings) {
        this.readings = readings;
    }

    @NonNull
    @Override
    public ReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reading, parent, false);
        return new ReadingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingViewHolder holder, int position) {
        Reading reading = readings.get(position);

        // Set type icon and text
        if ("heart_rate".equals(reading.getType())) {
            holder.tvType.setText("❤️ Heart Rate");
            holder.tvValue.setText(reading.getValue());
        } else {
            holder.tvType.setText("🩸 Blood Pressure");
            holder.tvValue.setText(reading.getValue());
        }

        holder.tvTimestamp.setText(reading.getTimestamp());

        if (reading.getNotes() != null && !reading.getNotes().isEmpty()) {
            holder.tvNotes.setText(reading.getNotes());
            holder.tvNotes.setVisibility(View.VISIBLE);
        } else {
            holder.tvNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return readings.size();
    }

    static class ReadingViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvValue, tvTimestamp, tvNotes;

        public ReadingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvValue = itemView.findViewById(R.id.tvValue);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvNotes = itemView.findViewById(R.id.tvNotes);
        }
    }
}