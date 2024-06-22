package com.leishmaniapp.domain.exceptions

import com.leishmaniapp.R

/**
 * Bad credentials or authentication token expired
 */
class BadAuthenticationException :
    RemoteException(R.string.exception_remote_bad_authentication)