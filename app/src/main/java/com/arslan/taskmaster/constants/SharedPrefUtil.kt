package com.arslan.taskmaster.constants

import android.content.Context

/**
 * Created by Fahad Butt .
 */

class SharedPrefUtil {

    fun getSharedPrefValue(mContext: Context, sharedPrefTitle: String): String? {
        val settings = mContext.getSharedPreferences(PREFS_NAME, 0)
        return settings.getString(sharedPrefTitle, null)
    }

    fun saveSharedPrefValue(mContext: Context, sharedPrefTitle: String,
                            sharedPrefValue: String) {
        try {
            val settings = mContext.getSharedPreferences(PREFS_NAME, 0)
            val editor = settings.edit()
            editor.putString(sharedPrefTitle, sharedPrefValue)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeSharedPrefValue(mContext: Context, sharedPrefTitle: String) {
        try {
            val settings = mContext.getSharedPreferences(
                    PREFS_NAME, 0)
            val editor = settings.edit()
            editor.remove(sharedPrefTitle)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        internal var PREFS_NAME = "TaskMaster"
    }
}
