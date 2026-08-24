package com.harshdi.healthmonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class DoctorRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- FIX ---
        // This activity is being replaced by the unified RegisterActivity.
        // To ensure a consistent user experience, we immediately redirect to the
        // correct registration screen with the "doctor" type pre-selected.
        Intent intent = new Intent(DoctorRegisterActivity.this, RegisterActivity.class);
        intent.putExtra("userType", "doctor");
        startActivity(intent);
        finish(); // Close this activity immediately so the user cannot navigate back to it.
    }
}
