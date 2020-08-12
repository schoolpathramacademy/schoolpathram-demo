package com.schoolpathram.schoolpathramdotcom.ui.office;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OfficeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public OfficeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is class fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}