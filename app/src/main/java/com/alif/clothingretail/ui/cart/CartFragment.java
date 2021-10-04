package com.alif.clothingretail.ui.cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alif.clothingretail.R;
import com.alif.clothingretail.adapter.CartAdapter;
import com.alif.clothingretail.database.Database;
import com.alif.clothingretail.model.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCartItems;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseDatabase database;
    private DatabaseReference requests;

    private TextView tvTotalPrice;
    private Button btnPlaceOrder;

    private List<Order> cart = new ArrayList<>();
    private CartAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("requests");

        // Initialize views
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        rvCartItems = view.findViewById(R.id.rv_cart_items);
        rvCartItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvCartItems.setLayoutManager(layoutManager);

        tvTotalPrice = view.findViewById(R.id.total_price);
        btnPlaceOrder = view.findViewById(R.id.btn_place_order);

        loadOrderList();

        return view;
    }

    private void loadOrderList() {
        cart = new Database(getActivity()).getCarts();
        adapter = new CartAdapter(cart, getActivity());
        rvCartItems.setAdapter(adapter);

        // Calculate price total
        int total = 0;
        for (Order order: cart) {
            total += (Integer.parseInt(order.getPrice())  * Integer.parseInt(order.getQuantity()));
            tvTotalPrice.setText("Rp" + total);
        }
    }
}