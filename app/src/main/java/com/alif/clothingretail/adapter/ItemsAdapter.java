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
        ((ItemsViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return RecyclerViewMockData.itemName.length;
    }

    private class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView itemImage;
        private TextView itemName;

        public ItemsViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.clothing_item_image);
            itemName = view.findViewById(R.id.clothing_item_name);
            view.setOnClickListener(this);
        }

        public void bindView(int position) {
            // itemImage.setImageResource(RecyclerViewMockData.itemImage[position]);
            Picasso.get()
                    .load(RecyclerViewMockData.itemImage[position])
                    .placeholder(R.drawable.no_image)
                    .into(itemImage);
            itemName.setText(RecyclerViewMockData.itemName[position]);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
