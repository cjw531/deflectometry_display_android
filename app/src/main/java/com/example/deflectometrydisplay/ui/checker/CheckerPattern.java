package com.example.deflectometrydisplay.ui.checker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class CheckerPattern {
    private int[] resolution;
    private int checker_pixel;
    private int num_col;
    private int num_row;
    private Bitmap pattern;

    public CheckerPattern(Context context, int checker_pixel, int num_col, int num_row) {
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

        this.checker_pixel = checker_pixel;
        this.num_col = num_col;
        this.num_row = num_row;

        this.pattern = generateChessboard(resolution_0, resolution_1, this.checker_pixel, this.num_col, this.num_row);
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



}
