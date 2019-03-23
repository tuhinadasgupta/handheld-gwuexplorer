package com.metro.gwuexplorer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class AlertActivity : AppCompatActivity() {

    private val alertManager: AlertManager = AlertManager()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        alertManager.retrieveAlert(
            successCallback = { alerts ->
                runOnUiThread {
                    if (alerts.isNotEmpty()){
                        recyclerView.adapter = AlertAdapter(alerts)
                    }
                    else{
                        // defensive error check
                        Toast.makeText(this@AlertActivity, "No Alerts to show", Toast.LENGTH_LONG).show()
                    }
                }
            },
            errorCallback = {
                runOnUiThread {
                    // defensive error check
                    Toast.makeText(this@AlertActivity, "Error retrieving alerts", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}
