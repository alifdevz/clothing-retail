package com.alif.clothingretail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alif.clothingretail.R;
import com.alif.clothingretail.RecyclerViewMockData;
import com.alif.clothingretail.viewholder.ItemsViewHolder;
import com.squareup.picasso.Picasso;

public class ItemsAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_list_clothing_items, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // ((ItemsViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return RecyclerViewMockData.itemName.length;
    }
}
