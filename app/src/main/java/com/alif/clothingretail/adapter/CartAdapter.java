package com.alif.clothingretail.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.clothingretail.R;
import com.alif.clothingretail.interfaces.ItemClickListener;
import com.alif.clothingretail.model.Order;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvCartItemName, tvCartItemPrice;
    public TextView tvCartItemCount;

    private ItemClickListener itemClickListener;

    public void setTvCartItemName(TextView tvCartItemName) {
        this.tvCartItemName = tvCartItemName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        tvCartItemName = itemView.findViewById(R.id.cart_item_name);
        tvCartItemPrice = itemView.findViewById(R.id.cart_item_price);
        tvCartItemCount = itemView.findViewById(R.id.tv_cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.rv_list_cart_items, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.tvCartItemName.setText(orderList.get(position).getProductName());
        int price = Integer.parseInt(orderList.get(position).getPrice()) * Integer.parseInt(orderList.get(position).getQuantity());
        holder.tvCartItemPrice.setText("Rp" + String.valueOf(price));
        holder.tvCartItemCount.setText(orderList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
