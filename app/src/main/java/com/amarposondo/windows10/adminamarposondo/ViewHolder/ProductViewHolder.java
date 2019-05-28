package com.amarposondo.windows10.adminamarposondo.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amarposondo.windows10.adminamarposondo.Interface.ItemClickListner;
import com.amarposondo.windows10.adminamarposondo.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName, txtProductescription, txtproductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image_item);
        txtProductName = itemView.findViewById(R.id.product_name_item);
        txtProductescription = itemView.findViewById(R.id.product_description_item);
        txtproductPrice = itemView.findViewById(R.id.product_price_item);


    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View v)
    {
        listner.onClick(v, getAdapterPosition(), false);

    }

}
