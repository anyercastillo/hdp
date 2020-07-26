package com.anyer.bluetooth

class CharacteristicReadException : Exception() {
    override fun equals(other: Any?): Boolean {
        if (other !is CharacteristicReadException) return false

        return message.equals(other.message)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}