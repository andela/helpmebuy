package com.andela.helpmebuy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.models.PurchaseRequestStatus;
import com.andela.helpmebuy.utilities.DateManager;

import java.util.ArrayList;

public class PurchaseRequestHistoryAdapter extends RecyclerView.Adapter<PurchaseRequestHistoryAdapter.CustomViewHolder> {
    private ArrayList<PurchaseRequest> purchaseRequestList;

    public PurchaseRequestHistoryAdapter(ArrayList<PurchaseRequest> purchaseRequestList) {
        this.purchaseRequestList = purchaseRequestList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.purchase_request_history_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        DateManager dateManager = new DateManager();
        PurchaseRequest purchaseRequest = purchaseRequestList.get(position);
        holder.name.setText(purchaseRequest.getReceiverFullname());
        holder.status.setText((getStatus(purchaseRequest.getPurchaseStatus())));
        holder.date.setText(dateManager.formatTime(purchaseRequest.getDateMillis()));
    }

    @Override
    public int getItemCount() {
        return purchaseRequestList.size();
    }

    public String getStatus(int statusInt) {
        if (statusInt == PurchaseRequestStatus.ACCEPTED.getStatus()) {
            return "ACCEPTED";
        }
        if (statusInt == PurchaseRequestStatus.PENDING.getStatus()) {
            return "PENDING";
        }
        if (statusInt == PurchaseRequestStatus.REJECTED.getStatus()) {
            return "REJECTED";
        }
        return null;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name, status, date;

        public CustomViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.purchase_request_history_name);
            status = (TextView) view.findViewById(R.id.purchase_request_history_status);
            date = (TextView) view.findViewById(R.id.purchase_request_history_date);
        }
    }
}