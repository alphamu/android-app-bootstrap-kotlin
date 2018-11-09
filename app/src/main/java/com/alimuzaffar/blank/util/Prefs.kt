package com.alimuzaffar.blank.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * A Singleton for managing your SharedPreferences.
 *
 *
 * You should make sure to change the SETTINGS_NAME to what you want
 * and choose the operating made that suits your needs, the default is
 * MODE_PRIVATE.
 *
 *
 * IMPORTANT: The class is not thread safe. It should work fine in most
 * circumstances since the write and read operations are fast. However
 * if you call edit for bulk updates and do not commit your changes
 * there is a possibility of data loss if a background thread has modified
 * preferences at the same time.
 *
 *
 * Usage:
 *
 *
 * int sampleInt = Prefs.getInstance(context).getInt(Key.SAMPLE_INT);
 * Prefs.getInstance(context).set(Key.SAMPLE_INT, sampleInt);
 *
 *
 * If Prefs.getInstance(Context) has been called once, you can
 * simple use Prefs.getInstance() to save some precious line space.
 *
 * @author Ali Muzaffar
 */
class Prefs(context: Context) {
    private val mPref: SharedPreferences

    // === EASY ACCESS METHODS

    var accessToken: String?
        get() = getSecureString(Key.ACCESS_TOKEN, null)
        set(token) = setSecureString(Key.ACCESS_TOKEN, token)

    /**
     * Class for keeping all the keys used for shared preferences in one place.
     */
    object Key {
        /* Recommended naming convention:
         * ints, floats, doubles, longs:
         * SAMPLE_NUM or SAMPLE_COUNT or SAMPLE_INT, SAMPLE_LONG etc.
         *
         * boolean: IS_SAMPLE, HAS_SAMPLE, CONTAINS_SAMPLE
         *
         * String: SAMPLE_KEY, SAMPLE_STR or just SAMPLE
         */
        val ACCESS_TOKEN = "ACCESS_TOKEN"

    }

    init {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
        try {
            //This will only create a certificate once as it checks
            //internally whether a certificate with the given name
            //already exists.
            KeyStoreHelper.createKeys(context, KEYSTORE_ALIAS)
        } catch (e: Exception) {
            //Probably will never happen.
            throw RuntimeException(e)
        }

    }

    fun put(key: String, value: String) {
        mPref.edit { putString(key, value) }
    }

    fun put(key: String, value: Int) {
        mPref.edit { putInt(key, value) }
    }

    fun put(key: String, value: Boolean) {
        mPref.edit { putBoolean(key, value) }
    }

    fun put(key: String, value: Float) {
        mPref.edit { putFloat(key, value) }
    }

    /**
     * Convenience method for storing doubles.
     *
     *
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to store.
     * @param value The new value for the preference.
     */
    fun put(key: String, value: Double) {
        mPref.edit { putString(key, value.toString()) }
    }

    fun put(key: String, value: Long) {
        mPref.edit { putLong(key, value) }
    }

    fun getInt(key: String): Int {
        return mPref.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return mPref.getInt(key, defaultValue)
    }

    fun getLong(key: String): Long {
        return mPref.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return mPref.getLong(key, defaultValue)
    }

    fun getFloat(key: String): Float {
        return mPref.getFloat(key, 0f)
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        return mPref.getFloat(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mPref.getBoolean(key, defaultValue)
    }

    fun getBoolean(key: String): Boolean {
        return mPref.getBoolean(key, false)
    }

    fun getString(key: String, defaultValue: String): String? {
        return mPref.getString(key, defaultValue)
    }

    fun getString(key: String): String? {
        return mPref.getString(key, null)
    }

    /**
     * Convenience method for retrieving doubles.
     *
     *
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    fun getDouble(key: String): Double? {
        return getDouble(key, 0.0)
    }

    /**
     * Convenience method for retrieving doubles.
     *
     *
     * There may be instances where the accuracy of a double is desired.
     * SharedPreferences does not handle doubles so they have to
     * cast to and from String.
     *
     * @param key The name of the preference to fetch.
     */
    fun getDouble(key: String, defaultValue: Double): Double? {
        return try {
            mPref.getString(key, defaultValue.toString())?.toDouble() ?: defaultValue
        } catch (nfe: NumberFormatException) {
            defaultValue
        }

    }

    private fun setSecureString(key: String, value: String?) {
        val hashKey = KeyStoreHelper.sha256(key)
        if (value.isNullOrEmpty()) {
            mPref.edit { remove(hashKey) }
        } else {
            val encVal = KeyStoreHelper.encrypt(KEYSTORE_ALIAS, value)
            mPref.edit { putString(hashKey, encVal) }
        }

    }

    private fun getSecureString(key: String, defaultValue: String?): String? {
        val hashKey = KeyStoreHelper.sha256(key)
        val encVal = mPref.getString(hashKey, defaultValue)
        try {
            return KeyStoreHelper.decrypt(KEYSTORE_ALIAS, encVal)
        } catch (e: RuntimeException) {
            return defaultValue
        }

    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The name of the key(s) to be removed.
     */
    fun remove(vararg keys: String) {
        mPref.edit {
            for (key in keys) {
                remove(key)
            }
        }
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    fun clear() {
        mPref.edit {
            clear()
        }
    }

    companion object {
        private val SETTINGS_NAME = "blank_settings"
        private val KEYSTORE_ALIAS = "blank-keystore-alias"
    }
}