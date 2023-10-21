package com.leishmaniapp.infrastructure.initialization

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin

class CloudInitialization: Initializer<Unit> {
    override fun create(context: Context) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(context)
            Log.i("AmplifyConfiguration", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("AmplifyConfiguration", "Could not initialize Amplify", error)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}