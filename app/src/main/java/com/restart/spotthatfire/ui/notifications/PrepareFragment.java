package com.restart.spotthatfire.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.restart.spotthatfire.R;
import com.restart.spotthatfire.ui.dashboard.ReportFragment;
import com.restart.spotthatfire.ui.notifications.go.GoFragment;
import com.restart.spotthatfire.ui.notifications.ready.ReadyFragment;
import com.restart.spotthatfire.ui.notifications.set.SetFragment;

public class PrepareFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        view.findViewById(R.id.readyLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                final FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();

                switch (view.getId()) {
                    case R.id.readyLayout:
                        ft.replace(R.id.nav_host_fragment, new ReadyFragment(), ReportFragment.class.getSimpleName());
                        ft.commit();
                        ft.addToBackStack(null);
                        break;

                    case R.id.setLayout:
                        ft.replace(R.id.nav_host_fragment, new SetFragment(), ReportFragment.class.getSimpleName());
                        ft.commit();
                        ft.addToBackStack(null);
                        break;

                    case R.id.goLayout:
                        ft.replace(R.id.nav_host_fragment, new GoFragment(), ReportFragment.class.getSimpleName());
                        ft.commit();
                        ft.addToBackStack(null);
                        break;
                }
            }
        });

        return view;
    }
}