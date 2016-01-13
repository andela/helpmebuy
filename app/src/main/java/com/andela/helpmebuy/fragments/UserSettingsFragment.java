package com.andela.helpmebuy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.andela.helpmebuy.R;
import com.andela.helpmebuy.activities.ChangePasswordActivity;
import com.andela.helpmebuy.activities.ForgotPasswordActivity;
import com.andela.helpmebuy.utilities.Launcher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * interface.
 */
public class UserSettingsFragment extends Fragment {


    private static final String SETTING_ITEM = "item";
    List<String> userProfileList;
    ArrayAdapter<String> userProfileListAdapter;

    private ListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


       final ListView listView = (ListView) view.findViewById(R.id.user_settings_layout);

        userProfileList = new ArrayList<>(Arrays.asList("Change Name", "Change Email", "Reset Password"));

        userProfileListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.user_settings, userProfileList);

        listView.setAdapter(userProfileListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (userProfileListAdapter.getItem(position).equals("Reset Password")) {

                    Launcher.launchActivity(getContext(), ChangePasswordActivity.class);
                }

            }
        });

        return view;

    }


}
