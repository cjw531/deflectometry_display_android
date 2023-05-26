package com.example.deflectometrydisplay.ui.checker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.deflectometrydisplay.databinding.ActivityCheckerDisplayBinding;

public class CheckerDisplayActivity extends Activity  {

    private ActivityCheckerDisplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCheckerDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the notch design to LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Get the Window object
        Window window = getWindow();

        // Set the flags to make the activity full screen
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Make the activity full screen and hide system bars
        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        insetsController.hide(WindowInsetsCompat.Type.systemBars());
        insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        // Retrieve the pattern byte array from the intent extras
        byte[] patternBytes = getIntent().getByteArrayExtra("pattern");
        Bitmap pattern = BitmapFactory.decodeByteArray(patternBytes, 0, patternBytes.length);

        // Set the pattern image to the ImageView
        ImageView imageView = binding.checkerView;
        imageView.setImageBitmap(pattern);

        // Add a view on top of the ImageView to block the notch area with a black line
        View blackLineView = new View(this);
        blackLineView.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight() // Get the height of the status bar (notch area)
        );
        binding.frameLayout.addView(blackLineView, layoutParams);
    }

    // Helper method to get the height of the status bar (notch area)
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
