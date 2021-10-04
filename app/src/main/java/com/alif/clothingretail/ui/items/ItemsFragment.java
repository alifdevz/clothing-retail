package com.alif.clothingretail.ui.items;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alif.clothingretail.ClothingDetailsActivity;
import com.alif.clothingretail.HomeActivity;
import com.alif.clothingretail.R;
import com.alif.clothingretail.interfaces.ItemClickListener;
import com.alif.clothingretail.model.Clothing;
import com.alif.clothingretail.viewholder.ItemsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase database;
    private DatabaseReference clothing;
    private FirebaseRecyclerOptions<Clothing> options;
    private FirebaseRecyclerAdapter<Clothing, ItemsViewHolder> adapter;
    private RecyclerView rvClothingItems;

    public ItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        rvClothingItems = view.findViewById(R.id.rv_clothing_items);
        rvClothingItems.setHasFixedSize(true);
        // ItemsAdapter adapter = new ItemsAdapter();
        // rvClothingItems.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvClothingItems.setLayoutManager(layoutManager);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        clothing = database.getReference("clothing");
        Query query = clothing;
        options = new FirebaseRecyclerOptions.Builder<Clothing>()
                .setQuery(query, Clothing.class)
                .build();

        loadItems();

        return view;
    }

    private void loadItems() {
        adapter = new FirebaseRecyclerAdapter<Clothing, ItemsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemsViewHolder viewHolder, int position, @NonNull Clothing model) {
                viewHolder.itemName.setText(model.getName());
                Picasso.get().load(model.getImage())
                        .into(viewHolder.itemImage);
                final Clothing local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent clothingDetailsIntent = new Intent(getActivity(), ClothingDetailsActivity.class);
                        clothingDetailsIntent.putExtra("clothingId", adapter.getRef(position).getKey());
                        startActivity(clothingDetailsIntent);
                        // Toast.makeText(getActivity(), "clothingId: " + local.getName(), Toast.LENGTH_SHORT).show();
                        // Toast.makeText(getActivity(), "clothingId: " + adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_clothing_items, parent, false);
                return new ItemsViewHolder(view);
            }
        };
        adapter.startListening();
        rvClothingItems.setAdapter(adapter);
    }
}