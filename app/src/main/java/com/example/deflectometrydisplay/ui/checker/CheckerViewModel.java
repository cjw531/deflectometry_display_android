package com.example.deflectometrydisplay.ui.checker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CheckerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is checker fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}