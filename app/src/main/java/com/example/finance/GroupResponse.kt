package com.example.finance

data class GroupResponse(
    val message: String,
    val groupId: String, // The ID of the newly created group
    val participants: List<Person>, // List of participants in the group
    val splitAmount: Double // The split amount for the group
)