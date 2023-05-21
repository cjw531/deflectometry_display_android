package com.example.deflectometrydisplay.ui.sinusoid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentSinusoidBinding;

public class SinusoidFragment extends Fragment {

    private FragmentSinusoidBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SinusoidViewModel sinusoidViewModel =
                new ViewModelProvider(this).get(SinusoidViewModel.class);

        binding = FragmentSinusoidBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSinusoid;
        sinusoidViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}