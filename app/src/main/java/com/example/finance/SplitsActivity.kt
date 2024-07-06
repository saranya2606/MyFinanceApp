package com.example.finance

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplitsActivity : AppCompatActivity() {

    private var userNumber: String? = null
    private var userEmail: String? = null

    private lateinit var newSplitButton: Button
    private lateinit var existingSplitsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splits)

        getIntentExtras()
        initializeUIComponents()
        setButtonListeners()
    }

    private fun getIntentExtras() {
        userNumber = intent.getStringExtra("numbert")
        userEmail = intent.getStringExtra("emailt")
    }

    private fun initializeUIComponents() {
        newSplitButton = findViewById(R.id.ngbtn)
        existingSplitsButton = findViewById(R.id.egsbtn)
    }

    private fun setButtonListeners() {
        newSplitButton.setOnClickListener {
            navigateToSplitCreate()
        }

        existingSplitsButton.setOnClickListener {
            navigateToSplitShow()
        }
    }

    private fun navigateToSplitCreate() {
        val intent = Intent(this, SplitCreate::class.java).apply {
            putExtra("numbery", userNumber)
        }
        startActivity(intent)
    }

    private fun navigateToSplitShow() {
        val intent = Intent(this, SplitShow::class.java).apply {
            putExtra("numbery", userNumber)
            putExtra("emaily", userEmail)
        }
        startActivity(intent)
    }
}

