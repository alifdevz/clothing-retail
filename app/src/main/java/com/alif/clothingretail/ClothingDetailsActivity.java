package com.alif.clothingretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alif.clothingretail.database.Database;
import com.alif.clothingretail.model.Clothing;
import com.alif.clothingretail.model.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClothingDetailsActivity extends AppCompatActivity {
    private FloatingActionButton btnCart;
    private ImageView clothingImage;
    private TextView clothingName, clothingPrice, clothingDescription;
    private TextView number;
    private Button btnIncrement, btnDecrement;
    private int numberInt;

    private FirebaseDatabase database;
    private DatabaseReference clothing;
    private FirebaseRecyclerOptions<Clothing> options;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private String clothingId = "";
    private Clothing currentClothing;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_detail);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        clothing = database.getReference("clothing");
        Query query = clothing;
        options = new FirebaseRecyclerOptions.Builder<Clothing>()
                .setQuery(query, Clothing.class)
                .build();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize views
        btnCart = findViewById(R.id.btn_cart);
        clothingImage = findViewById(R.id.image_clothing_detail);
        clothingName = findViewById(R.id.clothing_name_detail);
        clothingPrice = findViewById(R.id.clothing_price_detail);
        clothingDescription = findViewById(R.id.clothing_description_detail);

        number = findViewById(R.id.number);
        btnIncrement = findViewById(R.id.btn_increment);
        btnDecrement = findViewById(R.id.btn_decrement);
        numberInt = Integer.parseInt(number.getText().toString());

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // Get clothing id from intent
        if (getIntent() != null) {
            clothingId = getIntent().getStringExtra("clothingId");
        }
        if (!clothingId.isEmpty()) {
            getClothingDetails(clothingId);
        }
    }

    public void decreaseNumber(View view) {
        if (numberInt > 1) {
            numberInt--;
            number.setText(String.valueOf(numberInt));
            updatePrice();
        }
    }

    public void increaseNumber(View view) {
        numberInt++;
        number.setText(String.valueOf(numberInt));
        updatePrice();

        // Added to firebase analytics event
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, clothingId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, currentClothing.getName());
        bundle.putString(FirebaseAnalytics.Param.PRICE, currentClothing.getPrice());
        firebaseAnalytics.logEvent("add_item_quantity", bundle);
    }

    private void updatePrice() {
        int price = Integer.parseInt(currentClothing.getPrice());
        price = price * numberInt;
        clothingPrice.setText(String.valueOf(price));
    }

    public void storeToCart(View view) {
        new Database(this).addToCart(new Order(
                clothingId,
                currentClothing.getName(),
                number.getText().toString(),
                currentClothing.getPrice()
        ));

        // Added to firebase analytics event
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, clothingId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, currentClothing.getName());
        bundle.putString(FirebaseAnalytics.Param.QUANTITY, number.getText().toString());
        bundle.putString(FirebaseAnalytics.Param.PRICE, currentClothing.getPrice());
        firebaseAnalytics.logEvent("add_to_cart", bundle);

        Toast.makeText(ClothingDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
    }

    private void getClothingDetails(String clothingId) {
        clothing.child(clothingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentClothing = dataSnapshot.getValue(Clothing.class);
                if (clothing != null) {
                    collapsingToolbarLayout.setTitle(currentClothing.getName());
                    Picasso.get().load(currentClothing.getImage())
                            .into(clothingImage);
                    clothingName.setText(currentClothing.getName());
                    clothingPrice.setText(currentClothing.getPrice());
                    clothingDescription.setText(currentClothing.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}