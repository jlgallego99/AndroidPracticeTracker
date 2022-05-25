package com.example.androidpracticetracker.ui.practica;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PracticaViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PracticaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Practica fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}