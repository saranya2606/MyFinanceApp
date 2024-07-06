package com.example.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityPage : AppCompatActivity() {

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    private lateinit var nameTextView: TextView
    private lateinit var oweTextView: TextView
    private lateinit var lentTextView: TextView
    private lateinit var splitImageView: ImageView
    private lateinit var transImageView: ImageView

    private var userName: String? = null
    private var userEmail: String? = null
    private var userNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        initializeUIComponents()
        getIntentExtras()
        setUserName()
        setButtonListeners()
        fetchOwedAndLentData()
    }

    private fun initializeUIComponents() {
        nameTextView = findViewById(R.id.namet)
        oweTextView = findViewById(R.id.oweet)
        lentTextView = findViewById(R.id.lentet)
        splitImageView = findViewById(R.id.splitiv)
        transImageView = findViewById(R.id.transiv)
    }

    private fun getIntentExtras() {
        userName = intent.getStringExtra("user_name")
        userEmail = intent.getStringExtra("user_email")
        userNumber = intent.getStringExtra("user_number")
    }

    private fun setUserName() {
        nameTextView.text = userName
    }

    private fun setButtonListeners() {
        splitImageView.setOnClickListener {
            navigateToSplitsActivity()
        }

        transImageView.setOnClickListener {
            navigateToTransactionsActivity()
        }
    }

    private fun navigateToSplitsActivity() {
        val intent = Intent(this, SplitsActivity::class.java).apply {
            putExtra("numbert", userNumber)
            putExtra("emailt", userEmail)
        }
        startActivity(intent)
    }

    private fun navigateToTransactionsActivity() {
        val intent = Intent(this, TransactionsActivity::class.java).apply {
            putExtra("numbert", userNumber)
        }
        startActivity(intent)
    }

    private fun fetchOwedAndLentData() {
        userEmail?.let { email ->
            val call = apiService.getOwedAndLentData(email)
            call.enqueue(object : Callback<OwedAndLentResponse> {
                override fun onResponse(
                    call: Call<OwedAndLentResponse>,
                    response: Response<OwedAndLentResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            updateOwedAndLentData(data)
                        }
                    } else {
                        showToast("Failed to fetch data: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<OwedAndLentResponse>, t: Throwable) {
                    showToast(t.message)
                }
            })
        }
    }

    private fun updateOwedAndLentData(data: OwedAndLentResponse) {
        userNumber?.let { number ->
            val userLentAmount = data.userLent[number] ?: 0.0
            val userOwedAmount = data.userOwed[number] ?: 0.0

            oweTextView.text = userOwedAmount.toString()
            lentTextView.text = userLentAmount.toString()
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }
}
