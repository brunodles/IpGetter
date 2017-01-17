package com.brunodles.test.helper

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun <T> once(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}