package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplitShow : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var splitAdapter: SplitAdapter

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_show)

        initializeViews()

        val currentUserEmail = intent.getStringExtra("emaily")

        fetchSplits(currentUserEmail)
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewSplits)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchSplits(currentUserEmail: String?) {
        val call = apiService.getAllSplits()
        call.enqueue(object : Callback<List<Split>> {
            override fun onResponse(call: Call<List<Split>>, response: Response<List<Split>>) {
                if (response.isSuccessful) {
                    val splits = response.body()
                    Log.d("splits", "$splits")
                    splits?.let { splitsList ->
                        // Filter splits where current user is a participant
                        val currentUserSplits = splitsList.filter { split ->
                            split.selectedPeople.any { selectedPerson ->
                                selectedPerson.email == currentUserEmail
                            }
                        }
                        setupRecyclerView(currentUserSplits)
                    }
                } else {
                    showErrorToast()
                }
            }

            override fun onFailure(call: Call<List<Split>>, t: Throwable) {
                showErrorToast()
            }
        })
    }

    private fun setupRecyclerView(splits: List<Split>) {
        splitAdapter = SplitAdapter(splits)
        recyclerView.adapter = splitAdapter
    }

    private fun showErrorToast() {
        Toast.makeText(this@SplitShow, "Error fetching splits", Toast.LENGTH_SHORT).show()
    }
}
