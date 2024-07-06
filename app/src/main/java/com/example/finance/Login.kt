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

class Login : AppCompatActivity() {

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: ImageView
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeUIComponents()

        signupButton.setOnClickListener {
            navigateToSignup()
        }

        loginButton.setOnClickListener {
            val userCredentials = collectUserCredentials()
            loginUser(userCredentials)
        }
    }

    private fun initializeUIComponents() {
        emailEditText = findViewById(R.id.emailet)
        passwordEditText = findViewById(R.id.pwdet)
        loginButton = findViewById(R.id.loginybtn)
        signupButton = findViewById(R.id.newuserbtn)
    }

    private fun navigateToSignup() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
    }

    private fun collectUserCredentials(): HashMap<String, String> {
        return hashMapOf(
            "email" to emailEditText.text.toString(),
            "password" to passwordEditText.text.toString()
        )
    }

    private fun loginUser(credentials: HashMap<String, String>) {
        val loginUserCall = apiService.loginUser(credentials)
        loginUserCall.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call: Call<LoginUserResponse>, response: Response<LoginUserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    handleSuccessfulLogin(response.body()!!)
                } else if (response.code() == 401) {
                    Toast.makeText(this@Login, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Login, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSuccessfulLogin(loginResponse: LoginUserResponse) {
        val email = loginResponse.email
        fetchUserDetails(email)
    }

    private fun fetchUserDetails(email: String) {
        val getUserByNameCall = apiService.getUserByName(email)
        getUserByNameCall.enqueue(object : Callback<GetUserByNameResponse> {
            override fun onResponse(call: Call<GetUserByNameResponse>, response: Response<GetUserByNameResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    navigateToHome(response.body()!!)
                } else {
                    Toast.makeText(this@Login, "Failed to fetch user's name", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetUserByNameResponse>, t: Throwable) {
                Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToHome(userDetails: GetUserByNameResponse) {
        val intent = Intent(this, HomeActivityPage::class.java)
        intent.putExtra("user_name", userDetails.name)
        intent.putExtra("user_email", emailEditText.text)
        intent.putExtra("user_number", userDetails.number)
        startActivity(intent)
    }
}
