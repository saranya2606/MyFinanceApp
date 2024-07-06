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

    private val selectedUsers = mutableListOf<User>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: UserAdapter
    private val apiService: Retrofit1 by lazy {
        RetrofitClient.apiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_create)

        initializeViews()

        val addUserButton = findViewById<Button>(R.id.addUserButton)
        val createGroupButton = findViewById<Button>(R.id.createGroupButton)

        addUserButton.setOnClickListener {
            showAddUserDialog()
        }

        createGroupButton.setOnClickListener {
            createGroup()
        }
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = UserAdapter(selectedUsers)
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun showAddUserDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editTextName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editTextEmail)

        AlertDialog.Builder(this)
            .setTitle("Add User")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = nameEditText.text.toString().trim()
                val email = emailEditText.text.toString().trim()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    val user = User(name, email)
                    selectedUsers.add(user)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createGroup() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val groupId = "your_group_id"
                val groupName = "Your Group Name"
                val groupAmount = 100.0
                val participants = selectedUsers.map { UserModel(it.name, it.email) }

                val response = apiService.createGroup(GroupData(groupId, groupName, groupAmount, participants))
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val groupId = responseBody.groupId
                        val groupName = responseBody.groupName
                        val groupAmount = responseBody.groupAmount
                        val participants = responseBody.selectedPeople
                        // Handle successful group creation
                    }
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                Log.e("SplitCreate", "Error creating group", e)
            }
        }
    }
}
