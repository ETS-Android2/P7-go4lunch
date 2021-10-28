package com.pierre44.go4lunch.ui.activity.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkmatesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WorkmatesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is workmate fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}