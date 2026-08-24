package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartsActivity extends AppCompatActivity {

    private Button btnHeartRateChart, btnBloodPressureChart;
    private TextView tvChartTitle, tvNoData;
    private LineChart lineChart;
    private RecyclerView recyclerViewReadings;
    private HealthReadingsAdapter readingsAdapter;
    private List<HealthReading> readingsList;
    private String selectedChartType = "heart_rate";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        readingsList = new ArrayList<>();

        initViews();
        setupClickListeners();
        setupRecyclerView();
        setupChartSettings();
        loadReadings();
    }

    private void initViews() {
        btnHeartRateChart = findViewById(R.id.btnHeartRateChart);
        btnBloodPressureChart = findViewById(R.id.btnBloodPressureChart);
        tvChartTitle = findViewById(R.id.tvChartTitle);
        tvNoData = findViewById(R.id.tvNoData);
        lineChart = findViewById(R.id.lineChart);
        recyclerViewReadings = findViewById(R.id.recyclerViewReadings);

        // Bottom navigation
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        });
    }

    private void setupClickListeners() {
        btnHeartRateChart.setOnClickListener(v -> {
            selectedChartType = "heart_rate";
            updateChartSelectionUI();
            loadReadings();
        });

        btnBloodPressureChart.setOnClickListener(v -> {
            selectedChartType = "blood_pressure";
            updateChartSelectionUI();
            loadReadings();
        });
    }

    private void setupRecyclerView() {
        readingsAdapter = new HealthReadingsAdapter(readingsList);
        recyclerViewReadings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReadings.setAdapter(readingsAdapter);
    }

    private void setupChartSettings() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < readingsList.size()) {
                    Date date = readingsList.get(readingsList.size() - 1 - (int)value).getTimestamp();
                    return date != null ? mFormat.format(date) : "";
                }
                return "";
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        lineChart.getAxisRight().setEnabled(false);
    }

    private void updateChartSelectionUI() {
        if ("heart_rate".equals(selectedChartType)) {
            btnHeartRateChart.setBackgroundResource(R.drawable.button_blue);
            btnHeartRateChart.setTextColor(Color.WHITE);
            btnBloodPressureChart.setBackgroundResource(R.drawable.button_outline_blue);
            btnBloodPressureChart.setTextColor(getResources().getColor(R.color.blue_primary));
            tvChartTitle.setText("Heart Rate Trend");
        } else {
            btnBloodPressureChart.setBackgroundResource(R.drawable.button_blue);
            btnBloodPressureChart.setTextColor(Color.WHITE);
            btnHeartRateChart.setBackgroundResource(R.drawable.button_outline_blue);
            btnHeartRateChart.setTextColor(getResources().getColor(R.color.blue_primary));
            tvChartTitle.setText("Blood Pressure Trend");
        }
    }

    private void loadReadings() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        db.collection("health_readings")
                .whereEqualTo("userId", user.getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(snapshots -> {
                    readingsList.clear();
                    List<Entry> entries1 = new ArrayList<>();
                    List<Entry> entries2 = new ArrayList<>();

                    int count = snapshots.size();
                    int index = 0;
                    
                    // We iterate backwards to plot from oldest to newest
                    List<QueryDocumentSnapshot> docs = snapshots.getDocuments().stream()
                            .map(d -> (QueryDocumentSnapshot)d)
                            .collect(java.util.stream.Collectors.toList());
                    java.util.Collections.reverse(docs);

                    for (QueryDocumentSnapshot doc : docs) {
                        HealthReading reading = doc.toObject(HealthReading.class);
                        reading.setId(doc.getId());
                        
                        if ("heart_rate".equals(selectedChartType)) {
                            if (reading.getHeartRate() != null) {
                                entries1.add(new Entry(index, reading.getHeartRate().floatValue()));
                            }
                        } else {
                            if (reading.getBloodPressureSystolic() != null) {
                                entries1.add(new Entry(index, reading.getBloodPressureSystolic().floatValue()));
                            }
                            if (reading.getBloodPressureDiastolic() != null) {
                                entries2.add(new Entry(index, reading.getBloodPressureDiastolic().floatValue()));
                            }
                        }
                        index++;
                    }

                    // For the list view, we want newest first
                    readingsList.addAll(snapshots.toObjects(HealthReading.class));
                    readingsAdapter.notifyDataSetChanged();

                    if (entries1.isEmpty()) {
                        lineChart.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    } else {
                        lineChart.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                        displayChart(entries1, entries2);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayChart(List<Entry> entries1, List<Entry> entries2) {
        LineData lineData = new LineData();

        if ("heart_rate".equals(selectedChartType)) {
            LineDataSet set = new LineDataSet(entries1, "Heart Rate (BPM)");
            styleDataSet(set, Color.BLUE);
            lineData.addDataSet(set);
        } else {
            LineDataSet setSys = new LineDataSet(entries1, "Systolic (mmHg)");
            styleDataSet(setSys, Color.RED);
            lineData.addDataSet(setSys);

            LineDataSet setDia = new LineDataSet(entries2, "Diastolic (mmHg)");
            styleDataSet(setDia, Color.MAGENTA);
            lineData.addDataSet(setDia);
        }

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void styleDataSet(LineDataSet set, int color) {
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setFillColor(color);
        set.setFillAlpha(50);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }
}