package com.andela.helpmebuy.adapters;

import android.content.Context;
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


    private Context context;

    private List<String> purchaseItems;
    private static ItemDeleteListener itemDeleteListener;

    public PurchaseRequestAdapter(Context context, List<String> purchaseItems, ItemDeleteListener itemDeleteListener) {
        this.purchaseItems = purchaseItems;
        this.context = context;
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
        PurchaseItem purchaseItem = new PurchaseItem();
        holder.item.setText(purchaseItems.get(position));
    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item;
        ImageButton delete;

        public CustomViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(R.id.purchase_requests_item_text);
            delete = (ImageButton) view.findViewById(R.id.delete_item_button);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemDeleteListener.deleteSelection(view, this.getLayoutPosition());
        }
    }
}