package com.converter.feedy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.converter.feedy.databinding.ActivityLocationUsingGeminiBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class LocationUsingGeminiActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityLocationUsingGeminiBinding binding;
    private TextView submit;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private double latitude;
    private double longitude;
    private final static int FINE_PERMISSION_CODE = 1;
    private String userID, name, phone;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivityLocationUsingGeminiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //receive data from intent

        String quantity = getIntent().getStringExtra("quantity");
        String expiry = getIntent().getStringExtra("expiry");
        String foodItems = getIntent().getStringExtra("foodItems");

        fAuth = FirebaseAuth.getInstance();

        //userID
        userID = fAuth.getCurrentUser().getUid();



        //getting name of the user from cloud fireStore
        FirebaseFirestore.getInstance().collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                phone = value.getString("phone");
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ImageButton current = findViewById(R.id.current);
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude == 0.0 || longitude == 0.0) {
                    Toast.makeText(LocationUsingGeminiActivity.this, "Click on button right to next to fetch the current location", Toast.LENGTH_SHORT).show();
                } else {

                    String lat = String.valueOf(latitude);
                    String lang = String.valueOf(longitude);

                    if (Integer.parseInt(expiry) > 9) {
                        Toast.makeText(LocationUsingGeminiActivity.this, "Expiry duration cannot be more then 9 Hours", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isValid(quantity, expiry, foodItems)) {
                        addInDatabaseConsole(quantity, expiry, foodItems, lat, lang, userID, name, phone);
                    } else {
                        Toast.makeText(LocationUsingGeminiActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(LocationUsingGeminiActivity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(LocationUsingGeminiActivity.this);
                } else {
                    Toast.makeText(LocationUsingGeminiActivity.this, "Please enable location and network services.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        latitude = currentLocation.getLatitude();
        longitude = currentLocation.getLongitude();
        LatLng coordinates = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(coordinates).title("My Location"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17f));
        submit.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission is denied, Please allow", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
//
    private void addInDatabaseConsole(String quantity, String expiry, String foodItems, String lat, String lang, String userID, String name, String phone) {
        Map<String, String> foodDetails = new HashMap<>();
        foodDetails.put("latitude", lat);
        foodDetails.put("longitude", lang);
        foodDetails.put("quantity", quantity);
        foodDetails.put("food_items", foodItems);
        foodDetails.put("expiry", expiry);
        foodDetails.put("name", name);
        foodDetails.put("phone", phone);


        FirebaseFirestore.getInstance().collection("requests").document(userID).set(foodDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LocationUsingGeminiActivity.this, "Request Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AppreciationActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private boolean isValid(String quantity, String expiry, String foodItems) {
        return quantity.isEmpty() && expiry.isEmpty() && foodItems.isEmpty();
    }
    }

