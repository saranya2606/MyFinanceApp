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


class TransactionsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    //private val userNumber2 = intent.getStringExtra("numbert")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        val BASEURL = "http://10.0.2.2:3000/"

        val userNumber3 = intent.getStringExtra("numbert")
        Log.d("Usernumber2", "$userNumber3")


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@TransactionsActivity)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val transactions = apiService.getTransactions()

                withContext(Dispatchers.Main) {
                    val currentUserNumber = userNumber3

                    val currentUserTransactions = transactions.filter { transaction ->
                        transaction.fromUsernumber == currentUserNumber || transaction.toUsernumber == currentUserNumber
                    }
                    Log.d("transaction", "$currentUserTransactions")

                    transactionAdapter = TransactionAdapter(currentUserTransactions,this@TransactionsActivity)






                    recyclerView.adapter = transactionAdapter

                }
            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@TransactionsActivity,
                        "Error fetching transactions",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

