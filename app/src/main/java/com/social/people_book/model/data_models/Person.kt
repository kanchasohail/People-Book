package com.social.people_book.model.data_models

data class Person(
    val personId: Int,
    val name: String,
    val number: String?,
    val email: String?,
    val about: String?
)
