package de.syntax_institut.jetpack.a04_05_online_shopper.data.model.user

data class Address(
    val geolocation: Geolocation,
    val city: String,
    val street: String,
    val number: Long,
    val zipcode: String,
)

