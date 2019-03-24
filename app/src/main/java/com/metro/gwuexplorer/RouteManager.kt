package com.metro.gwuexplorer

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class RouteManager {

    private val okHttpClient: OkHttpClient
     var LineCode: String =""

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

    fun retrieveStationList(
        primaryKey: String,
        codeNext:String,
        successCallback: (List<String> , String) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {

        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode=C04&ToStationCode=$codeNext")
            .header("api_key", primaryKey)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val stationCode = mutableListOf<String>()
                val responseString = response.body()?.string()
                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Path")
                    if(statuses.length() != 0){                  
                        val now = statuses.getJSONObject(0)
                      LineCode = now.getString("LineCode")
                        
                    }
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val code:String = curr.getString("StationName")
                        stationCode.add(code)
                    }
                    successCallback(stationCode,LineCode)
                } else {
                    errorCallback(Exception("Search Entrances call failed"))
                }
            }
        })
    }
}
