package com.example.deflectometrydisplay.ui.sinusoid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SinusoidViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SinusoidViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sinusoid fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}