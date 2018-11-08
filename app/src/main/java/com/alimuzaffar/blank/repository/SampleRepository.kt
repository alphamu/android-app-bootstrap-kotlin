package com.alimuzaffar.blank.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.alimuzaffar.blank.App
import com.alimuzaffar.blank.database.dao.SampleDao
import com.alimuzaffar.blank.database.entity.Sample
import com.alimuzaffar.blank.net.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRepository @Inject
constructor(private val apiInterface: ApiInterface, private val sampleDao: SampleDao, private val executor: ExecutorService) {

    // ---

    fun getUser(userLogin: String): LiveData<Sample> {
        refreshUser(userLogin) // try to refresh data if possible from Github Api
        return sampleDao.load(userLogin) // return a LiveData directly from the database.
    }

    // ---

    private fun refreshUser(userLogin: String) {
        executor.execute {
            // Check if user was fetched recently
            sampleDao.hasUser(userLogin, getMaxRefreshTime(Date()))
            val userExists = true
            // If user have to be updated
            if (!userExists) {
                apiInterface.getUser(userLogin).enqueue(object : Callback<Sample> {
                    override fun onResponse(call: Call<Sample>, response: Response<Sample>) {
                        Log.e("TAG", "DATA REFRESHED FROM NETWORK")
                        Toast.makeText(App.context, "Data refreshed from network !", Toast.LENGTH_LONG).show()
                        executor.execute {
                            val user = response.body()
                            user!!.lastRefresh = Date()
                            sampleDao.save(user)
                        }
                    }

                    override fun onFailure(call: Call<Sample>, t: Throwable) {}
                })
            }
        }
    }

    // ---

    private fun getMaxRefreshTime(currentDate: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES)
        return cal.time
    }

    companion object {

        private val FRESH_TIMEOUT_IN_MINUTES = 1
    }
}
