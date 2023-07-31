package com.example.finance

data class CreateGroupResponse(
    val message: String,
    val groupId: String,
    val groupName: String,
    val groupAmount: Double,
    val selectedPeople: List<UserModel>
)
