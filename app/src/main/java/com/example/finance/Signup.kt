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


class Signup : AppCompatActivity() {

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val BASEURL="kk"
        val emls=findViewById<EditText>(R.id.emlset)
        val names=findViewById<EditText>(R.id.nameset)
        val pwds=findViewById<EditText>(R.id.pwdset)
        val net=findViewById<EditText>(R.id.numberet)
        val bet=findViewById<EditText>(R.id.balanceet)
        val sign=findViewById<ImageView>(R.id.signupybtn)
        val alog=findViewById<Button>(R.id.loggedinbtn)









        alog.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }


        sign.setOnClickListener {
            val map: HashMap<String, String> = HashMap()
            map["username"] = names.text.toString()
            map["email"] = emls.text.toString()
            map["password"] = pwds.text.toString()
            map["number"]=net.text.toString()
            map["balance"]=bet.text.toString()
            Log.d("MAP", "$map")


            val registerUserCall = apiService.registerUser(map)

            registerUserCall.enqueue(object : Callback<RegisterUserResponse> {
                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    Log.d("MAP2", "$map")
                    Log.d("res", "$response")

                    if (response.isSuccessful) {
                        val getUserByNameResponse = response.body()
                        if (getUserByNameResponse != null) {
                            val name =
                                getUserByNameResponse.name
                            // Get the user's name from the response
                            //val numb=getUserByNameResponse.number
                            //val registerUserResponse = response.body()
                            Toast.makeText(
                                this@Signup,
                                "Signed up successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@Signup, HomeActivityPage::class.java)
                            intent.putExtra("user_name", name)
                            startActivity(intent)
                            // Handle successful registration
                        } else if (response.code() == 404) {
                            Toast.makeText(
                                this@Signup,
                                "ALREADY REGISTERED",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()

                            // Handle registration error
                        }
                    }


                }

                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    Toast.makeText(this@Signup, t.message, Toast.LENGTH_SHORT).show()
                }
            })

        }


    }
}
