package com.alif.clothingretail.viewholder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alif.clothingretail.ClothingDetailsActivity;
import com.alif.clothingretail.R;
import com.alif.clothingretail.RecyclerViewMockData;
import com.alif.clothingretail.interfaces.ItemClickListener;
import com.squareup.picasso.Picasso;

public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView itemImage;
    public TextView itemName;
    private ItemClickListener itemClickListener;

    public ItemsViewHolder(View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.clothing_item_image);
        itemName = itemView.findViewById(R.id.clothing_item_name);
        itemView.setOnClickListener(this);
    }

    public void bindView(int position) {
        // itemImage.setImageResource(RecyclerViewMockData.itemImage[position]);
        Picasso.get()
                .load(RecyclerViewMockData.itemImageFromUrl[position])
                .placeholder(R.drawable.no_image)
                .into(itemImage);
        itemName.setText(RecyclerViewMockData.itemName[position]);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        // Intent clothingDetailsIntent = new Intent(view.getContext(), ClothingDetailsActivity.class);
        // view.getContext().startActivity(clothingDetailsIntent);
        itemClickListener.onClick(view , getAbsoluteAdapterPosition(), false);
    }
}
