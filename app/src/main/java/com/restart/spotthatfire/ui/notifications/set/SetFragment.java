package com.restart.spotthatfire.ui.notifications.set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restart.spotthatfire.R;

import androidx.fragment.app.Fragment;

public class SetFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ready, container, false);
    }
}