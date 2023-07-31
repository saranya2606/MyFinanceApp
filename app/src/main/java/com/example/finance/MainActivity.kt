package com.example.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sign=findViewById<ImageView>(R.id.signupbtn)
        val login=findViewById<ImageView>(R.id.loginbtn)


        sign.setOnClickListener {
            val intent= Intent(this,Signup::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }




    }
}