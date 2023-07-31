package com.example.finance

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SplitCreate : AppCompatActivity() {

    private val selectedUsersList = mutableListOf<User>()

    // ...


    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_create)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = UserAdapter(selectedUsersList)
        recyclerView.adapter = recyclerViewAdapter

        // Initialize UI components (e.g., EditText, Button) for user input
        val addUserButton = findViewById<Button>(R.id.addUserButton)
        val createGroupButton = findViewById<Button>(R.id.createGroupButton)

        addUserButton.setOnClickListener {
            showAddUserDialog()
        }

        createGroupButton.setOnClickListener {
            // Create the group by sending a POST request to the server with selected users
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Collect group data, including selected users
                    val groupId = "your_group_id" // Replace with your group ID generation logic
                    val groupName = "Your Group Name" // Replace with the entered group name
                    val groupAmount = 100.0 // Replace with the entered group amount
                    val selectedPeople = selectedUsersList.map { user ->
                        UserModel(user.name, user.email)
                    }

                    // Send the data to the server using Retrofit
                    val response = apiService.createGroup(GroupData(groupId, groupName, groupAmount, selectedPeople))
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        // Handle the response containing group ID, name, amount, and participants
                        if (responseBody != null) {
                            val groupId = responseBody.groupId
                            val groupName = responseBody.groupName
                            val groupAmount = responseBody.groupAmount
                            val participants = responseBody.selectedPeople
                            // You can now use this information to update your UI
                        }
                    } else {
                        // Group creation failed, handle the error
                    }
                } catch (e: Exception) {
                    // Handle any exceptions
                }
            }
        }

    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editTextName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editTextEmail)

        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Add User")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val user = User(name, email)
                    selectedUsersList.add(user)
                    Log.d("LISTS","$selectedUsersList")
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }



    // ...
}
