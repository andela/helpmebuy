package com.andela.helpmebuy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.activities.PurchaseReqResponse;
import com.andela.helpmebuy.models.PurchaseItem;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.utilities.DateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andeladev on 01/06/2016.
 */
public class ReceivedRequestAdapter extends RecyclerView.Adapter<ReceivedRequestAdapter.CustomViewHolder> {

    private Context context;

    private List<PurchaseRequest> purchaseRequests;

    public ReceivedRequestAdapter(Context context, List<PurchaseRequest> request) {
        this.purchaseRequests = request;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_request_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        DateManager dateManager = new DateManager();
        PurchaseRequest request = purchaseRequests.get(position);
        String items = getItemsDescription(request);
        String date = dateManager.formatTime(request.getDateMillis());
        holder.sender.setText(request.getSendersFullName());
        holder.description.setText(items);
        holder.date.setText(date);
    }

    public String getItemsDescription(PurchaseRequest request){
        int limit = 3;
        ArrayList<PurchaseItem> items = request.getPurchaseList();
        String itemName = items.get(0).getItemName();
        for (int i = 1; i <= limit ;i++){
            if(i < items.size()){
                itemName = itemName + ", " + items.get(i).getItemName();
            }
        }
        if(items.size() > limit ){
            itemName += "...";
        }
        return itemName;
    }

    @Override
    public int getItemCount() {
        return purchaseRequests.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sender, description, date;


        public CustomViewHolder(View view) {
            super(view);
            sender = (TextView) view.findViewById(R.id.tv_purchase_req_sender);
            description = (TextView) view.findViewById(R.id.tv_purchase_request_desc);
            date = (TextView) view.findViewById(R.id.tv_purchase_request_date);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getAdapterPosition();
            PurchaseRequest request = purchaseRequests.get(mPosition);
            Intent intent = new Intent(context, PurchaseReqResponse.class);
            intent.putExtra("request", request);
            context.startActivity(intent);
        }
    }
}
