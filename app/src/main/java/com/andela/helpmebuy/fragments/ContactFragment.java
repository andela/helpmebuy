package com.andela.helpmebuy.fragments;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.adapters.ContactsAdapter;
import com.andela.helpmebuy.models.Contact;
import com.andela.helpmebuy.models.User;
import com.andela.helpmebuy.utilities.ContactsHelper;
import com.andela.helpmebuy.utilities.CurrentUserManager;
import com.andela.helpmebuy.utilities.ItemDivider;
import com.andela.helpmebuy.utilities.ListCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private View rootView;

    private Context context;

    private User currentUser;

    private ProgressWheel progressWheel;

    private ImageView retry;

    private List<Contact> contacts;

    private ContactsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.rootView = view;
        context = getActivity();
        contacts = new ArrayList<>();
        initializeComponents();
        initializeRecyclerView();
        getCurrentUser();
    }

    private void initializeComponents() {
        retry = (ImageView) rootView.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressWheel.spin();
                loadContacts();
            }
        });
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.connection_progress_wheel);
    }

    private void initializeRecyclerView() {
        adapter = new ContactsAdapter(context, contacts);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ItemDivider(getContext()));
    }

    private void getCurrentUser() {
        currentUser = CurrentUserManager.get(context);
        loadContacts();
    }

    private void loadContacts() {
        progressWheel.spin();
        ContactsHelper helper = new ContactsHelper(null);
        helper.getAllContacts(currentUser.getId(), getListCallback());
    }

    private ListCallback<Contact> getListCallback() {
        return new ListCallback<Contact>() {
            @Override
            public void onGetList(List<Contact> items) {
                progressWheel.stopSpinning();
                if (items.size() == 0) {
                    retry.setVisibility(View.VISIBLE);
                } else {
                    for (Contact contact : items) {
                        contacts.add(contact);
                    }
                    adapter.notifyDataSetChanged();
                    retry.setVisibility(View.GONE);
                }
            }
        };
    }
}
