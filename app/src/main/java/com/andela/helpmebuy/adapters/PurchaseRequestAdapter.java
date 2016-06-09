package com.andela.helpmebuy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.utilities.ItemDeleteListener;

import java.util.List;

public class PurchaseRequestAdapter extends RecyclerView.Adapter<PurchaseRequestAdapter.CustomViewHolder> {

    private List<PurchaseItem> purchaseItems;
    private static ItemDeleteListener itemDeleteListener;

    public PurchaseRequestAdapter(List<PurchaseItem> purchaseItems, ItemDeleteListener itemDeleteListener) {
        this.purchaseItems = purchaseItems;
        this.itemDeleteListener = itemDeleteListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_request_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.itemName.setText(purchaseItems.get(position).getItemName());
        holder.itemDescription.setText(purchaseItems.get(position).getItemDescription());
        holder.itemQuantity.setText(purchaseItems.get(position).getItemQuantity());
        holder.itemPrice.setText(purchaseItems.get(position).getItemPrice());
    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName, itemPrice, itemDescription, itemQuantity;
        ImageButton delete;

        public CustomViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.purchase_requests_item_name);
            itemPrice = (TextView) view.findViewById(R.id.purchase_requests_item_price);
            itemDescription = (TextView) view.findViewById(R.id.purchase_requests_item_description);
            itemQuantity = (TextView) view.findViewById(R.id.purchase_requests_item_quantity);
            delete = (ImageButton) view.findViewById(R.id.delete_item_button);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemDeleteListener.deleteSelection(view, this.getLayoutPosition());
        }
    }
}