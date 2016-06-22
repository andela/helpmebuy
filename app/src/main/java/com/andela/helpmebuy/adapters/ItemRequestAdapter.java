package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andeladev on 24/05/2016.
 */
public class ItemRequestAdapter extends RecyclerView.Adapter<ItemRequestAdapter.CustomViewHolder> {
    private Context context;
    private List<PurchaseItem> purchaseItems;

    public ItemRequestAdapter(Context context, ArrayList<PurchaseItem> purchaseItem) {
        this.purchaseItems = purchaseItem;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_request_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        PurchaseItem purchaseItem = purchaseItems.get(position);
        holder.itemName.setText(purchaseItem.getItemName());
        holder.itemDescription.setText(purchaseItem.getItemDescription().toString());
        holder.itemPrice.setText(purchaseItem.getItemPrice().toString());
        holder.itemQuantity.setText((purchaseItem.getItemQuantity()));
    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemDescription, itemQuantity;

        public CustomViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.tv_purchase_item_name);
            itemPrice = (TextView) view.findViewById(R.id.tv_purchase_item_price);
            itemDescription = (TextView) view.findViewById(R.id.tv_purchase_item_desc);
            itemQuantity = (TextView) view.findViewById(R.id.tv_purchase_item_quantity);
        }
    }
}
