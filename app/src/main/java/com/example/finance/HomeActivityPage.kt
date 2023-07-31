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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val BASEURL="http://10.0.2.2:3000/"


        val userName = intent.getStringExtra("user_name")

        val nameet=findViewById<TextView>(R.id.namet)
        nameet.text=userName
        val owee=findViewById<TextView>(R.id.oweet)
        val lent=findViewById<TextView>(R.id.lentet)
        val split=findViewById<ImageView>(R.id.splitiv)
        val trans=findViewById<ImageView>(R.id.transiv)



        val userEmail = intent.getStringExtra("user_email")
        val userNumber = intent.getStringExtra("user_number")




        split.setOnClickListener {
            Log.d("Splits","heyc")
            val intent=Intent(this,SplitsActivity::class.java)
            intent.putExtra("numbert", userNumber)
            intent.putExtra("emailt",userEmail)
            startActivity(intent)
        }

        trans.setOnClickListener {


            val intent=Intent(this,TransactionsActivity::class.java)
            intent.putExtra("numbert", userNumber)
            startActivity(intent)
        }











        val apiService = RetrofitClient.apiService
        val call = apiService.getOwedAndLentData(userEmail)

        //val call = apiService.getOwedAndLentData(userEmail)

        call.enqueue(object : Callback<OwedAndLentResponse> {
            override fun onResponse(
                call: Call<OwedAndLentResponse>,
                response: Response<OwedAndLentResponse>
            ) {
                // ...

                if (response.isSuccessful) {
                    Log.d("owedlent_Response","${response.body()}")
                    val data = response.body()
                    data?.let {
                        // Use the user's number to retrieve the money owed and money lent values
                        val userLentAmount = it.userLent[userNumber] ?: 0.0
                        val userOwedAmount = it.userOwed[userNumber] ?: 0.0

                        owee.text=userOwedAmount.toString()
                        lent.text=userLentAmount.toString()




                    }
                }

                // ...
            }

            override fun onFailure(call: Call<OwedAndLentResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivityPage, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}