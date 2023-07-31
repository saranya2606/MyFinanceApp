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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val BASEURL="http://10.0.2.2:3000/"


        val eml = findViewById<EditText>(R.id.emailet)
        val pwd = findViewById<EditText>(R.id.pwdet)
        val log = findViewById<ImageView>(R.id.loginybtn)
        val signed = findViewById<Button>(R.id.newuserbtn)
        val retrofitBuilder2 = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .build()
        val backendService2: Retrofit1 = retrofitBuilder2.create(Retrofit1::class.java)
        val backendService3: Retrofit1 = retrofitBuilder2.create(Retrofit1::class.java)

        signed.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }


        log.setOnClickListener {
            val map2: HashMap<String, String> = HashMap()
            map2["email"] = eml.text.toString()
            map2["password"] = pwd.text.toString()
            fun intenth() {
                val intent = Intent(this, HomeActivityPage::class.java)
                startActivity(intent)
            }


            val loginUserCall = backendService2.loginUser(map2)
            loginUserCall.enqueue(object : Callback<LoginUserResponse> {

                override fun onResponse(
                    call: Call<LoginUserResponse>,
                    response: Response<LoginUserResponse>
                ) {
                    Log.d("res", "$response")

                    if (response.code()==200) {
                        val loginUserResponse = response.body()
                        Log.d("LoginResponse","$loginUserResponse")

                        if (loginUserResponse != null) {
                            val email =
                                loginUserResponse.email // Get the user's email from the response

                            // Call another API endpoint on the server to fetch the user's name based on the email
                            val getUserByNameCall = backendService3.getUserByName(email)
                            getUserByNameCall.enqueue(object : Callback<GetUserByNameResponse> {
                                override fun onResponse(
                                    call: Call<GetUserByNameResponse>,
                                    response: Response<GetUserByNameResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val getUserByNameResponse = response.body()
                                        if (getUserByNameResponse != null) {
                                            val name =
                                                getUserByNameResponse.name
                                            // Get the user's name from the response
                                            val numb=getUserByNameResponse.number

                                            // Pass the user's name to the next activity using Intent
                                            val intent =
                                                Intent(this@Login, HomeActivityPage::class.java)
                                            intent.putExtra("user_name", name)
                                            intent.putExtra("user_email", email)
                                            intent.putExtra("user_number", numb)
                                            startActivity(intent)
                                        } else {
                                            // Handle null response or missing data
                                            Toast.makeText(
                                                this@Login,
                                                "Invalid response",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        // Handle API call failure
                                        Toast.makeText(
                                            this@Login,
                                            "Failed to fetch user's name",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(
                                    call: Call<GetUserByNameResponse>,
                                    t: Throwable
                                ) {
                                    // Handle API call failure
                                    Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            // Handle null response or missing data
                            Toast.makeText(this@Login, "Invalid response", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(this@Login, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                    Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
                }

                /*loginUserCall.enqueue(object : Callback<LoginUserResponse> {
                override fun onResponse(
                    call: Call<LoginUserResponse>,
                    response: Response<LoginUserResponse>
                ) {
                    Log.d("res","$response")

                    if (response.code()==200) {
                        val name=//val loginUserResponse = response.body()
                        intenth()

                        // Handle successful login
                    } else if(response.code()==400){
                        Toast.makeText(this@Login, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()
                    }
                }



                override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                    Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
                }
            })*/


            })


        }
    }
}