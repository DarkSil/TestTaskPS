package com.example.testtaskps.utils

/**
 * Tracks if the previous livedata value was used
 * to handle forced values only
 * */
class Event<T> (value: T) {

    var value = value
        get() {
            hasBeenHandled = true
            return field
        }
        set(value) {
            field = value
        }

    var hasBeenHandled = false
        private set

    fun getValueIfNotHandled() : T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            value
        }
    }

}