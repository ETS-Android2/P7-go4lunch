package com.pierre44.go4lunch.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierre44.go4lunch.models.Workmate;

/**
 * Created by pmeignen on 21/10/2021.
 */
public class WorkmateDataRepository {

    private CollectionReference workmateCollectionRef;

    private static final String COLLECTION_NAME = "workmates";
    private static final String WORKMATE_NAME_FIELD = "workmateName";

    private static volatile WorkmateDataRepository instance;

    public static WorkmateDataRepository getInstance() {
        WorkmateDataRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(WorkmateDataRepository.class) {
            if (instance == null) {
                instance = new WorkmateDataRepository();
            }
            return instance;
        }
    }

    @Nullable
    public FirebaseUser getCurrentWorkmate(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    private String getCurrentWorkmateUID() {
        FirebaseUser Workmate = getCurrentWorkmate();
        return (Workmate !=null)? Workmate.getUid() : null;
    }

    // sign out
    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    // Delete Workmate profile
    public Task<Void> deleteWorkmate(Context context){
        return AuthUI.getInstance().delete(context);
    }

    /************
     * FIRESTORE *
     ************/

    // Get the Collection Reference
    private CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create Workmate in Firestore
    public MutableLiveData<Workmate> createWorkmate() {
        MutableLiveData<Workmate> workmateMutableLiveData = new MutableLiveData<>();
        FirebaseUser workmate = getCurrentWorkmate();
        if(workmate != null){
            String uid = workmate.getUid();
            String workmateName = workmate.getDisplayName();
            String urlPicture = (workmate.getPhotoUrl() != null) ? workmate.getPhotoUrl().toString() : null;
            String workmateEmail = workmate.getEmail();
            Workmate WorkmateToCreate = new Workmate(uid, workmateName, urlPicture, workmateEmail);
        }
        return workmateMutableLiveData;
    }

    public MutableLiveData<Workmate> getWorkmate(String iD) {
        MutableLiveData<Workmate> workmateData = new MutableLiveData<>();
        workmateCollectionRef.document(iD).get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                if(task.getResult() != null)
                    workmateData.postValue(task.getResult().toObject(Workmate.class));
                else if (task.getException() != null)
                    Log.e(TAG,"get Workmate" + (task.getException().getMessage()));
        });
        return workmateData;
    }


    // Get Workmate Data from Firestore
    public Task<DocumentSnapshot> getWorkmateData(){
        String uid = this.getCurrentWorkmateUID();
        if(uid != null){
            return this.getWorkmatesCollection().document(uid).get();
        }else{
            return null;
        }
    }

    // Update WorkmateName from Firestore
    public Task<Void> updateWorkmateName(String WorkmateName) {
        String uid = this.getCurrentWorkmateUID();
        if(uid != null){
            return this.getWorkmatesCollection().document(uid).update(WORKMATE_NAME_FIELD, WorkmateName);
        }else{
            return null;
        }
    }

    // Delete the Workmate from Firestore
    public void deleteWorkmateFromFirestore() {
        String uid = this.getCurrentWorkmateUID();
        if(uid != null){
            this.getWorkmatesCollection().document(uid).delete();
        }
    }

}
