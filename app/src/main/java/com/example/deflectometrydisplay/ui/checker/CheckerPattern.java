package com.example.deflectometrydisplay.ui.checker;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CheckerPattern {
    private Context context;
    private int[] resolution;
    private int checker_pixel;
    private int num_col;
    private int num_row;
    private Bitmap pattern;

    public CheckerPattern(Context context, int checker_pixel, int num_col, int num_row, int screenWidth, int screenHeight) {
        int resolution_0 = Math.min(screenWidth, screenHeight);
        int resolution_1 = Math.max(screenWidth, screenHeight);
        int[] resolution = {resolution_0, resolution_1};
        this.resolution = resolution;

        this.context = context;
        this.checker_pixel = checker_pixel;
        this.num_col = num_col;
        this.num_row = num_row;

        this.pattern = generateChessboard(resolution_0, resolution_1, this.checker_pixel, this.num_col, this.num_row);
        saveBmpImagesInPictures();
    }

    public Bitmap getPatterns() {
        return pattern;
    }

    public Bitmap generateChessboard(int width, int height, int checkerPixel, int numCol, int numRow) {
        // Exception handling (invalid inputs)
        if (checkerPixel > Math.min(width, height)) {
            throw new IllegalArgumentException("Single checker pixel dimension is larger than the width/height of the monitor!");
        } else if ((numCol * checkerPixel) > height) {
            throw new IllegalArgumentException("Number of columns exceeds the board dimensions!");
        } else if ((numRow * checkerPixel) > width) {
            throw new IllegalArgumentException("Number of rows exceeds the board dimensions!");
        }

        int checkerWidth = numCol * checkerPixel;
        int checkerHeight = numRow * checkerPixel;

        Bitmap chessboard = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(chessboard);

        // Fill the entire canvas with white color
        canvas.drawColor(Color.WHITE);

        // Calculate the margin sizes
        int marginX = (width - checkerHeight) / 2;
        int marginY = (height - checkerWidth) / 2;

        // Create paint objects for black and white squares
        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        Paint whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        boolean isBlack = false;
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                int x = marginX + (i * checkerPixel);
                int y = marginY + (j * checkerPixel);

                // Draw the black or white square
                Paint paint = isBlack ? blackPaint : whitePaint;
                canvas.drawRect(x, y, x + checkerPixel, y + checkerPixel, paint);

                isBlack = !isBlack;
            }
            isBlack = !isBlack;
        }

        // Rotate the chessboard pattern by 90 degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedChessboard = Bitmap.createBitmap(chessboard, 0, 0, chessboard.getWidth(), chessboard.getHeight(), matrix, true);

        return rotatedChessboard;
    }

    // Function to save BMP image files in Pictures/Deflectometry using custom BMP format
    private void saveBmpImagesInPictures() {
        // Create the Pictures/Deflectometry directory if it doesn't exist
        File picturesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Deflectometry/Chessboard");
        picturesDir.mkdirs();

        // Save the BMP images to Pictures/Deflectometry using PNG format
        File pngFile = new File(picturesDir, "chess_col_" + String.valueOf(num_col) + "_row_" + String.valueOf(num_row) + "_pix_" + String.valueOf(checker_pixel) + ".png");
        Bitmap bitmap = pattern;

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
