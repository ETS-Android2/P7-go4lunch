package com.pierre44.go4lunch.ui.listView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListRestaurantViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListRestaurantViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is list view fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}