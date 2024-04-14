package com.example.testtaskps.utils

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