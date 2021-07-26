package com.adwi.pexwallpapers.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


// Cold flow - executed only if there is collector
@ExperimentalCoroutinesApi
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline queryLocal: () -> Flow<ResultType>,
    crossinline fetchRemote: suspend () -> RequestType,
    crossinline saveRemoteToLocal: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = channelFlow {
    val data = queryLocal().first()

    if (shouldFetch(data)) {
        val loading = launch {
            queryLocal().collect { send(Resource.Loading(it)) }
        }
        try {
            delay(2000)
            saveRemoteToLocal(fetchRemote())
            loading.cancel()
            queryLocal().collect { send(Resource.Success(it)) }
        } catch (t: Throwable) {
            loading.cancel()
            queryLocal().collect { send(Resource.Error(t, it)) }
        }
    } else {
        queryLocal().collect { send(Resource.Success(it)) }
    }
}