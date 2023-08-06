package com.example.deflectometrydisplay.ui.sinusoid;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SinusoidPattern {
    private Context context;
    private int[] resolution;
    private int nph;
    private int frequency;
    private Bitmap[] patterns;

    public SinusoidPattern(Context context, int nph, int frequency, int width, int height) {
        // Get screen resolution
        int resolution_0 = Math.min(width, height);
        int resolution_1 = Math.max(width, height);
        int[] resolution = {resolution_0, resolution_1};
        this.resolution = resolution;

        this.context = context;
        this.nph = nph;
        this.frequency = frequency;
        this.patterns = createSinusXY();
        saveBmpImagesInPictures();
    }

    public Bitmap[] getPatterns() {
        return patterns;
    }

    private Bitmap[] createSinusXY() {
        Bitmap[] patterns = new Bitmap[nph * 2];

        double[] sinX = new double[resolution[0]];
        double[] sinY = new double[resolution[1]];

        double period_x = (resolution[0] * frequency * 2.0) / resolution[1];
        double period_y = frequency * 2;

        for (int i = 0; i < nph; i++) {
            int k = i - 1;

            // Create sinusoidal pattern in Y direction
            for (int j = 0; j < resolution[1]; j++) {
                double y = j * (period_y * Math.PI) / (resolution[1] - 1);
                sinY[j] = 0.5 + 0.5 * Math.sin(y + 0.5 * k * Math.PI);
            }

            int[][] bitmapArray = new int[resolution[1]][resolution[0]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    int value = (int) Math.round(sinY[j] * 255); // for proper rounding
                    int pixelValue = value << 16 | value << 8 | value | 0xFF000000;
                    bitmapArray[j][l] = pixelValue;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(resolution[0], resolution[1], Bitmap.Config.ARGB_8888);
            int[] pixels = new int[resolution[0] * resolution[1]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    pixels[j * resolution[0] + l] = bitmapArray[j][l];
                }
            }
            bitmap.setPixels(pixels, 0, resolution[0], 0, 0, resolution[0], resolution[1]);

            // Rotate the bitmap
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            patterns[i] = rotatedBitmap;

            // Create sinusoidal pattern in X direction
            for (int j = 0; j < resolution[0]; j++) {
                double x = j * (period_x * Math.PI) / (resolution[0] - 1);
                sinX[j] = 0.5 + 0.5 * Math.sin(x + 0.5 * k * Math.PI);
            }

            bitmapArray = new int[resolution[1]][resolution[0]];
            for (int j = 0; j < resolution[1]; j++) {
                for (int l = 0; l < resolution[0]; l++) {
                    int value = (int) Math.round(sinX[l] * 255);; // proper rounding
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

            patterns[i + 4] = rotatedBitmap;

        }

        return patterns;
    }

    // Function to save BMP image files in Pictures/Deflectometry using custom BMP format
    private void saveBmpImagesInPictures() {
        // Create the Pictures/Deflectometry directory if it doesn't exist
        File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Deflectometry/Frequency_" + String.valueOf(frequency));
        picturesDir.mkdirs();

        // Save the BMP images to Pictures/Deflectometry using PNG format
        for (int i = 0; i < patterns.length; i++) {
            Bitmap bitmap = patterns[i];
            File pngFile = new File(picturesDir, "pattern_" + String.valueOf(i) + ".png");

            try {
                FileOutputStream fos = new FileOutputStream(pngFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add the image to the MediaStore to make it visible in the Gallery app
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, pngFile.getName());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, pngFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.DATA, pngFile.getAbsolutePath());
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

}

