package de.syntax_institut.jetpack.a04_05_online_shopper.data.model

data class User(
    val address: Address,
    val id: Long,
    val email: String,
    val username: String,
    val password: String,
    val name: Name,
    val phone: String,
    val v: Long,
)