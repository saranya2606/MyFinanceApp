package com.example.finance

data class Split(
    val id: String,
    val selectedPeople: List<selectedPeople>,
    val amount: Double
)

data class selectedPeople(

    val email: String,
    val username: String
)