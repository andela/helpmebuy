package com.andela.helpmebuy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ReceivedRequestAdapter;
import com.andela.helpmebuy.config.Constants;
import com.andela.helpmebuy.dal.DataCallback;
import com.andela.helpmebuy.dal.firebase.FirebaseCollection;
import com.andela.helpmebuy.models.PurchaseRequest;
import com.andela.helpmebuy.models.PurchaseRequestStatus;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemDivider;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestFragment extends Fragment {
    private List<PurchaseRequest> purchaseRequests;
    private ReceivedRequestAdapter receivedRequestAdapter;
    private String user;
    private TextView noPurchaseRequests;
    RecyclerView recyclerView;
    Context context;

    public PurchaseRequestFragment() {
        purchaseRequests = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purch_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponents(view);
    }

    private void initializeComponents(View view) {
        context = getContext();
        receivedRequestAdapter = new ReceivedRequestAdapter(context, purchaseRequests);
        noPurchaseRequests = (TextView) view.findViewById(R.id.retry);
        recyclerView = (RecyclerView) view.findViewById(R.id.purch_req_recycler_view);
        recyclerView.setAdapter(receivedRequestAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
        user = CurrentUserManager.get(getContext()).getId();
        loadRequests();
    }

    private void loadRequests() {
        new FirebaseCollection<>(requestUrl(), PurchaseRequest.class)
                .getAll(new DataCallback<List<PurchaseRequest>>() {
                    @Override
                    public void onSuccess(List<PurchaseRequest> data) {
                        int size = data.size();
                        if (size > 0) {
                            for (PurchaseRequest myRequest : data) {
                                if (isRequestPending(myRequest)) {
                                    addRequest(myRequest);
                                    receivedRequestAdapter.notifyDataSetChanged();
                                }
                            }
                            if (purchaseRequests.size() > 0) {
                                noPurchaseRequests.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                noPurchaseRequests.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    private boolean isRequestPending(PurchaseRequest purchaseRequest) {
        return purchaseRequest.getReceiver().equals(user) &&
                (purchaseRequest.getPurchaseStatus() == PurchaseRequestStatus.PENDING.getStatus());
    }

    private void addRequest(PurchaseRequest newRequest) {
        int index = findIndex(newRequest);
        newRequest.setSenderId(newRequest.getId());
        if (index < 0) {
            purchaseRequests.add(newRequest);
            receivedRequestAdapter.notifyItemInserted(purchaseRequests.size() - 1);
        } else {
            purchaseRequests.set(index, newRequest);
            receivedRequestAdapter.notifyItemChanged(index);
        }
    }

    private int findIndex(PurchaseRequest requests) {
        for (int i = 0, size = purchaseRequests.size(); i < size; ++i) {
            if (requests.getId().equals(purchaseRequests.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    private String requestUrl() {
        return Constants.PURCHASE_REQUEST + "/" + CurrentUserManager.get(getContext()).getId();
    }

    private void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

