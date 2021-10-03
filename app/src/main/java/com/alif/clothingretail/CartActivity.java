package com.alif.clothingretail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.alif.clothingretail.adapter.CartAdapter;
import com.alif.clothingretail.database.Database;
import com.alif.clothingretail.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCartItems;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView tvTotalPrice;
    Button btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

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
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        rvCartItems.setAdapter(adapter);

        // Calculate price total
        int total = 0;
        for (Order order: cart) {
            total += (Integer.parseInt(order.getPrice())  * Integer.parseInt(order.getQuantity()));
            tvTotalPrice.setText("Rp" + total);
        }
    }
}