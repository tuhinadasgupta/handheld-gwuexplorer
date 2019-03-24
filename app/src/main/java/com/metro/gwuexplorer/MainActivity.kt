package com.metro.gwuexplorer

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val manager: StationManager = StationManager()
    private lateinit var stationname : EditText
    private lateinit var remember: CheckBox
    private lateinit var go : Button
    private lateinit var alert: Button
    private lateinit var checkedBox: CheckBox
    private lateinit var text: String
    private  var bool1: Boolean = false
    private lateinit var first: Address
    private lateinit var temp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stationname = findViewById(R.id.stationname)
        remember = findViewById(R.id.remember)
        go = findViewById(R.id.go)
        alert = findViewById(R.id.alert)
        checkedBox = findViewById(R.id.remember)
        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("\n\nPlease enter location to travel from GWU and use Alert button to use metro outage" +
                    "\n\n\nNote: Currently works only for the location which does not require transfer")
            .setPositiveButton("OK"){ _, _ ->  }
            .show()
        go.setOnClickListener {
               //instantiate instance of geocoder
                val geocoder = Geocoder(this, Locale.getDefault())
                val locationName : String? = stationname.text.toString()
                val maxResults = 3
                val results: List<Address>? = geocoder.getFromLocationName(locationName, maxResults)
                if (results != null && results.isNotEmpty()) {
                first = results[0]
                manager.retrieveNearbyStation(
                //passing the first result & OAth key
                primaryKey=getString(R.string.wmata_key),
                address = first,
                successCallback = { list,station ->
                    runOnUiThread {
                        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
                        arrayAdapter.addAll(list)
                        if(list.isEmpty()){
                            Toast.makeText(this@MainActivity, "List is empty...Try again", Toast.LENGTH_LONG).show()
                        }

                        AlertDialog.Builder(this)
                            .setTitle("Select an option")
                            .setAdapter(arrayAdapter) { _, which ->
                                Toast.makeText(this, "You picked: ${list[which]}", Toast.LENGTH_SHORT).show()
                            Log.d("code", station[which])
                                temp= station[which]
                                val intent = Intent(this, RouteActivity::class.java)
                                intent.putExtra("StationCode", temp)
                                intent.putExtra("Name", list[which])
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                },
                errorCallback = {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error retrieving Station name", Toast.LENGTH_LONG).show()
                    }
                })
                        saveData()
                }
                else{
                    //defensive error handling 
                    Toast.makeText(this@MainActivity, "Try new name", Toast.LENGTH_LONG).show()
                }
        }
        alert.setOnClickListener {
            val intent2 = Intent(this, AlertActivity::class.java)
            startActivity(intent2)
        }
        loadData()
        updateText()
    }

    //shared preference
    private  fun saveData ()
    {
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        preferences.edit().putString("SAVED_STATIONNAME", stationname.text.toString()).apply()
        preferences.edit().putBoolean("CHECKBOX",remember.isChecked).apply()
    }
    private fun loadData ()
    {
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        text = preferences.getString("SAVED_STATIONNAME","")
        bool1=preferences.getBoolean("CHECKBOX",false)
    }
     private fun updateText(){
         stationname.setText(text)
         remember.isChecked = bool1
    }
}

