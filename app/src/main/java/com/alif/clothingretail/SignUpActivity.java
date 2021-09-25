package com.alif.clothingretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alif.clothingretail.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {
    MaterialEditText editPhoneNumber, editName, editPassword;
    Button btnSignUp;

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Check if the user phone number has already been registered
                        if (dataSnapshot.child(editPhoneNumber.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone number is already registered!", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            User user = new User(editName.getText().toString(), editPassword.getText().toString());
                            table_user.child(editPhoneNumber.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
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