package com.pierre44.go4lunch.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.R;
import com.pierre44.go4lunch.di.Injection;
import com.pierre44.go4lunch.di.ViewModelFactory;

/**
 * Created by pmeignen on 21/10/2021.
 *  Base Activity class that allow to manage all the common code for the activities
 *  @param <T> Should be the type of the viewBinding of your activity see more <a href="https://developer.android.com/topic/libraries/view-binding"> here </a>
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    public T viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        setContentView(this.getLayout());
    }

    protected void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        viewModel = (T) new ViewModelProvider(this,viewModelFactory).get(getViewModelClass());
    }

    protected abstract Class getViewModelClass();

    protected abstract View getLayout();

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public T getViewModel() {
        return viewModel;
    }

    // --------------------
    // ERROR
    // --------------------
    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }
}