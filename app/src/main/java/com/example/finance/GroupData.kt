package com.example.finance

data class GroupData(
    val id: String,
    val groupName: String,
    val amount: Double,
    val selectedPeople: List<UserModel>,
)

data class Person(
    val email: String,
    val name: String
)