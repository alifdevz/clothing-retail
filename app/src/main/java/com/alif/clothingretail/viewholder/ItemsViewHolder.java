package com.alif.clothingretail.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alif.clothingretail.R;
import com.alif.clothingretail.RecyclerViewMockData;
import com.squareup.picasso.Picasso;

public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView itemImage;
    public TextView itemName;

    public ItemsViewHolder(View view) {
        super(view);
        itemImage = view.findViewById(R.id.clothing_item_image);
        itemName = view.findViewById(R.id.clothing_item_name);
        view.setOnClickListener(this);
    }

    public void bindView(int position) {
        // itemImage.setImageResource(RecyclerViewMockData.itemImage[position]);
        Picasso.get()
                .load(RecyclerViewMockData.itemImageFromUrl[position])
                .placeholder(R.drawable.no_image)
                .into(itemImage);
        itemName.setText(RecyclerViewMockData.itemName[position]);
    }

    @Override
    public void onClick(View v) {

    }
}
