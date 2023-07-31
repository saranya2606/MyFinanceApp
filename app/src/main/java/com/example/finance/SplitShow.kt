package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplitShow : AppCompatActivity() {
    private lateinit var recyclerView1: RecyclerView
    private lateinit var splitAdapter: SplitAdapter

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_show)

        recyclerView1 = findViewById(R.id.recyclerViewSplits)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        val currentUserEmail = intent.getStringExtra("emaily")

        val call = apiService.getAllSplits()
        call.enqueue(object : Callback<List<Split>> {
            override fun onResponse(call: Call<List<Split>>, response: Response<List<Split>>) {
                if (response.isSuccessful) {
                    val splits = response.body()
                    Log.d("splits","$splits")
                    splits?.let {
                        // Filter the splits where the current user is a participant
                        val currentUserSplits = it.filter { split ->
                            split.selectedPeople.any { selectedPeople ->
                                selectedPeople.email == currentUserEmail
                            }
                        }

                        splitAdapter = SplitAdapter(currentUserSplits)
                        recyclerView1.adapter = splitAdapter
                    }
                } else {
                    Toast.makeText(
                        this@SplitShow,
                        "Error fetching splits",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Split>>, t: Throwable) {
                Toast.makeText(
                    this@SplitShow,
                    "Error fetching splits",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
