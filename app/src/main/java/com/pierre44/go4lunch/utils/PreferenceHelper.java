package com.pierre44.go4lunch.utils;

import static android.content.Context.MODE_PRIVATE;
import static com.pierre44.go4lunch.utils.Constants.PREF_REMINDER;
import static com.pierre44.go4lunch.utils.Constants.SHARED_PREFS;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static SharedPreferences sharedPrefs;

    public static void initPreferenceHelper(Context context) {
        sharedPrefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        initReminderPreference();
    }

    private static void initReminderPreference() {
        if (!sharedPrefs.contains(PREF_REMINDER))
            sharedPrefs.edit().putBoolean(PREF_REMINDER, true).apply();
    }

    public static SharedPreferences getSharedPrefs(){
        return sharedPrefs;
    }


}
