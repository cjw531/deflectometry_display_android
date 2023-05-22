package com.example.deflectometrydisplay.ui.checker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentCheckerBinding;
import com.example.deflectometrydisplay.ui.sinusoid.FringeActivity;
import com.example.deflectometrydisplay.ui.sinusoid.SinusoidPattern;

import java.io.ByteArrayOutputStream;

public class CheckerFragment extends Fragment {

    private FragmentCheckerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CheckerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(CheckerViewModel.class);

        binding = FragmentCheckerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText input_checker_pixel = binding.inputCheckerPixel;
        final EditText input_num_col = binding.inputNumCol;
        final EditText input_num_row = binding.inputNumRow;
        final Button button_generate = binding.buttonGenerateChecker;

        // Set a click listener for the button
        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the input values
                int checker_pixel = Integer.parseInt(input_checker_pixel.getText().toString());
                int num_col = Integer.parseInt(input_num_col.getText().toString());
                int num_row = Integer.parseInt(input_num_row.getText().toString());

                CheckerPattern checkerPattern = new CheckerPattern(requireContext(), checker_pixel, num_col, num_row);
                Bitmap pattern = checkerPattern.getPatterns();

                // Start the FullscreenActivity and pass the pattern as an extra
                Intent intent = new Intent(getActivity(), FringeActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pattern.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] patternBytes = stream.toByteArray();
                intent.putExtra("pattern", patternBytes);
                startActivity(intent);
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