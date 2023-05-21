package com.example.deflectometrydisplay.ui.checker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentCheckerBinding;

public class CheckerFragment extends Fragment {

    private FragmentCheckerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CheckerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(CheckerViewModel.class);

        binding = FragmentCheckerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textChecker;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}