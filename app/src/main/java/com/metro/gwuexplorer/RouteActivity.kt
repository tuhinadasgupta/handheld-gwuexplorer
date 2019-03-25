package com.metro.gwuexplorer

import android.content.Intent
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
    private lateinit var direction: Button
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        //variables from the alert xml file
        share =findViewById(R.id.share)
        direction= findViewById(R.id.direc)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //taken from intent passed from main activity's intent.putExtra
        val stationCodeNext:String =intent.getStringExtra("StationCode")
        val locationName:String = intent.getStringExtra("Name")
        //used linecode from watma
        val hm: HashMap<String, String> = hashMapOf( "SV" to "Silver", "GR" to "Green", "BL" to "Blue", "RD" to "Red","YL" to "Yellow", "OR" to "Orange")
        routeManager.retrieveStationList(
            primaryKey=getString(R.string.wmata_key), //not used directly here but in routeManager
            codeNext=stationCodeNext,
            //upon success
            successCallback = { path,line ->
                runOnUiThread {
                    recyclerView.adapter = RouteAdapter(path) //path of metro stops
                    val temp: String? = hm.get(line) //tells you what line (blue,orange,silver) to take
                    Toast.makeText(this@RouteActivity, "Please take $temp line", Toast.LENGTH_LONG).show()
                }
            },
            //upon failure --> error handling
            errorCallback = {
                runOnUiThread {
                    Toast.makeText(this@RouteActivity, "Error", Toast.LENGTH_LONG).show() //display error message
                }
            }
        )
        //implicit intent to share your travel plans w/someone
        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "I am travelling from Foggy bottom to $locationName via the metro")
            }
            startActivity(sendIntent)
        }
        //explicit intent that launches google maps to show you where you're headed
        direction.setOnClickListener {
            //foggy bottom lat and long
            val navigationUri = Uri.parse ("geo:38.9009,-77.0505?q=" + Uri.encode(locationName+ ", Washington, DC metro"))
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

}
