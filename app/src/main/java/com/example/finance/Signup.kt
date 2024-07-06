package com.example.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup : AppCompatActivity() {

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize UI components
        val emailEditText = findViewById<EditText>(R.id.emlset)
        val nameEditText = findViewById<EditText>(R.id.nameset)
        val passwordEditText = findViewById<EditText>(R.id.pwdset)
        val numberEditText = findViewById<EditText>(R.id.numberet)
        val balanceEditText = findViewById<EditText>(R.id.balanceet)
        val signupButton = findViewById<ImageView>(R.id.signupybtn)
        val loginButton = findViewById<Button>(R.id.loggedinbtn)

        // Set login button click listener
        loginButton.setOnClickListener {
            navigateToLogin()
        }

        // Set signup button click listener
        signupButton.setOnClickListener {
            val userMap = collectUserData(
                emailEditText,
                nameEditText,
                passwordEditText,
                numberEditText,
                balanceEditText
            )
            Log.d("User Data", "$userMap")
            registerUser(userMap)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun collectUserData(
        emailEditText: EditText,
        nameEditText: EditText,
        passwordEditText: EditText,
        numberEditText: EditText,
        balanceEditText: EditText
    ): HashMap<String, String> {
        return hashMapOf(
            "username" to nameEditText.text.toString(),
            "email" to emailEditText.text.toString(),
            "password" to passwordEditText.text.toString(),
            "number" to numberEditText.text.toString(),
            "balance" to balanceEditText.text.toString()
        )
    }

    private fun registerUser(userMap: HashMap<String, String>) {
        val registerUserCall = apiService.registerUser(userMap)

        registerUserCall.enqueue(object : Callback<RegisterUserResponse> {
            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                handleRegisterUserResponse(response)
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                Toast.makeText(this@Signup, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleRegisterUserResponse(response: Response<RegisterUserResponse>) {
        Log.d("API Response", "$response")

        if (response.isSuccessful) {
            val registerUserResponse = response.body()
            if (registerUserResponse != null) {
                val userName = registerUserResponse.name
                Toast.makeText(this, "Signed up successfully", Toast.LENGTH_SHORT).show()
                navigateToHome(userName)
            } else if (response.code() == 404) {
                Toast.makeText(this, "ALREADY REGISTERED", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome(userName: String) {
        val intent = Intent(this, HomeActivityPage::class.java)
        intent.putExtra("user_name", userName)
        startActivity(intent)
    }
}
