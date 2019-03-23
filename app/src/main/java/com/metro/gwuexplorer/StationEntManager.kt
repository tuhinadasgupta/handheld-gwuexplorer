package com.metro.gwuexplorer

import android.location.Address
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class StationEntManager {

    private val okHttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }

    fun retrieveNearbyStation(
        primaryKey: String,
        address: Address,
        successCallback: (List<String>, List<String>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        val lat = address.latitude
        val lon = address.longitude
        val radius = "500"
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStationEntrances?Lat=$lat&Lon=$lon&Radius=$radius")
            .header("api_key", primaryKey)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val ent = mutableListOf<String>()
                val stationCode = mutableListOf<String>()
                val responseString = response.body()?.string()
                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Entrances")
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val text = curr.getString("Name")
                        val code:String = curr.getString("StationCode1")
                        stationCode.add(code)
                        ent.add(text)
                    }
                    successCallback(ent,stationCode)
                } else {
                    errorCallback(Exception("Search Entrances call failed"))
                }
            }
        })
    }

}
