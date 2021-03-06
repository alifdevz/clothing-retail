package com.alif.clothingretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alif.clothingretail.common.Common;
import com.alif.clothingretail.model.User;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {
    private MaterialEditText editPhone, editPassword;
    private Button btnSignIn;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPhone = findViewById(R.id.edt_phone_number);
        editPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in_lv2);

        // Initialize Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();
                // User addListenerForSingleValueEvent (not addValueEventListener) to load user profile or sign in
                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check whether any user exists in database or not
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            // Get user information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(editPassword.getText().toString())) {
                                // Toast.makeText(SignInActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);

                                // Added to firebase analytics event
                                Bundle bundle = new Bundle();
                                bundle.putString("username", user.getName());
                                bundle.putString("password", user.getPassword());
                                bundle.putString("phone_number", user.getPhoneNumber());
                                firebaseAnalytics.logEvent("login", bundle);
                            } else {
                                // Wrong password
                                Toast.makeText(SignInActivity.this, "Wrong phone number or password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            // Wrong phone number (User does not exist in database)
                            Toast.makeText(SignInActivity.this, "Wrong phone number or password!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}