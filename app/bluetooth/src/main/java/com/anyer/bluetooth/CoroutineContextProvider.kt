package com.anyer.bluetooth

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider @Inject constructor() {
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
}