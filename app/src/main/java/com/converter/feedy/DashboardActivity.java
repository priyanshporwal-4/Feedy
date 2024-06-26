package com.converter.feedy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    private TextView profileTextView, name;
    private CardView logoutButton, profileButton, donateButton, donationButton, emergencyButton;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        profileTextView = findViewById(R.id.profile_textView);
        name = findViewById(R.id.name);
        logoutButton = findViewById(R.id.logout);
        back = findViewById(R.id.back);
        profileButton = findViewById(R.id.profile);
        donateButton = findViewById(R.id.donate);
        donationButton = findViewById(R.id.donations);
        emergencyButton = findViewById(R.id.emergency);

        char firstLetter = (FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()).charAt(0);
        profileTextView.setText(Character.toString(firstLetter).toUpperCase());

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        name.setText(email);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //logout current user
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "This feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "This feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });

        donationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "This feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });

        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "This feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}