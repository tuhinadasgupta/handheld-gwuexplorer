package com.metro.gwuexplorer

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class AlertManager {
    private val okHttpClient: OkHttpClient
  //setup of okhttp
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
    
    fun retrieveAlert(
        successCallback: (List<Alert>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        val primaryKey = "507c1527b662401aaee3f16396982ccc"
        val request = Request.Builder()
            .url("https://api.wmata.com/Incidents.svc/json/Incidents")
            .header("api_key", primaryKey)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val alerts = mutableListOf<Alert>()
                val responseString = response.body()?.string()
                //get tweets using rail incidents api returning a json object
                if (response.isSuccessful && responseString != null) {
                       val incidents = JSONObject(responseString).getJSONArray("Incidents")
                       for (i in 0 until incidents.length()) {
                        val curr = incidents.getJSONObject(i)
                        val text = curr.getString("Description")
                        alerts.add(
                            Alert(
                                description = text
                            )
                        )
                    }
                    successCallback(alerts)

                } else {
                    errorCallback(Exception("Search alerts call failed"))

                }
            }
        })
    }
}
