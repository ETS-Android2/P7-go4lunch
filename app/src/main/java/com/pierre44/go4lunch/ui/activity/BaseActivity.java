package com.pierre44.go4lunch.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * Created by pmeignen on 21/10/2021.
 *  Base Activity class that allow to manage all the common code for the activities
 *  @param <T> Should be the type of the viewBinding of your activity see more <a href="https://developer.android.com/topic/libraries/view-binding"> here </a>
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    public abstract T getViewBinding();
    protected T binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
    }

    /**
     * Initialise the binding object and the layout of the activity
     */
    private void initBinding(){
        binding = getViewBinding();
        View view = binding.getRoot();
        setContentView(view);
    }
}
