package com.pierre44.go4lunch.manager;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.repository.WorkmateDataRepository;

/**
 * Created by pmeignen on 21/10/2021.
 */
public class WorkmateManager {

    private static volatile WorkmateManager instance;
    private WorkmateDataRepository userRepository;

    private WorkmateManager() {
        userRepository = WorkmateDataRepository.getInstance();
    }

    public static WorkmateManager getInstance() {
        WorkmateManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (WorkmateDataRepository.class) {
            if (instance == null) {
                instance = new WorkmateManager();
            }
            return instance;
        }
    }

    // repository getter
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // user logged boolean
    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    // user sign out
    public Task<Void> signOut(Context context) {
        return userRepository.signOut(context);
    }

    // create user
    public void createUser() {
        userRepository.createWorkmate();
    }

    public Task<Workmate> getUserData() {
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getWorkmateData().continueWith(task -> task.getResult().toObject(Workmate.class));
    }

    public Task<Void> updateUsername(String username) {
        return userRepository.updateWorkmateName(username);
    }

    public Task<Void> deleteUser(Context context) {
        // Delete the user account from the Auth
        return userRepository.deleteWorkmate(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            userRepository.deleteWorkmateFromFirestore();
        });
    }
}

