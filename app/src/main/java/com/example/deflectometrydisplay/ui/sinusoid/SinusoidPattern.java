package com.example.deflectometrydisplay.ui.sinusoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class SinusoidPattern {
    private int[] resolution;
    private int nph;
    private int frequency;
    private Bitmap[] patterns;

    public SinusoidPattern(Context context, int nph, int frequency) {
        // Get screen resolution
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int resolution_0 = Math.min(screenWidth, screenHeight);
        int resolution_1 = Math.max(screenWidth, screenHeight);
        int[] resolution = {resolution_0, resolution_1};
        this.resolution = resolution;

        this.nph = nph;
        this.frequency = frequency;
        this.patterns = createSinusXY();
    }

    public Bitmap[] getPatterns() {
        return patterns;
    }

    private Bitmap[] createSinusXY() {
        Bitmap[] patterns = new Bitmap[nph * 2];
        int patternSize = Math.min(resolution[0], resolution[1]);

        for (int i = 0; i < nph; i++) {
            int k = i - 1;
            double period = frequency * 2 * Math.PI;

            // Create sinusoidal pattern in X direction
            double[] sinX = new double[patternSize];
            for (int j = 0; j < patternSize; j++) {
                double value = 0.5 + 0.5 * Math.sin(j * period / patternSize + k * Math.PI);
                sinX[j] = value;
            }
            Bitmap imgX = createPatternImage(sinX, resolution[1], patternSize);
            patterns[i] = imgX;

            // Create sinusoidal pattern in Y direction
            double[] sinY = new double[patternSize];
            period = (patternSize * frequency * 2 * Math.PI) / patternSize;
            for (int j = 0; j < patternSize; j++) {
                double value = 0.5 + 0.5 * Math.sin(j * period / patternSize + k * Math.PI);
                sinY[j] = value;
            }
            Bitmap imgY = createPatternImage(sinY, resolution[0], patternSize);
            patterns[nph + i] = imgY;
        }

        return patterns;
    }

    private Bitmap createPatternImage(double[] values, int height, int width) {
        Bitmap pattern = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = (int) (values[j] * 255);
                pattern.setPixel(i, width - 1 - j, Color.rgb(color, color, color));
            }
        }
        return pattern;
    }

}

