package com.social.people_book.util.google_sign_in

import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity

object GoogleSignInHelper {
    fun getGoogleSignInClient(context: Context) = Identity.getSignInClient(context)

    fun getGoogleSignInRequest() = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
//                .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID) // Can be obtained in Google Cloud
                .setServerClientId("261881744960-l7bnjhh9acc33uafj18un0u47l2gdgqr.apps.googleusercontent.com") // Can be obtained in Google Cloud
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()
}