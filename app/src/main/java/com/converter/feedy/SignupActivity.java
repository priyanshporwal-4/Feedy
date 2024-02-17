package com.converter.feedy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText nameEdit, phoneEdit, emailEdit, passwordEdit, confirmPasswordEdit;
    private Button submit;
    private TextView login;
    ProgressBar progressBar;
    FirebaseAuth fauth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEdit = findViewById(R.id.name);
        phoneEdit = findViewById(R.id.phone);
        emailEdit = findViewById(R.id.email);
        passwordEdit = findViewById(R.id.password);
        confirmPasswordEdit = findViewById(R.id.confirm_password);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_bar);

        fauth = FirebaseAuth.getInstance();
//        if (fauth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        //on submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String email = emailEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String confirmPassword = confirmPasswordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEdit.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordEdit.setError("Password is Required.");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    confirmPasswordEdit.setError("This is Required Field.");
                    return;
                }
                if (password.length() < 8) {
                    passwordEdit.setError("Password size is too small.");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    confirmPasswordEdit.setError("It should be same as password.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register user in Firebase
                fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "You are Registered", Toast.LENGTH_SHORT).show();
                            userID = fauth.getCurrentUser().getUid();
                            addInFireStore(name, phone, email, userID, password);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        //on login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

    private void addInFireStore(String name, String phone, String email, String userID, String password) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("phone", phone);
        userData.put("email", email);
        userData.put("password", password);

        FirebaseFirestore.getInstance().collection("users").document(userID).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user profile is created for : " + userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Registration Failed");
            }
        });
    }
}