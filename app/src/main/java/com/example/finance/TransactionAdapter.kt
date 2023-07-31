package com.example.finance



import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


var isSettled=false

class TransactionAdapter(private val transactions: List<Transactions>,private val context: Context) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transactionr = transactions[position]
        Log.d("TransactionData", transactionr.toString())
        Log.d("onBindViewHolder", "Position: $position, Username: ${transactionr.fromUsernumber}, Amount: ${transactionr.amount}")
        if (isSettled) {
            holder.buttonSettle.text = "Settled"
            holder.buttonSettle.isEnabled = false
        } else {
            holder.buttonSettle.text = "Settled"
            holder.buttonSettle.isEnabled = true
        }




        holder.textViewUserName.text = transactionr.fromUsernumber
        holder.textViewAmount.text = transactionr.amount.toString()
        holder.buttonSettle.setOnClickListener {
            onSettleTransaction(transactionr, TransactionAdapter(transactions,context),position)
            // Handle the "Settle" button click here
        }
    }
    private fun onSettleTransaction(transaction: Transactions, adapter: TransactionAdapter, position: Int) {



        val call = apiService.settleTransaction(transaction.id)

        call.enqueue(object : Callback<SettleResponse> {
            override fun onResponse(call: Call<SettleResponse>, response: Response<SettleResponse>) {
                if (response.isSuccessful) {
                    val settleResponse = response.body()
                    Log.d("transaction_reponse","$settleResponse")
                    if (settleResponse != null) {
                        isSettled=true
                        // Transaction settled successfully, update the UI as needed
                        // For example, disable the "Settle" button and show a success message

                        adapter.notifyItemChanged(position)
                        Toast.makeText(
                            context, // Replace with your activity reference
                            "Transaction settled successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Handle null response
                        // Show an error message to the user
                        Toast.makeText(
                            context, // Replace with your activity reference
                            "Failed to settle transaction",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Handle API error or failure to settle the transaction
                    // Show an error message to the user
                    Toast.makeText(
                        context, // Replace with your activity reference
                        "Failed to settle transaction",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SettleResponse>, t: Throwable) {
                // Handle network or server error
                // Show an error message to the user
                Toast.makeText(
                    context, // Replace with your activity reference
                    "Failed to settle transaction",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }



    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val buttonSettle: Button = itemView.findViewById(R.id.buttonSettle)

        init {
            buttonSettle.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val transaction = transactions[position]
                    /*if (!transaction.isSettled) {
                        CoroutineScope(Dispatchers.Main).launch {
                            onSettleTransaction(transaction, this@TransactionAdapter, position)
                        }
                    }*/
                }
            }
        }
    }
}
 /*class TransactionAdapter(
    private val transactions: List<Transactions>,
    private val settledTransactions: Transactions //= mutableListOf()
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    val adapter = TransactionAdapter(transactions)

    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        // Update UI elements with transaction data
        holder.textViewUserName.text = transaction.fromUsernumber
        holder.textViewAmount.text = transaction.amount.toString()

        // Check if the current transaction is settled based on its ID
        //val isSettled = settledTransactions.contains(transaction.id)

        if (isSettled) {
            // If the transaction is settled, disable the "Settle" button and set text to "Settled"
            holder.buttonSettle.apply {
                isEnabled = false
                text = "Settled"
            }
        } else {
            // If the transaction is not settled, enable the "Settle" button and set text to "Settle"
            holder.buttonSettle.apply {
                isEnabled = true
                text = "Settle"
            }

            // Set a click listener for the "Settle" button
            holder.buttonSettle.setOnClickListener {
                // Call the onSettleTransaction function and pass the transaction object
                onSettleTransaction(settledTransactions , adapter, position)
            }
        }
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.textViewUserName)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val buttonSettle: Button = itemView.findViewById(R.id.buttonSettle)
    }

    private fun onSettleTransaction(transaction: Transactions, adapter: TransactionAdapter, position: Int) {




        /*val call = apiService.settleTransaction(transaction.id)

        call.enqueue(object : Callback<SettleResponse> {
            override fun onResponse(call: Call<SettleResponse>, response: Response<SettleResponse>) {
                if (response.isSuccessful) {
                    settledTransactions.add(transaction)
                    // Transaction settled successfully, update the UI as needed
                    // For example, disable the "Settle" button and show a success message
                    adapter.notifyItemChanged(position)
                    Toast.makeText(
                        this@YourActivity, // Replace with your activity reference
                        "Transaction settled successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle API error or failure to settle the transaction
                    // Show an error message to the user
                    Toast.makeText(this@TransactionAct, // Replace with your activity reference
                        "Failed to settle transaction",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SettleResponse>, t: Throwable) {
                // Handle network or server error
                // Show an error message to the user
                Toast.makeText(
                    this@YourActivity, // Replace with your activity reference
                    "Failed to settle transaction",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })*/
    }

  */




