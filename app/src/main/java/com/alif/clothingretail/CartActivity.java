package com.alif.clothingretail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCartItems;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView tvTotalPrice;
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("requests");

        // Initialize views
        rvCartItems = findViewById(R.id.rv_cart_items);
        rvCartItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvCartItems.setLayoutManager(layoutManager);

        tvTotalPrice = findViewById(R.id.total_price);
        btnPlaceOrder = findViewById(R.id.btn_place_order);

        loadOrderList();
    }

    private void loadOrderList() {

    }
}