package com.example.deflectometrydisplay.ui.sinusoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

        double[] sinX = new double[resolution[0]];
        double[] sinY = new double[resolution[1]];

        double period_x = frequency * 2;
        double period_y = (resolution[1] * frequency * 2) / resolution[0];

        for (int i = 0; i < nph; i++) {
            int k = i - 1;

            // Create sinusoidal pattern in X direction
            for (int j = 0; j < resolution[0]; j++) {
                double x = j * (period_x * Math.PI) / (resolution[0] - 1);
                sinX[j] = 0.5 + 0.5 * Math.sin(x + 0.5 * k * Math.PI);
            }

            int[][] bitmapArray = new int[resolution[1]][resolution[0]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    int value = (int) (sinX[l] * 255);
                    int pixelValue = value << 16 | value << 8 | value | 0xFF000000;
                    bitmapArray[j][l] = pixelValue;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(resolution[0], resolution[1], Bitmap.Config.ARGB_8888);
            int[] pixels = new int[resolution[0] * resolution[1]];
            for (int j = 0; j < resolution[1]; j++) {
                System.arraycopy(bitmapArray[j], 0, pixels, j * resolution[0], resolution[0]);
            }
            bitmap.setPixels(pixels, 0, resolution[0], 0, 0, resolution[0], resolution[1]);

            // Rotate the bitmap
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            patterns[i] = rotatedBitmap;

            // Create sinusoidal pattern in Y direction
            for (int j = 0; j < resolution[1]; j++) {
                double y = j * (period_y * Math.PI) / (resolution[1] - 1);
                sinY[j] = 0.5 + 0.5 * Math.sin(y + 0.5 * k * Math.PI);
            }

            bitmapArray = new int[resolution[1]][resolution[0]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    int value = (int) (sinY[j] * 255);
                    int pixelValue = value << 16 | value << 8 | value | 0xFF000000;
                    bitmapArray[j][l] = pixelValue;
                }
            }

            bitmap = Bitmap.createBitmap(resolution[0], resolution[1], Bitmap.Config.ARGB_8888);
            pixels = new int[resolution[0] * resolution[1]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    pixels[j * resolution[0] + l] = bitmapArray[j][l];
                }
            }
            bitmap.setPixels(pixels, 0, resolution[0], 0, 0, resolution[0], resolution[1]);

            // Rotate the bitmap
            matrix = new Matrix();
            matrix.postRotate(90);
            rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            patterns[nph + i] = rotatedBitmap;
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

