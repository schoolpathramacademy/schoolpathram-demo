package com.schoolpathram.schoolpathramdotcom.ui.resource;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResourceViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ResourceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is class fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}