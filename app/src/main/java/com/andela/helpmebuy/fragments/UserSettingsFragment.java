package com.andela.helpmebuy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andela.helpmebuy.R;
import com.andela.helpmebuy.activities.ChangeEmailActivity;
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
    private ArrayAdapter<String> userProfileListAdapter;
    private static final String CHANGE_EMAIL = "Change Email";
    private static final String CHANGE_PASSWORD = "Change Password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        List<String> userProfileList = new ArrayList<>(Arrays.asList(CHANGE_EMAIL, CHANGE_PASSWORD));
        userProfileListAdapter = new ArrayAdapter<>(getActivity(), R.layout.user_settings, userProfileList);

        final ListView listView = (ListView) view.findViewById(R.id.user_settings_layout);
        listView.setAdapter(userProfileListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (userProfileListAdapter.getItem(position)) {
                    case CHANGE_PASSWORD:
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ConfirmPasswordFragment())
                                .commit();
                        break;
                    case CHANGE_EMAIL:
                        Launcher.launchActivity(getContext(), ChangeEmailActivity.class);
                        break;
                }
            }
        });

        return view;
    }
}
