package com.example.younessandroidlabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textview)
        var myedit = findViewById<EditText>(R.id.myedittext)
        var editString = myedit.text
        myedit.setText( "Your edit text has: $editString ")




    }
}