package com.example.deflectometrydisplay.ui.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<int[]> get_pixel_dimensions(Context context) {
        MutableLiveData<int[]> pixelDimensionsLiveData = new MutableLiveData<>();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        int[] dimensions = {width, height};
        pixelDimensionsLiveData.setValue(dimensions);

        return pixelDimensionsLiveData;
    }

    public LiveData<String> hasNotch(Context context) {
        MutableLiveData<String> has_notch = new MutableLiveData<>();
        has_notch.setValue("N");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayCutout displayCutout = windowManager.getDefaultDisplay().getCutout();
                if (displayCutout != null && displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0) {
                    has_notch.setValue("Y");
                }
            }
        } else {
            // TODO: currently null implementation
            // Check for other methods to detect notches on older Android versions
        }

        return has_notch;
    }

    public LiveData<Integer> getNotchHeight(Context context) {
        MutableLiveData<Integer> notchHeightLiveData = new MutableLiveData<>();
        notchHeightLiveData.setValue(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                DisplayCutout displayCutout = display.getCutout();
                if (displayCutout != null && displayCutout.getBoundingRects() != null && displayCutout.getBoundingRects().size() > 0) {
                    List<Rect> boundingRects = displayCutout.getBoundingRects();
                    // Assuming a single notch, retrieve the dimensions of the first bounding rectangle
                    Rect notchRect = boundingRects.get(0);
                    int notchHeight = notchRect.height();
                    notchHeightLiveData.setValue(notchHeight);
                }
            }
        }

        return notchHeightLiveData;
    }


    public LiveData<String> getText() {
        return mText;
    }
}