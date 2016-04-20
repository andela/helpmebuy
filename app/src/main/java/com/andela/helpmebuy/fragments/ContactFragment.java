package com.andela.helpmebuy.fragments;


import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

    private List<Contact> contacts;

    private ContactsAdapter adapter;

    private CountDownTimer countDownTimer;

    private TextView noContacts;

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
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.connection_progress_wheel);
        noContacts = (TextView)rootView.findViewById(R.id.retry);
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
        countDown();
        ContactsHelper helper = new ContactsHelper(null);
        helper.getAllContacts(currentUser.getId(), getListCallback());
    }

    private void countDown() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (progressWheel.isSpinning()) {
                    progressWheel.stopSpinning();
                }
            }
        };
        countDownTimer.start();
    }

    public void stopTimer() {
        countDownTimer.cancel();
        progressWheel.stopSpinning();
    }

    private ListCallback<Contact> getListCallback() {
        return new ListCallback<Contact>() {
            @Override
            public void onGetList(List<Contact> items) {
                stopTimer();
                if (items.size() == 0) {
                    noContacts.setVisibility(View.VISIBLE);
                } else {
                    for (Contact contact : items) {
                        contacts.add(contact);
                    }
                    adapter.notifyDataSetChanged();
                    noContacts.setVisibility(View.GONE);
                }
            }
        };
    }
}
