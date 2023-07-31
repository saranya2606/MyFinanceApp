package com.example.finance

data class OwedAndLentResponse (
    val userLent: Map<String, Double>,
    val userOwed: Map<String, Double>
)
