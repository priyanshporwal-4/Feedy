package com.converter.feedy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class GeminiActivity extends AppCompatActivity {

    private TextInputEditText prompt;
    private Button go;
    private TextView textViewBot, back, next;
    private ProgressBar progressBar;
    private String person, expiry, food_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gemini);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        prompt = findViewById(R.id.prompt_input);
        go = findViewById(R.id.generate_button);
        textViewBot = findViewById(R.id.textView_bot);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.progress_bar);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeminiActivity.this, MainActivity.class));
                finish();
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeminiPro model = new GeminiPro();

                String promptString = prompt.getText().toString();

                if (promptString.isEmpty()) {
                    Toast.makeText(GeminiActivity.this, "Please enter your request", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                String prompt1 = promptString + "'Give me answer only in number, give me number of persons to feed is described in this sentence'";
                String prompt2 = promptString + "'Give me answer only in number of the hours talked in this sentence.'";
                String prompt3 = promptString + "''Give me answer only with names of dishes or food items described in this sentence.";

                textViewBot.setText(prompt.getText().toString());
                prompt.setText("");


                model.getResponse(prompt1, new ResponseCallback() {
                    @Override
                    public void onResponse(String response) {

                        person = response;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(GeminiActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                model.getResponse(prompt2, new ResponseCallback() {
                    @Override
                    public void onResponse(String response) {
                        expiry = response;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(GeminiActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                model.getResponse(prompt3, new ResponseCallback() {
                    @Override
                    public void onResponse(String response) {
                        food_items = response;
                        progressBar.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(GeminiActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeminiActivity.this, LocationUsingGeminiActivity.class);
                intent.putExtra("quantity", person);
                intent.putExtra("expiry", expiry);
                intent.putExtra("foodItems", food_items);
                startActivity(intent);
                finish();
            }
        });
    }

}