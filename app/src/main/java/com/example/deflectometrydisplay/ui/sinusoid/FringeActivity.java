package com.example.deflectometrydisplay.ui.sinusoid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.deflectometrydisplay.databinding.ActivityFringeBinding;

public class FringeActivity extends Activity {

    private ActivityFringeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFringeBinding.inflate(getLayoutInflater());
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
        Intent intent = getIntent();
        byte[][] patternBytesArray = (byte[][]) intent.getSerializableExtra("patterns");
        Integer exposureTime = (Integer) intent.getSerializableExtra("time");

        // Create an array of Bitmaps from the pattern byte arrays
        Bitmap[] patterns = new Bitmap[patternBytesArray.length];
        for (int i = 0; i < patternBytesArray.length; i++) {
            byte[] patternBytes = patternBytesArray[i];
            patterns[i] = BitmapFactory.decodeByteArray(patternBytes, 0, patternBytes.length);
        }

        // Set the pattern image to the ImageView
        ImageView imageView = binding.fringeView;
        imageView.setImageBitmap(patterns[0]);

        // Add a view on top of the ImageView to block the notch area with a black line
        View blackLineView = new View(this);
        blackLineView.setBackgroundColor(Color.BLACK);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight() // Get the height of the status bar (notch area)
        );
        binding.frameLayout.addView(blackLineView, layoutParams);

        // Find the ViewFlipper
        ViewFlipper imageFlipper = binding.imageFlipper;
        imageFlipper.removeAllViews(); // Clear any existing child views in the ViewFlipper

        // Add image views to the flipper
        for (Bitmap pattern : patterns) {
            ImageView image = new ImageView(getApplicationContext());
            image.setImageBitmap(pattern);
            imageFlipper.addView(image);
        }

        // Set an in-animation for the ViewFlipper
        Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        imageFlipper.setInAnimation(inAnimation);

        // Set the flipper properties and start
        imageFlipper.setFlipInterval(exposureTime); // exposure time
        // Add an AnimationListener to stop flipping after initial display
        imageFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int displayedChild = imageFlipper.getDisplayedChild();
                int childCount = imageFlipper.getChildCount();

                if (displayedChild == childCount - 1) {
                    imageFlipper.stopFlipping();
                    // Pause for exposure time seconds before finishing the activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, exposureTime);
                }
            }
        });
        imageFlipper.startFlipping();
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
