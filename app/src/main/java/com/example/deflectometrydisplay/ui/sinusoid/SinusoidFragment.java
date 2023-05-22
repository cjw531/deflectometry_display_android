package com.example.deflectometrydisplay.ui.sinusoid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentSinusoidBinding;

import java.io.ByteArrayOutputStream;


public class SinusoidFragment extends Fragment {

    private FragmentSinusoidBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SinusoidViewModel sinusoidViewModel =
                new ViewModelProvider(this).get(SinusoidViewModel.class);

        binding = FragmentSinusoidBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText input_nph = binding.inputNph;
        final EditText input_max_freq = binding.inputMaxFrequency;
        final Button button_generate = binding.buttonGenerateFringe;

        // Set a click listener for the button
        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the input values
                String phaseShift = input_nph.getText().toString();
                String maxFrequency = input_max_freq.getText().toString();

                // For example, display a toast message with the input values
                String message = "Phase Shift: " + phaseShift + ", Max. Frequency: " + maxFrequency;
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                int nph = Integer.parseInt(input_nph.getText().toString());
                int max_freq = Integer.parseInt(input_max_freq.getText().toString());
                SinusoidPattern sinPattern = new SinusoidPattern(requireContext(), nph, max_freq);
                Bitmap[] patterns = sinPattern.getPatterns();

                // Start the FullscreenActivity and pass the pattern as an extra
                Intent intent = new Intent(getActivity(), FringeActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                patterns[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
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