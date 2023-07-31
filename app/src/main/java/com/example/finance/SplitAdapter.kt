package com.example.finance

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
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val apiService: Retrofit1 by lazy {
    RetrofitClient.apiService
}



class SplitAdapter(private val splits: List<Split>) :
    RecyclerView.Adapter<SplitAdapter.SplitViewHolder>() {
    /*inner class SplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val settleButton: Button = itemView.findViewById(R.id.settleButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SplitViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_split, parent, false)

        return SplitViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return splits.size
    }

    override fun onBindViewHolder(holder: SplitViewHolder, position: Int) {

        val split = splits[position]

        // Display the split data in the views
        val participants = split.selectedPeople.joinToString(", ") { participant ->
            "${participant.username} (${participant.email})"
        }
        holder.textViewParticipants.text = participants
        holder.textViewAmount.text = "${split.amount} rupees"

        holder.settleButton.setOnClickListener {
            onSettleButtonClick(split, SplitAdapter(splits))

        }


        holder.deleteButton.setOnClickListener {
            onDeleteButtonClick(split)
        }
    }


    private fun onDeleteButtonClick(split: Split) {

    }


    private fun onSettle1ButtonClick(split: Split, adapter: SplitAdapter) {
        Log.d("check_splitbutton", "hey")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                onSettleSplit(split, adapter)
            } catch (e: Exception) {
                // Handle the exception here if needed
            }
        }
    }



    /*private suspend fun onSettleSplit(split: Split, adapter: SplitAdapter) {
        val call = apiService.settleSplit(split.id)
        call.enqueue(object : Callback<SplitSettleResponse> {
            override fun onResponse(call: Call<SplitSettleResponse>, response: Response<SplitSettleResponse>) {
                if (response.isSuccessful) {
                    Log.d("splitresponse", "$response")
                    // Split settled successfully, update the UI as needed
                    // For example, disable the "Settle" button
                    /*val position = adapter.getSplitPosition(split)
                    if (position != -1) {
                        val updatedSplit = split.copy()
                        adapter.updateSplit(position, updatedSplit)
                    }*/
                    adapter.notifyDataSetChanged()
                } else {
                    // Handle API error or failure to settle the split
                    // Show an error message to the user
                }
            }

            override fun onFailure(call: Call<SplitSettleResponse>, t: Throwable) {

                // Handle network or server error
                // Show an error message to the user
            }
        })
    }*/
    private fun onSettleButtonClick(transaction: Transactions, adapter: TransactionAdapter, position: Int) {



        val call = apiService.settleSplit(transaction.id)

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








    inner class SplitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewParticipants: TextView = itemView.findViewById(R.id.textViewParticipants)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        val settleButton: Button = itemView.findViewById(R.id.buttonSettle)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }
}
