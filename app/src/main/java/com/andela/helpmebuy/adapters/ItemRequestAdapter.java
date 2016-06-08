package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;

import java.util.List;

/**
 * Created by andeladev on 24/05/2016.
 */
public class ItemRequestAdapter extends RecyclerView.Adapter<ItemRequestAdapter.CustomViewHolder> {
    private Context context;
    private List<PurchaseItem> purchaseItems;

    public ItemRequestAdapter(Context context, PurchaseRequest purchaseRequest) {
        this.purchaseItems = purchaseRequest.getPurchaseList();
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
    }

    @Override
    public int getItemCount() {
        return purchaseItems.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName, itemPrice, itemDescription, itemQuantity;
        Button accept, reject;

        public CustomViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.tv_purchase_item_name);
            itemPrice = (TextView) view.findViewById(R.id.tv_purchase_item_price);
            itemDescription = (TextView) view.findViewById(R.id.tv_purchase_item_desc);
            itemQuantity = (TextView) view.findViewById(R.id.tv_purchase_item_quantity);
            accept = (Button) view.findViewById(R.id.purch_req_accept);
            reject = (Button) view.findViewById(R.id.purch_req_reject);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id) {
                case (R.id.purch_req_accept):
                    break;
                case (R.id.purch_req_reject):
                    break;
            }
        }
    }
}
