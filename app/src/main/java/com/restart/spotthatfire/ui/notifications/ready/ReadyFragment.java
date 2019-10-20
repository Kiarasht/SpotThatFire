package com.restart.spotthatfire.ui.notifications.ready;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.restart.spotthatfire.MainActivity;
import com.restart.spotthatfire.R;

public class ReadyFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {


    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ready, container, false);

        ((RadioButton) view.findViewById(R.id.radioButton3)).setOnCheckedChangeListener(this);
        ((RadioButton) view.findViewById(R.id.radioButton2)).setOnCheckedChangeListener(this);
        ((RadioButton) view.findViewById(R.id.radioButton1)).setOnCheckedChangeListener(this);
        button = view.findViewById(R.id.grayButton);
        button.setOnClickListener(this);

        return view;

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.radioButton3:
                MainActivity.radioValues[0] = b;
                break;

            case R.id.radioButton2:
                MainActivity.radioValues[1] = b;
                break;

            case R.id.radioButton1:
                MainActivity.radioValues[2] = b;
                break;
        }

        if( MainActivity.radioValues[0] & MainActivity.radioValues[1] & MainActivity.radioValues[2] ) {
            button.setEnabled(true);
            button.setBackgroundColor(Color.GREEN);
            button.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void onClick(View view) {
    }
}
