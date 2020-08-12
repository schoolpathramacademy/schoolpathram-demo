package com.schoolpathram.schoolpathramdotcom.ui.help;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.schoolpathram.schoolpathramdotcom.R;
import com.schoolpathram.schoolpathramdotcom.ui.about.AboutViewModel;

public class HelpFragment extends Fragment {

    private HelpViewModel helpViewModel;

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        helpViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);

        View root = inflater.inflate(R.layout.help_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_help);
        helpViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}