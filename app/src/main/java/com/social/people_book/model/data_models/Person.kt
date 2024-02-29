package com.social.people_book.model.data_models

data class Person(
    val personId: String,
    val name: String,
    val number: String?,
    val email: String?,
    val about: String?
){
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            "$number",
            "$email",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
