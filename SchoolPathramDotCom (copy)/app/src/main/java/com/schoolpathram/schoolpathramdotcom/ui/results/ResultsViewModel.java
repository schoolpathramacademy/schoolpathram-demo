package com.schoolpathram.schoolpathramdotcom.ui.results;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResultsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ResultsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is class fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}