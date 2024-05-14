package com.social.people_book.model.util.google_sign_in

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleLoginRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("859454857235-n31rskqftrgmh8q15nmkptf4fdcekprq.apps.googleusercontent.com") // Can be obtained in Google Cloud
                .setFilterByAuthorizedAccounts(true)
                .build()
//        ).setAutoSelectEnabled(true).build()
    ).build()

    fun getGoogleSignUpRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("859454857235-n31rskqftrgmh8q15nmkptf4fdcekprq.apps.googleusercontent.com") // Can be obtained in Google Cloud
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}