package com.converter.feedy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity {

    private EditText quantityEdit, expiryEdit, foodItemsEdit;
    private TextView submit;
    private ImageButton back;
    private String userID, name, phone;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        String lat = String.valueOf(latitude);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        String lang = String.valueOf(longitude);


        quantityEdit = findViewById(R.id.quantity);
        expiryEdit = findViewById(R.id.expiry);
        foodItemsEdit = findViewById(R.id.food_items);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantity = quantityEdit.getText().toString();
                String expiry = expiryEdit.getText().toString();
                String foodItems = foodItemsEdit.getText().toString();
                if (Integer.parseInt(expiry) > 9) {
                    Toast.makeText(FoodDetailsActivity.this, "Expiry duration cannot be more then 9 Hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValid(quantity, expiry, foodItems)) {
                    addInDatabaseConsole(quantity, expiry, foodItems, lat, lang, userID, name, phone);
                } else {
                    Toast.makeText(FoodDetailsActivity.this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

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
                Toast.makeText(FoodDetailsActivity.this, "Details added Successfully", Toast.LENGTH_SHORT).show();
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