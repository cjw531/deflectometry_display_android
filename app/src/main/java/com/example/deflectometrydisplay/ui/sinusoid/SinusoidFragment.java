package com.example.deflectometrydisplay.ui.sinusoid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.deflectometrydisplay.databinding.FragmentSinusoidBinding;

import java.io.ByteArrayOutputStream;


public class SinusoidFragment extends Fragment {

    private FragmentSinusoidBinding binding;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SinusoidViewModel sinusoidViewModel =
                new ViewModelProvider(this).get(SinusoidViewModel.class);

        binding = FragmentSinusoidBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get the screen dimensions and update the LiveData
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        final EditText input_nph = binding.inputNph;
        final EditText input_max_freq = binding.inputMaxFrequency;
        final EditText input_exposure_time = binding.inputExposureTime;
        final Button button_generate = binding.buttonGenerateFringe;

        // Set a click listener for the button
        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the input values
                String phaseShift = input_nph.getText().toString();
                String maxFrequency = input_max_freq.getText().toString();
                String exposureTimeString = input_exposure_time.getText().toString(); // string first
                Integer exposureTime = Integer.parseInt(exposureTimeString); // convert into double

                // For example, display a toast message with the input values
                String message = "Phase Shift: " + phaseShift + ", Max. Frequency: " + maxFrequency;
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                int nph = Integer.parseInt(input_nph.getText().toString());
                int max_freq = Integer.parseInt(input_max_freq.getText().toString());

                // Declare the sinPattern variable outside the if block
                SinusoidPattern sinPattern = null;

                // Check if the WRITE_EXTERNAL_STORAGE permission is granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, you can call the method
                    sinPattern = new SinusoidPattern(requireContext(), nph, max_freq, screenWidth, screenHeight);
                    // Use the sinPattern object as needed
                } else {
                    // Request the permission if not granted
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                }

                // Check if sinPattern is not null before using it
                if (sinPattern != null) {
                    Bitmap[] patterns = sinPattern.getPatterns();

                    // Start the FullscreenActivity and pass the patterns as an extra
                    Intent intent = new Intent(getActivity(), FringeActivity.class);
                    ByteArrayOutputStream[] streamArray = new ByteArrayOutputStream[patterns.length];
                    byte[][] patternBytesArray = new byte[patterns.length][];

                    for (int i = 0; i < patterns.length; i++) {
                        streamArray[i] = new ByteArrayOutputStream();
                        patterns[i].compress(Bitmap.CompressFormat.PNG, 100, streamArray[i]);
                        patternBytesArray[i] = streamArray[i].toByteArray();
                    }

                    intent.putExtra("patterns", patternBytesArray); // pass fringe pattern array
                    intent.putExtra("time", exposureTime); // pass exposure time
                    startActivity(intent);
                }
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