package com.metro.gwuexplorer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast

class RouteActivity : AppCompatActivity() {

    private val routeManager: RouteManager = RouteManager()
    private lateinit var share: Button
    private lateinit var direc: Button
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        share =findViewById(R.id.share)
        direc= findViewById(R.id.direc)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val intentt: Intent = intent
        val stationcodeNext:String =intentt.getStringExtra("StationCode")
        val locationName:String = intentt.getStringExtra("Name")
        val hm: HashMap<String, String> = hashMapOf( "SV" to "Silver", "GR" to "Green", "BL" to "Blue", "RD" to "Red","YL" to "Yellow", "OR" to "Orange")
        routeManager.retrieveStationList(
            primaryKey=getString(R.string.wmata_key),
            codeNext=stationcodeNext,
            successCallback = { path,line ->
                runOnUiThread {
                    val temp: String? = hm.get(line)
                    Toast.makeText(this@RouteActivity, "Please take $temp line", Toast.LENGTH_LONG).show()
                }
            },
            errorCallback = {
                runOnUiThread {
                    Toast.makeText(this@RouteActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
        )
        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "I am travelling from Foggy bottom to $locationName")
            }
            startActivity(sendIntent)
        }
        direc.setOnClickListener {
            val navigationUri = Uri.parse ("geo:38.9009,77.0505?q=" + Uri.encode(locationName+ ", Washington, DC metro"))
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

}
