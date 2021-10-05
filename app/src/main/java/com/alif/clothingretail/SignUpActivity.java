package com.alif.clothingretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alif.clothingretail.model.User;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {
    private MaterialEditText editPhoneNumber, editName, editPassword;
    private Button btnSignUp;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editPhoneNumber = findViewById(R.id.edt_phone_number);
        editName = findViewById(R.id.edt_name);
        editPassword = findViewById(R.id.edt_password);
        btnSignUp = findViewById(R.id.btn_sign_up);

        // Initialize Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    boolean dataExists = false;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check whether the user phone number has already been registered or not
                        if (dataSnapshot.child(editPhoneNumber.getText().toString()).exists()) {
                            mDialog.dismiss();
                            if (!dataExists) {
                                Toast.makeText(SignUpActivity.this, "Phone number is already registered!", Toast.LENGTH_SHORT).show();
                                dataExists = true;
                            }
                        } else {
                            mDialog.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString(), editPhoneNumber.getText().toString());
                            table_user.child(editPhoneNumber.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();

                            // Added to firebase analytics event
                            Bundle bundle = new Bundle();
                            bundle.putString("username", user.getName());
                            bundle.putString("password", user.getPassword());
                            bundle.putString("phone_number", user.getPhoneNumber());
                            firebaseAnalytics.logEvent("sign_up", bundle);
                            dataExists = true;
                            finish();
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