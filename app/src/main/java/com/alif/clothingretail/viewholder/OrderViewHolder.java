package com.alif.clothingretail.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.clothingretail.R;
import com.alif.clothingretail.interfaces.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvOrderId, tvOrderStatus, tvOrderPhoneNumber, tvOrderAddress;
    private ItemClickListener itemClickListener;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        tvOrderId = itemView.findViewById(R.id.tv_order_id);
        tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
        tvOrderPhoneNumber = itemView.findViewById(R.id.tv_order_phone_number);
        tvOrderAddress = itemView.findViewById(R.id.tv_order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAbsoluteAdapterPosition(), false);
    }
}
