package com.example.finance

data class GroupResponse(
    val message: String,
    val groupId: String,
    val participants: List<Person>,
    val splitAmount: Double
)