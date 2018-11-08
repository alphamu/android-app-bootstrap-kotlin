package com.alimuzaffar.blank.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * A Singleton for managing your SharedPreferences.
 * <p>
 * You should make sure to change the SETTINGS_NAME to what you want
 * and choose the operating made that suits your needs, the default is
 * MODE_PRIVATE.
 * <p>
 * IMPORTANT: The class is not thread safe. It should work fine in most
 * circumstances since the write and read operations are fast. However
 * if you call edit for bulk updates and do not commit your changes
 * there is a possibility of data loss if a background thread has modified
 * preferences at the same time.
 * <p>
 * Usage:
 * <p>
 * int sampleInt = Prefs.getInstance(context).getInt(Key.SAMPLE_INT);
 * Prefs.getInstance(context).set(Key.SAMPLE_INT, sampleInt);
 * <p>
 * If Prefs.getInstance(Context) has been called once, you can
 * simple use Prefs.getInstance() to save some precious line space.
 *
 * @author Ali Muzaffar
 */
public class Prefs {
    private static final String SETTINGS_NAME = "blank_settings";
    private static final String KEYSTORE_ALIAS = "blank-keystore-alias";
    private SharedPreferences mPref;

    /**
     * Class for keeping all the keys used for shared preferences in one place.
     */
    public static class Key {
        /* Recommended naming convention:
         * ints, floats, doubles, longs:
         * SAMPLE_NUM or SAMPLE_COUNT or SAMPLE_INT, SAMPLE_LONG etc.
         *
         * boolean: IS_SAMPLE, HAS_SAMPLE, CONTAINS_SAMPLE
         *
         * String: SAMPLE_KEY, SAMPLE_STR or just SAMPLE
         */
        public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    }

    public Prefs(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        try {
            //This will only create a certificate once as it checks
            //internally whether a certificate with the given name
            //already exists.
            KeyStoreHelper.createKeys(context, KEYSTORE_ALIAS);
        } catch (Exception e) {
            //Probably will never happen.
            throw new RuntimeException(e);
        }
    }

    public void put(String key, String val) {
        doEdit((editor) -> editor.putString(key, val));
    }

    public void put(String key, int val) {
        doEdit((editor) -> editor.putInt(key, val));
    }

    public void put(String key, boolean val) {
        doEdit((editor) -> editor.putBoolean(key, val));
    }

    public void put(String key, float val) {
        doEdit((editor) -> editor.putFloat(key, val));
    }

    /**
     * Convenience method for storing doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to store.
     * @param val The new value for the preference.
     */
    public void put(String key, double val) {
        doEdit((editor) -> editor.putString(key, String.valueOf(val)));
    }

    public void put(String key, long val) {
        doEdit((editor) -> editor.putLong(key, val));
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public float getFloat(String key) {
        return mPref.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Convenience method for retrieving doubles.
     * <p>
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    @SuppressWarnings("ConstantConditions")
    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    private void setSecureString(String key, String val) {
        String hashKey = KeyStoreHelper.sha256(key);
        if (TextUtils.isEmpty(val)) {
            mPref.edit().remove(hashKey).apply();
        } else {
            String encVal = KeyStoreHelper.encrypt(KEYSTORE_ALIAS, val);
            doEdit((editor) -> editor.putString(hashKey, encVal));
        }

    }

    private String getSecureString(String key, String defaultValue) {
        String hashKey = KeyStoreHelper.sha256(key);
        String encVal = mPref.getString(hashKey, defaultValue);
        try {
            return KeyStoreHelper.decrypt(KEYSTORE_ALIAS, encVal);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The name of the key(s) to be removed.
     */
    public void remove(String... keys) {
        doEdit((editor) -> {
            for (String key : keys) {
                editor.remove(key);
            }
        });
    }

    // === EASY ACCESS METHODS

    public String getAccessToken() {
        return getSecureString(Key.ACCESS_TOKEN, null);
    }

    public void setAccessToken(String token) {
        setSecureString(Key.ACCESS_TOKEN, token);
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit(SharedPreferences.Editor::clear);
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit(CommitRunnable r) {
        SharedPreferences.Editor editor = mPref.edit();
        r.run(editor);
        editor.apply();
    }

    @FunctionalInterface
    private interface CommitRunnable {
        void run(SharedPreferences.Editor editor);
    }
}