package com.alif.clothingretail.ui.cart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alif.clothingretail.HomeActivity;
import com.alif.clothingretail.R;
import com.alif.clothingretail.SignInActivity;
import com.alif.clothingretail.adapter.CartAdapter;
import com.alif.clothingretail.common.Common;
import com.alif.clothingretail.database.Database;
import com.alif.clothingretail.interfaces.DeleteButtonClickListener;
import com.alif.clothingretail.model.Order;
import com.alif.clothingretail.model.Request;
import com.alif.clothingretail.ui.items.ItemsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
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

    private FirebaseAnalytics firebaseAnalytics;

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
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        // Initialize views
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        rvCartItems = view.findViewById(R.id.rv_cart_items);
        rvCartItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvCartItems.setLayoutManager(layoutManager);

        tvTotalPrice = view.findViewById(R.id.total_price);
        btnPlaceOrder = view.findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        loadOrderList();

        return view;
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter your address: ");

        final EditText edtAddress = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(layoutParams);
        alertDialog.setView(edtAddress); // Add edittext to alertdialog
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressDialog mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Please wait...");
                mDialog.show();

                String status = "Waiting";
                // Create new request
                Request request = new Request(
                        Common.currentUser.getPhoneNumber(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        tvTotalPrice.getText().toString(),
                        cart,
                        status
                );

                // Submit to Firebase
                // Use System.CurrentTimeMillis as key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                // Added to firebase analytics event
                Bundle bundle = new Bundle();
                bundle.putString("phone_number", Common.currentUser.getPhoneNumber());
                bundle.putString("username", Common.currentUser.getName());
                bundle.putString("address", edtAddress.getText().toString());
                bundle.putString("total_price", tvTotalPrice.getText().toString());
                firebaseAnalytics.logEvent("purchase", bundle);

                // Delete cart
                new Database(getActivity()).cleanCart();
                Toast.makeText(getActivity(), "Thank you for ordering!", Toast.LENGTH_SHORT).show();

                // Go back to ItemsFragment (HomeActivity's first fragment)
                mDialog.dismiss();
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadOrderList() {
        cart = new Database(getActivity()).getCarts();
        adapter = new CartAdapter(cart, getActivity());
        adapter.setDeleteButtonClickListener(new DeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClicked(int totalPrice) {
                tvTotalPrice.setText(String.valueOf(totalPrice));
            }
        });
        rvCartItems.setAdapter(adapter);

        // Calculate price total
        int total = 0;
        for (Order order: cart) {
            total += (Integer.parseInt(order.getPrice())  * Integer.parseInt(order.getQuantity()));
            tvTotalPrice.setText("Rp" + total);
        }
    }
}