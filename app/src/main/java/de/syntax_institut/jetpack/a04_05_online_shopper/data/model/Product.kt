package de.syntax_institut.jetpack.a04_05_online_shopper.data.model

data class Product(
    val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
//    val user: User,
//    val Auth: Auth
)
