package com.alif.clothingretail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alif.clothingretail.model.Clothing;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        }
    }

    public void increaseNumber(View view) {
        numberInt++;
        number.setText(String.valueOf(numberInt));
    }

    public void storeToCart(View view) {

    }

    private void getClothingDetails(String clothingId) {
        clothing.child(clothingId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Clothing clothing = dataSnapshot.getValue(Clothing.class);
                if (clothing != null) {
                    collapsingToolbarLayout.setTitle(clothing.getName());
                    Picasso.get().load(clothing.getImage())
                            .into(clothingImage);
                    clothingName.setText(clothing.getName());
                    clothingPrice.setText(clothing.getPrice());
                    clothingDescription.setText(clothing.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}