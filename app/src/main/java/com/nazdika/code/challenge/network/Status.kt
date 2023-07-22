package com.nazdika.code.challenge.network

sealed class Status {
    object Success : Status()
    object Failure : Status()
}