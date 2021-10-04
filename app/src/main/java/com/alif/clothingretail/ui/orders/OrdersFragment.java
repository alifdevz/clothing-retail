package com.alif.clothingretail.ui.orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alif.clothingretail.R;
import com.alif.clothingretail.common.Common;
import com.alif.clothingretail.model.Request;
import com.alif.clothingretail.viewholder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvOrderItems;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    private FirebaseRecyclerOptions<Request> options;
    private FirebaseDatabase database;
    private DatabaseReference requests;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        Query query = requests
                .orderByChild("phoneNumber")
                .equalTo(Common.currentUser.getPhoneNumber());
        options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        // Initialize views
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        rvOrderItems = view.findViewById(R.id.rv_order_items);
        rvOrderItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rvOrderItems.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhoneNumber());

        return view;
    }

    private void loadOrders(String phoneNumber) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull Request model) {
                viewHolder.tvOrderId.setText(getString(R.string.txt_order_id) + adapter.getRef(position).getKey());
                viewHolder.tvOrderStatus.setText(getString(R.string.txt_status) + model.getStatus());
                viewHolder.tvOrderPhoneNumber.setText(getString(R.string.txt_phone_number) + model.getPhoneNumber());
                viewHolder.tvOrderAddress.setText(getString(R.string.txt_address) + model.getAddress());
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_order_items, parent, false);
                return new OrderViewHolder(view);
            }
        };
        adapter.startListening();
        rvOrderItems.setAdapter(adapter);
    }
}