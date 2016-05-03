package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseItem;

import java.util.List;

/**
 * Created by andeladev on 03/05/2016.
 */
public class PurchaseRequestAdapter extends RecyclerView.Adapter<PurchaseRequestAdapter.CustomViewHolder> {


    private Context context;

    private List<PurchaseItem> purchaseItems;


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        PurchaseItem purchaseItem = purchaseItems.get(position);
        holder.item.setText(purchaseItem.getItem());
    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        public CustomViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(R.id.purchase_requests_item_text);
        }
    }
}
