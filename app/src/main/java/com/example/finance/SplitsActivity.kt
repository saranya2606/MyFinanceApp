package com.example.finance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplitsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splits)

        val userNumber=intent.getStringExtra("numbert")
        val userEmail=intent.getStringExtra("emailt")

        val ng = findViewById<Button>(R.id.ngbtn)
        val eg = findViewById<Button>(R.id.egsbtn)

        ng.setOnClickListener {
            val intent = Intent(this, SplitCreate::class.java)
            intent.putExtra("numbery", userNumber)
            intent.putExtra("numbery", userNumber)
            startActivity(intent)
        }

        eg.setOnClickListener {
            val intent = Intent(this, SplitShow::class.java)
            intent.putExtra("numbery", userNumber)
            intent.putExtra("emaily", userEmail)
            startActivity(intent)
        }
    }
}

