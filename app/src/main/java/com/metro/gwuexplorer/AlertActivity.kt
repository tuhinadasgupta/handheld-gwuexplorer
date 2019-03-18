package com.metro.gwuexplorer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class AlertActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val alert = generateFakeAlert()
        recyclerView.adapter = AlertAdapter(alert)
    }

    private fun generateFakeAlert(): List<Alert> {
        return listOf(
            Alert(
                stationName = "Metro Center ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Smithsonian ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Federal Triangle ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Rossyln ",
                icon = "https://...."
        )
        )

    }
}
