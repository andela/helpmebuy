package com.andela.helpmebuy.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.helpmebuy.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RequestActivityFragment extends Fragment {

    public RequestActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }
}
