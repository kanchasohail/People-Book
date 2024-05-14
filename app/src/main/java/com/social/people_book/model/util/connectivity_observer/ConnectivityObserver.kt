package com.social.people_book.model.util.connectivity_observer

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe():Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}