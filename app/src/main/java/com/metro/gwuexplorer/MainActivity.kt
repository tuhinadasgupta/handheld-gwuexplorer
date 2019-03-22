package com.metro.gwuexplorer

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var stationname : EditText
    private lateinit var remember: CheckBox
    private lateinit var go : Button
    private lateinit var alert: Button
    private lateinit var checkedBox: CheckBox
    private lateinit var text: String
    private  var bool1: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stationname = findViewById(R.id.stationname)
        remember = findViewById(R.id.remember)
        go = findViewById(R.id.go)
        alert = findViewById(R.id.alert)
        checkedBox = findViewById(R.id.remember);

        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("\n\nPlease enter location to travel from GWU and use Alert button to use metro outage" +
                    "\n\n\nNote: Currently works only for the location which does not require transfer")
            .setPositiveButton("OK"){dialog, which ->  }
            .show()

        if (checkedBox.isChecked()) {
            val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
            preferences.edit().putString("saved_stationName", stationname.text.toString()).apply()
            val savedStationName = preferences.getString("saved_stationName", "")
        }

        go.setOnClickListener {
                val geocoder = Geocoder(this, Locale.getDefault())
                val locationName = stationname.getText().toString()
                val maxResults = 3
                val results: List<Address> = geocoder.getFromLocationName(locationName, maxResults)
                lateinit var addr: MutableList<String?>
                if (results != null && results.size > 0) {
                    val first = results[0]
                    val firstAdd = first.getAddressLine(0)
                    addr = mutableListOf(firstAdd)
                }
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(addr)
            AlertDialog.Builder(this)
                .setTitle("Select an option")
                .setAdapter(arrayAdapter) { dialog, which ->
                    Toast.makeText(this, "You picked: ${addr[which]}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
            saveData();
            loadData();
        }

        alert.setOnClickListener {
            val intent2: Intent = Intent(this, AlertActivity::class.java)
            startActivity(intent2)
        }
        loadData()
        updateText()

    }
   private  fun saveData (){
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        preferences.edit().putString("SAVED_STATIONNAME", stationname.text.toString()).apply()
        preferences.edit().putBoolean("CHECKBOX",remember.isChecked).apply()
    }
    private fun loadData (){
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        text = preferences.getString("SAVED_STATIONNAME","")
        bool1=preferences.getBoolean("CHECKBOX",false)
    }
     fun updateText(){
         stationname.setText(text)
         remember.setChecked(bool1)
    }
}
