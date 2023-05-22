package com.example.deflectometrydisplay.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView text_notch = binding.textNotch;
        homeViewModel.hasNotch(requireContext()).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String has_notch) {
                String notch_information = "Notch Exists (Y/N): " + has_notch;
                text_notch.setText(notch_information);
            }
        });

        final TextView text_notch_dimension = binding.textNotchSize;
        homeViewModel.getNotchHeight(requireContext()).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer height) {
//                int screenWidth = dimensions[0];
//                int screenHeight = dimensions[1];

                String notch_dimensions = "Notch Height: " + height;
                text_notch_dimension.setText(notch_dimensions);
            }
        });

        final TextView text_dimension = binding.textDimension;
        homeViewModel.get_pixel_dimensions(requireContext()).observe(getViewLifecycleOwner(), new Observer<int[]>() {
            @Override
            public void onChanged(int[] dimensions) {
                int screenWidth = dimensions[0];
                int screenHeight = dimensions[1];

                String fullScreenDimensions = "Util. Screen: " + screenWidth + " x " + screenHeight;
                text_dimension.setText(fullScreenDimensions);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}